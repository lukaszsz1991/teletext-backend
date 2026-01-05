import pytest
import requests

BASE_URL = 'http://localhost:8080/api/admin'
LOGIN_URL = f'{BASE_URL}/auth/login'  # Upewnij się, że to właściwy endpoint logowania

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
    print("Szablony:", response.json())


@pytest.mark.parametrize("category", ["sports", "tv", "weather"])
def test_get_templates_by_category(token, category):
    params = {"category": category}
    response = requests.get(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        params=params,
        timeout=5
    )
    assert response.status_code == 200
    for template in response.json():
        assert template["category"].lower() == category.lower()
    print(f"Szablony w kategorii '{category}': {[t['name'] for t in response.json()]}")

def test_create_template(token):
    payload = {
        "name": "Wyniki turecka101 liga",
        "source": "sport_table",
        "category": "sports",
        "configJson": {"league": "bundesliga"}
    }
    response = requests.post(
        f"{BASE_URL}/templates",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )
    assert response.status_code == 201
    assert response.json()["name"] == payload["name"]

#test walidacji
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

def test_update_template(token):
    payload = {
        "category": "sports",
        "name": "Wynikii Ekstraklasa",
        "source": "sport-matches",
        "configJson": {"week": 50, "league": "ekstraklasa"}
    }
    response = requests.put(
        f"{BASE_URL}/templates/18",
        headers=auth_header(token),
        json=payload
    )
    assert response.status_code == 200
    assert response.json()["name"] == payload["name"]

