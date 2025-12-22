import requests

BASE_URL = "http://localhost:8080/api"
LOGIN_URL = f"{BASE_URL}/admin/auth/login"

def get_token():
    response = requests.post(LOGIN_URL, json={"username": "admin", "password": "admin"})
    assert response.status_code == 200
    return response.json()["token"]

def test_get_users():
    token = get_token()
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.get(f"{BASE_URL}/admin/users", headers=headers)

    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, list)
