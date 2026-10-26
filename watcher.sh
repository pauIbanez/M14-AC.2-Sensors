#!/usr/bin/env bash
# Watches the project and rebuilds + relaunches the app on the connected emulator/device
# whenever a source or build file changes. Stop with Ctrl+C.
set -u

cd "$(dirname "$0")"

PACKAGE="com.example.aa2_sensors"
ACTIVITY=".MainActivity"
WATCH_PATHS=(app/src/main app/build.gradle.kts build.gradle.kts settings.gradle.kts gradle/libs.versions.toml)
INTERVAL=1
# Only these log tags are shown. System.out is where println() goes.
LOG_TAGS=(MainActivity System.out)

marker="$(mktemp)"
logcat_pid=""
trap 'stop_logcat; rm -f "$marker"; echo; echo "Watcher stopped."; exit 0' INT TERM

log() { echo "[$(date +%T)] $*"; }

stop_logcat() {
  if [ -n "$logcat_pid" ]; then
    kill "$logcat_pid" 2>/dev/null
    wait "$logcat_pid" 2>/dev/null
    logcat_pid=""
  fi
}

# Streams the app's logs (Log.*, println) in the background. The app gets a new
# process id on every relaunch, so this is restarted after each deploy.
start_logcat() {
  stop_logcat
  local pid=""
  for _ in $(seq 1 20); do
    pid="$(adb shell pidof -s "$PACKAGE" 2>/dev/null | tr -d '\r')"
    [ -n "$pid" ] && break
    sleep 0.25
  done
  if [ -z "$pid" ]; then
    log "App process not found; no logs to show."
    return
  fi
  log "Showing logs for $PACKAGE (pid $pid):"
  adb logcat -v time --pid="$pid" -s "${LOG_TAGS[@]/%/:V}" &
  logcat_pid=$!
}

deploy() {
  log "Building and installing..."
  if ./gradlew installDebug -q; then
    adb shell am start -S -n "$PACKAGE/$ACTIVITY" >/dev/null && log "App reloaded." && start_logcat
  else
    log "Build failed. Fix the error and save again."
  fi
}

log "Waiting for a device..."
adb wait-for-device

touch "$marker"
deploy
log "Watching for changes (Ctrl+C to stop)..."

while true; do
  sleep "$INTERVAL"
  changed="$(find "${WATCH_PATHS[@]}" -type f -newer "$marker" -print -quit 2>/dev/null)"
  if [ -n "$changed" ]; then
    sleep 0.5 # let the editor finish writing
    # Touch the marker before building so edits made during the build trigger another run.
    touch "$marker"
    log "Change detected: $changed"
    deploy
  fi
done
