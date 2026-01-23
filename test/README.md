
# 🚀 teletext-backend

️Testy aplikacji Teletext.

---

## ⚙ Dokumentacja testów

W pliku  `requirements.txt` znajdują się zależności potrzebne do testów.
- pytest
- requests
```
pip install -r requirements.txt
```

---
Ustawienie zmiennej JWT 

```
$env:TELETEXT_JWT_SECRET="K9vR4d3Zx+5K0Yx7C1nR2r9Qk6yMZc8E0sJX4p0m5uE="
```
Uruchomienie backendu:

```
.\mvnw.cmd clean spring-boot:run -Plocal-dev
```
## 🎯 Uruchamianie testów w Pythonie

```
pytest ./test
```

## 🧪 Opis plików testowych

Projekt zawiera testy, podzielone według obszarów funkcjonalnych backendu:

| Plik testowy             | Zakres testów                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------|
| `test_page_stats.py`     | Testy statystyk stron – np. liczba odwiedzin.                                              |
| `test_pages.py`          | Testy logiki stron: tworzenie, edycja, usuwanie, aktywacja, obsługa błędów (`404`, `400`). |
| `test_schemas.py`        | Walidacja danych wejściowych zgodnie ze schematami.                                        |
| `test_templates.py`      | Testy szablonów – poprawność renderowania, dostępność, integracja z danymi.                |
| `test_user.py`           | Testy użytkowników: dodawanie, usuwanie, zmiana hasła, edycja użytkownika.                 |

> Każdy plik testowy odpowiada konkretnej warstwie logiki aplikacji.
