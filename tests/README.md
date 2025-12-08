
# teletext-backend

Testy aplikacji Teletext.

---

## Dokumentacja testów

Wstępnie są tu zmiany które należy wprowadzić, aby móc uruchomić te testy.
W pliku  `requirements.txt` znajdują się zależności potrzebne do testów.
- pytest
- requests
---
Internet mi podpowiada, że trzeba dodać osobny plik 
`docker-files/python-tests.Dockerfile` z zawartością: 

```
FROM python:3.11-slim

WORKDIR /app

COPY tests/python/ /app
COPY .env.example /app/.env

RUN pip install --no-cache-dir -r requirements.txt

CMD ["pytest"]
```
docker-compose.yml rozszerzyć o:

```
services:
  python-tests:
    build:
      context: .
      dockerfile: docker-files/python-tests.Dockerfile
    env_file:
      - .env
    depends_on:
      - java-backend
```
a do Makefile:
```
test-python:
    docker compose run --rm python-tests
```
## Uruchamianie testów w Pythonie

```
make test-python
```
