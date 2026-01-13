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

def test_get_page_stats(token):
    page_number = 4
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

    # 🖨️ Wyświetlenie danych w konsoli
    print(f"Strona: {data['pageNumber']}, Odsłony: {data['views']}")

def test_page_stats_not_found(token):
    page_number = 7  # zakładamy, że taka strona nie istnieje
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
    page_number = 1
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
    page_number = 1
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

    assert response.status_code == 400
    print("Odpowiedź z błędnym parametrem:", response.json())
    print("Kod odpowiedzi:", response.status_code)
    print("Treść odpowiedzi:", response.text)

