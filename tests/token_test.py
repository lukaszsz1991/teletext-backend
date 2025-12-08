import requests

def test_admin_login():
    url = "http://localhost:8080/api/admin/auth/login"
    payload = {
        "username": "admin",
        "password": "admin"
    }

    response = requests.post(url, json=payload)

    assert response.status_code == 200, f"Unexpected status code: {response.status_code}"
    data = response.json()
    assert "token" in data, "JWT token not found in response"
    print("Login successful, token:", data["token"])
