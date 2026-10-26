import socket, time, pathlib, requests

PHONE = "http://192.168.16.43"   # URL phyphox shows under Remote Access
token = (pathlib.Path.home() / ".emulator_console_auth_token").read_text().strip()

emu = socket.create_connection(("localhost", 5554))
emu.recv(4096)
emu.sendall(f"auth {token}\n".encode()); emu.recv(4096)

while True:
    data = requests.get(f"{PHONE}/get?accX&accY&accZ", timeout=1).json()["buffer"]
    x, y, z = (data[k]["buffer"][-1] for k in ("accX", "accY", "accZ"))
    if None not in (x, y, z):
        emu.sendall(f"sensor set acceleration {x}:{y}:{z}\n".encode())
        emu.recv(4096)
    time.sleep(0.05)  # ~20 Hz
