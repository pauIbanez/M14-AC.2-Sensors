import socket, time, pathlib, requests

PHONE = "http://192.168.1.41"   # URL phyphox shows under Remote Access
token = (pathlib.Path.home() / ".emulator_console_auth_token").read_text().strip()

PHONE_ERRORS = (requests.RequestException, ValueError, KeyError)


def read_phone(timeout):
    return requests.get(f"{PHONE}/get?accX&accY&accZ", timeout=timeout).json()["buffer"]


def connect_emulator():
    while True:
        try:
            emu = socket.create_connection(("localhost", 5554), timeout=2)
            emu.recv(4096)
            emu.sendall(f"auth {token}\n".encode())
            emu.recv(4096)
            print("Connected to emulator")
            return emu
        except OSError:
            print("Waiting for emulator...")
            time.sleep(1)


# The only fatal check: if the phone can't be reached at startup, the IP is wrong
try:
    read_phone(timeout=3)
except PHONE_ERRORS as e:
    raise SystemExit(f"Can't reach phyphox at {PHONE}, check the IP\nError:{e}")

emu = connect_emulator()
phone_connected = True

while True:
    time.sleep(0.05)

    try:
        data = read_phone(timeout=1)
        x, y, z = (data[k]["buffer"][-1] for k in ("accX", "accY", "accZ"))
    except IndexError:  # Connected, but the experiment has no data yet
        continue
    except PHONE_ERRORS:
        if phone_connected:
            print("Lost connection to phone, retrying...")
            phone_connected = False
        time.sleep(1)
        continue

    if not phone_connected:
        print("Phone reconnected")
        phone_connected = True

    if None not in (x, y, z):
        try:
            emu.sendall(f"sensor set acceleration {x}:{y}:{z}\n".encode())
            if not emu.recv(4096):
                raise ConnectionError("emulator closed the connection")
        except OSError:
            print("Lost connection to emulator, reconnecting...")
            emu.close()
            emu = connect_emulator()
