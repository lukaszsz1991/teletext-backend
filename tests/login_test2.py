import requests

def test_login_success():
    url = "http://localhost:3000/admin/login"
    payload = {
        "email": "admin@test.pl",
        "password": "admin123"
    }
    headers = {"Content-Type": "application/json"}

    response = requests.post(url, json=payload, headers=headers)
    print("Status:", response.status_code)
    print("Body:", response.text)

    assert response.status_code == 200
    data = response.json()
    assert "token" in data
    assert isinstance(data["token"], str)
