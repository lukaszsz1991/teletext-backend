import pytest
import requests

BASE_URL = "http://localhost:8080"


# 1. Zmieniamy get_token na fixture
@pytest.fixture
def token():
    payload = {
        "username": "admin",
        "password": "admin"
    }

    response = requests.post(
        url=f"{BASE_URL}/api/admin/auth/login",
        json=payload
    )

    assert response.status_code == 200
    return response.json()["token"]


# Przekazujemy 'token' jako argument do testu - pytest sam go wstrzyknie
def test_get_pages_by_category(token):
    headers = {
        "Authorization": f"Bearer {token}"
    }

    params = {
        "category": "SPORTS"
    }

    response = requests.get(
        url=f"{BASE_URL}/api/admin/pages",
        headers=headers,
        params=params
    )

    # 1. Status code
    assert response.status_code == 200

    data = response.json()

    # 2. Czy odpowiedź jest listą
    assert isinstance(data, list)

    # 3. ZABEZPIECZENIE: Sprawdź czy lista nie jest pusta
    assert len(data) > 0, "Lista zwrócona przez API jest pusta - nie można zweryfikować kategorii!"

    # 4. POPRAWKA LOGICZNA: Wchodzimy w głąb obiektu category
    for page in data:
        # Zakładam, że sprawdzamy pole 'originalName' z poprzedniego screena
        # Jeśli API zwraca po prostu nazwę, upewnij się jak wygląda JSON
        actual_category = page["category"]["originalName"]
        assert actual_category == "SPORTS", f"Oczekiwano SPORTS, otrzymano {actual_category}"