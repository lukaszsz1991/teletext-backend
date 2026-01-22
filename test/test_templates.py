import pytest
import requests
import uuid

BASE_URL = 'http://localhost:8080/api/admin'
LOGIN_URL = f'{BASE_URL}/auth/login'

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

def test_get_all_templates(token):
    response = requests.get(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200
    assert isinstance(response.json(), list)


@pytest.mark.parametrize("category", ["sports", "finance", "weather"])
def test_get_templates_by_category(token, category):
    params = {'category': category, 'includeInactive': 'false'}
    response = requests.get(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        params=params,
        timeout=5
    )
    assert response.status_code == 200
    for template in response.json():
        assert template["category"].lower() == category.lower()

def test_get_template_by_id(token):
    template_id = 2
    response = requests.get(
        url=f"{BASE_URL}/templates/{template_id}",
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200
    data = response.json()
    assert data["id"] == template_id

def test_create_template(token):
    unique_suffix = str(uuid.uuid4())[:8]
    payload = {
        "name": f"TEST_TEMPLATE_{unique_suffix}",
        "source": "sport_table",
        "category": "sports",
        "configJson": {"league": "bundesliga"}
    }

    create_resp = requests.post(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert create_resp.status_code == 201

@pytest.mark.parametrize("payload", [
    {"name": "", "source": "sport_table", "category": "sports", "configJson": {}},
    {"name": "Test", "source": "", "category": "sports", "configJson": {}},
    {"name": "Test", "source": "sport_table", "category": "", "configJson": {}},
])
def test_create_template_invalid(token, payload):
    response = requests.post(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        json=payload
    )
    assert response.status_code in [400, 500]

def test_create_template_invalid_types(token):
    payload = {
        "name": 123,
        "source": True,
        "category": None,
        "configJson": "bundesliga"
    }
    response = requests.post(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        json=payload
    )
    assert response.status_code in [400, 422]

def test_create_duplicate_template(token):
    payload = {
        "name": "Wynikii Ekstraklasa",
        "source": "sport_table",
        "category": "sports",
        "configJson": {"league": "bundesliga"}
    }
    first = requests.post(f"{BASE_URL}/templates", headers=auth_header(token), json=payload)
    second = requests.post(f"{BASE_URL}/templates", headers=auth_header(token), json=payload)
    assert second.status_code == 409


def test_update_template(token):
    payload = {
        "category": "sports",
        "name": "Wyniki nowy Ekstraklasa",
        "source": "sport-matches",
        "configJson": {"week": 48, "league": "ekstraklasa"}
    }
    response = requests.put(
        f"{BASE_URL}/templates/4",
        headers=auth_header(token),
        json=payload
    )
    assert response.status_code == 200
    assert response.json()["name"] == payload["name"]

def test_deactivate_template(token):
    response = requests.delete(
        f"{BASE_URL}/templates/6",
        headers=auth_header(token)
    )
    assert response.status_code == 204

def test_activate_template(token):
    response = requests.patch(
        f"{BASE_URL}/templates/6/activate",
        headers=auth_header(token),
        json={}
    )
    assert response.status_code in [200, 204]

def test_activate_nonexistent_template(token):
    response = requests.patch(
        f"{BASE_URL}/templates/9999/activate",
        headers=auth_header(token),
        json={}
    )
    assert response.status_code == 404

def test_deactivate_nonexistent_template(token):
    response = requests.delete(
        f"{BASE_URL}/templates/9999",
        headers=auth_header(token)
    )
    assert response.status_code == 404
