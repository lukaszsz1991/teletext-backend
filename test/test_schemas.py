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

def test_get_all_schemas(token):
    response = requests.get(
        f"{BASE_URL}/schemas",
        headers=auth_header(token),
        timeout=5
    )
    assert response.status_code == 200

    schemas = response.json()
    assert isinstance(schemas, list)
    for schema in schemas:
        assert isinstance(schema, dict)
        assert "source" in schema
        assert "required" in schema
        assert "optional" in schema
        assert "types" in schema
        assert isinstance(schema["required"], list)
        assert isinstance(schema["optional"], list)
        assert isinstance(schema["types"], dict)

        valid_prefixes = ["String", "Integer", "Boolean"]
        for field in schema["required"] + schema["optional"]:
            assert field in schema["types"]
            field_type = schema["types"][field]
            assert any(field_type.startswith(prefix) for prefix in valid_prefixes)

def test_schema_for_weather(token):
    response = requests.get(f"{BASE_URL}/schemas", headers=auth_header(token))
    assert response.status_code == 200

    schemas = response.json()
    schema = next(s for s in schemas if s["source"] == "weather")

    assert "city" in schema["required"]
    assert schema["types"]["city"].startswith("String")

def test_schema_for_unknown_source(token):
    response = requests.get(f"{BASE_URL}/schemas/unknown-source", headers=auth_header(token))
    assert response.status_code == 400

def test_all_schemas_requires_authentication():
    response = requests.get(f"{BASE_URL}/schemas")
    assert response.status_code == 401



