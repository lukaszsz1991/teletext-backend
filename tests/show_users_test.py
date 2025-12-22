import requests

BASE_URL = "http://localhost:8080/api"
LOGIN_URL = f"{BASE_URL}/admin/auth/login"
USERS_URL = f"{BASE_URL}/admin/users"

def get_token():
    response = requests.post(LOGIN_URL, json={
        "username": "admin",
        "password": "admin"
    })
    response.raise_for_status()
    return response.json()["token"]

def get_users():
    token = get_token()
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.get(USERS_URL, headers=headers)
    response.raise_for_status()
    users = response.json()

    print("Lista użytkowników:")
    for user in users:
        print(f"- ID: {user.get('id')}, Username: {user.get('username')}, Email: {user.get('email')}")

get_users()
