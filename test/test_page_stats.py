import pytest
import requests

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

def test_get_page_stats(token):
    page_number = 101
    include_details = True
    headers = {
        'Authorization': f'Bearer {token}',
        'Accept': 'application/json'
    }

    response = requests.get(
        url=f'{BASE_URL}/stats/pages/{page_number}',
        params={"includeDetails": str(include_details).lower()},
        headers=headers,
        timeout=5
    )

    data = response.json()
    assert response.status_code == 200
    assert 'pageNumber' in data
    assert 'views' in data

    print(f"Strona: {data['pageNumber']}, Odsłony: {data['views']}")

def test_page_stats_not_found(token):
    page_number = 7
    headers = {
        'Authorization': f'Bearer {token}',
        'Accept': 'application/json'
    }

    response = requests.get(
        url=f'{BASE_URL}/stats/pages/{page_number}',
        params={"includeDetails": "true"},
        headers=headers,
        timeout=5
    )

    assert response.status_code == 404 or response.status_code == 200
    print("Odpowiedź dla nieistniejącej strony:", response.json())

def test_page_stats_without_details(token):
    page_number = 101
    headers = {
        'Authorization': f'Bearer {token}',
        'Accept': 'application/json'
    }

    response = requests.get(
        url=f'{BASE_URL}/stats/pages/{page_number}',
        headers=headers,
        timeout=5
    )

    assert response.status_code == 200
    print("Odpowiedź bez includeDetails:", response.json())

def test_page_stats_unauthorized():
    page_number = 1
    headers = {
        'Accept': 'application/json'
    }

    response = requests.get(
        url=f'{BASE_URL}/stats/pages/{page_number}',
        headers=headers,
        timeout=5
    )

    assert response.status_code == 401
    print("Odpowiedź bez tokena:", response.json())

def test_page_stats_invalid_param(token):
    page_number = 101
    headers = {
        'Authorization': f'Bearer {token}',
        'Accept': 'application/json'
    }

    response = requests.get(
        url=f'{BASE_URL}/stats/pages/{page_number}',
        params={"includeDetails": "notaboolean"},
        headers=headers,
        timeout=5
    )

    assert response.status_code in [200, 400]

def test_all_pages_stats_with_details(token):
    response = requests.get(
        f"{BASE_URL}/stats/pages",
        params={"includeDetails": "true"},
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200
    stats = response.json()
    assert isinstance(stats, list)

    for stat in stats:
        assert "details" in stat and isinstance(stat["details"], list)
        for detail in stat["details"]:
            assert "id" in detail and isinstance(detail["id"], int)
            assert "openedAt" in detail and isinstance(detail["openedAt"], str)

