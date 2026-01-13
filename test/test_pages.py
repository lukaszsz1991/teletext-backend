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

    # category
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



