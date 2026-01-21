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

def test_get_all_active_pages(token):
    response = requests.get(
        f"{BASE_URL}/pages?includeInactive=false",
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200

    pages = response.json()
    assert isinstance(pages, list)

    for page in pages:
        assert isinstance(page, dict)
        assert "id" in page and isinstance(page["id"], int)
        assert "pageNumber" in page and isinstance(page["pageNumber"], int)
        assert "title" in page and isinstance(page["title"], str)
        assert "createdAt" in page and isinstance(page["createdAt"], str)
        assert "updatedAt" in page and isinstance(page["updatedAt"], str)

        assert "category" in page and isinstance(page["category"], dict)
        category = page["category"]
        assert "originalName" in category and isinstance(category["originalName"], str)
        assert "category" in category and isinstance(category["category"], str)
        assert "description" in category and isinstance(category["description"], str)
        assert "mainPage" in category and isinstance(category["mainPage"], int)
        assert "nextFreePage" in category
        assert category["nextFreePage"] is None or isinstance(category["nextFreePage"], int)

def test_get_all_pages_including_inactive(token):
    response = requests.get(
        f"{BASE_URL}/pages?includeInactive=true",
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200
    assert isinstance(response.json(), list)

def test_get_pages_without_token():
    response = requests.get(f"{BASE_URL}/pages?includeInactive=false")
    assert response.status_code in [401, 403]

def test_get_page_by_id(token):
    page_id = 1
    response = requests.get(
        f"{BASE_URL}/pages/{page_id}",
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200

    page = response.json()
    assert isinstance(page, dict)
    assert page["id"] == page_id
    assert page["type"] in ["MANUAL", "AUTO"]
    assert isinstance(page["pageNumber"], int)

    assert "category" in page and isinstance(page["category"], dict)
    cat = page["category"]
    assert isinstance(cat["originalName"], str)
    assert isinstance(cat["category"], str)
    assert isinstance(cat["description"], str)
    assert isinstance(cat["mainPage"], int)
    assert cat["nextFreePage"] is None or isinstance(cat["nextFreePage"], int)

    # content
    assert "content" in page and isinstance(page["content"], dict)
    content = page["content"]
    assert isinstance(content["source"], str)
    assert isinstance(content["title"], str)
    assert isinstance(content["description"], str)
    assert content["additionalData"] is None or isinstance(content["additionalData"], dict)
    assert isinstance(content["createdAt"], str)
    assert isinstance(content["updatedAt"], str)

    # timestamps
    assert isinstance(page["createdAt"], str)
    assert isinstance(page["updatedAt"], str)
    assert "deletedAt" in page
    assert page["deletedAt"] is None or isinstance(page["deletedAt"], str)

def test_create_manual_page_with_inactive_number(token):
    payload = {
        "type": "MANUAL",
        "pageNumber": 911,
        "category": "misc",
        "title": "Piata strona dodana ręcznie",
        "description": (
            "Test - dodaję stronę z numerem strony która jest"
        )
    }

    response = requests.post(
        url=f"{BASE_URL}/pages",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )
    assert response.status_code == 201
    assert "Location" in response.headers
    assert response.headers["Location"].startswith("/api/admin/pages/")

def test_delete_page_by_id(token):
    page_id = 2

    response = requests.delete(
        url=f"{BASE_URL}/pages/{page_id}",
        headers=auth_header(token),
        timeout=5
    )
    print(response.status_code, response.text)
    assert response.status_code == 204  # lub 200, zależnie od implementacji

def test_activate_page_by_id(token):
    page_id = 2  # ID strony, którą chcesz aktywować

    response = requests.patch(
        url=f"{BASE_URL}/pages/{page_id}/activate",
        headers=auth_header(token),
        timeout=5
    )

    assert response.status_code == 204

def test_activate_page_not_found(token):
    non_existing_id = 99999

    response = requests.patch(
        url=f"{BASE_URL}/pages/{non_existing_id}/activate",
        headers=auth_header(token),
        timeout=5
    )

    assert response.status_code == 404
def test_activate_page_already_active(token):
    page_id = 6  # strona aktywna

    second = requests.patch(
        url=f"{BASE_URL}/pages/{page_id}/activate",
        headers=auth_header(token),
        timeout=5
    )

    assert second.status_code == 404


@pytest.mark.parametrize("bad_payload", [
    {"type": "MANUAL", "pageNumber": "abc"},  # zły typ
    {"type": "UNKNOWN", "pageNumber": 999},   # nieznany typ
    {"type": "MANUAL", "pageNumber": 999, "title": ""},  # pusty tytuł
])
def test_create_page_invalid_data(token, bad_payload):
    response = requests.post(
        url=f"{BASE_URL}/pages",
        headers=auth_header(token),
        json=bad_payload,
        timeout=5
    )
    assert response.status_code == 400

def test_create_template_page_missing_template(token):
    payload = {
        "type": "TEMPLATE",
        "pageNumber": 410,
        "category": "tv",
        "templateId": 9999  # zakładamy, że nie istnieje
    }

    response = requests.post(
        url=f"{BASE_URL}/pages",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert response.status_code == 404

def test_update_manual_page(token):
    page_number = 2 # zakładamy, że strona istnieje

    payload = {
        "type": "MANUAL",
        "pageNumber": 901,
        "category": "misc",
        "title": "Druga strona dodana ręcznie - zedytowana v2.0",
        "description": "Wszem i wobec ogłaszam"
    }

    response = requests.put(
        url=f"{BASE_URL}/pages/{page_number}",
        headers=auth_header(token),
        json=payload,
        timeout=5
    )

    assert response.status_code == 200

