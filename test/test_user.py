import pytest
import requests

BASE_URL = "http://localhost:8080/api"
LOGIN_URL = f"{BASE_URL}/admin/auth/login"

@pytest.fixture
def token():
    headers = {'Content-Type': 'application/json'}
    response = requests.post(LOGIN_URL, json={"username": "admin", "password": "admin"}, headers=headers)
    assert response.status_code == 200
    return response.json()['token']

def auth_header(token):
    return {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

@pytest.mark.parametrize("include_deleted", [True, False])
def test_users(token, include_deleted):
    headers = {'Authorization': f'Bearer {token}'}
    response = requests.get(
        url=f"{BASE_URL}/admin/users",
        headers=headers,
        params={"includeDeleted": str(include_deleted).lower()}
    )
    assert response.status_code == 200
    assert isinstance(response.json(), list)

def test_get_single_user(token):
    user_id = 1
    response = requests.get(
        url=f"{BASE_URL}/admin/users/{user_id}",
        headers=auth_header(token),
        timeout=5
    )

    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, dict)
    assert data["id"] == user_id
    assert "username" in data
    assert "email" in data

def test_get_nonexist_user(token):
    user_id = 9999
    response = requests.get(
        url=f"{BASE_URL}/admin/users/{user_id}",
        headers=auth_header(token),
        timeout=5
    )

    assert response.status_code == 404

def test_add_user(token):
    payload = {
        "username": "testuser12",
        "email": "testuser12@example.com",
        "password": "pass123",
        "repeatPassword": "pass123"
    }

    response = requests.post(
        url=f"{BASE_URL}/admin/users",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert response.status_code == 201
    data = response.json()
    assert data["username"] == payload["username"]
    assert data["email"] == payload["email"]

def test_edit_user(token):
    user_id = 2
    payload = {
        "username": "testuser2",
        "email": "testuser2@example.com"
    }

    response = requests.put(
        url=f"{BASE_URL}/admin/users/{user_id}",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert response.status_code == 200  # zakładamy, że edycja zwraca 200 OK
    data = response.json()
    assert data["id"] == user_id
    assert data["username"] == payload["username"]
    assert data["email"] == payload["email"]

def test_change_user_password(token):
    user_id = 2
    payload = {
        "password": "password1",
        "repeatPassword": "password1"
    }

    response = requests.put(
        url=f"{BASE_URL}/admin/users/{user_id}/change-password",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert response.status_code == 204

def test_change_user_password_wrong(token):
    user_id = 2
    payload = {
        "password": "password1",
        "repeatPassword": "password123"
    }

    response = requests.put(
        url=f"{BASE_URL}/admin/users/{user_id}/change-password",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert response.status_code == 400

def test_delete_user(token):
    user_id = 2

    response = requests.delete(
        url=f"{BASE_URL}/admin/users/{user_id}",
        headers=auth_header(token),
        timeout=5
    )

    assert response.status_code in [200, 204]

def test_restore_user(token):
    user_id = 2

    response = requests.put(
        url=f"{BASE_URL}/admin/users/{user_id}/restore",
        headers=auth_header(token),
        timeout=5
    )

    assert response.status_code in [200, 204]
