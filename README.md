![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

# teletext-backend

Backend aplikacji Teletext.

---

## Dokumentacja API

Po uruchomieniu aplikacji dokumentacja API jest dostępna pod adresem:

```
http://<host>:<port>/swagger-ui.html
```

Dokumentacja jest podzielona na grupy, które można zmienić w prawym górnym rogu ekranu w sekcji **Select a definition**.
Dostępne grupy to:
- `public`
- `admin`

W celu wykonywania zapytań try-it-out do zasobów chronionych, należy podać token JWT w okienku po naciśnięciu przycisku `Authorize`.

---

## Uwierzytelnianie i autoryzacja
System uwierzytelniania oparty jest na JWT (JSON Web Tokens). Dostęp do zasobów `/api/public/**` jest otwarty dla wszystkich użytkowników, natomiast dostęp do zasobów `/api/admin/**` wymaga posiadania ważnego tokenu JWT.

Token można uzyskać jedynie przez zalogowanie się na konto z rolą **ADMIN** przy użyciu endpointu 
```
POST /api/admin/auth/login
``` 
Po pomyślnym zalogowaniu, serwer zwraca token JWT, który należy przesłać w nagłówku: 
```
Authorization: Bearer <token>
```

Token ma określony czas ważności, po którym użytkownik musi ponownie się zalogować, aby uzyskać nowy token.
Czas ważności ustawiony jest w pliku `application.properties (w odpowiednim profilu)` pod kluczem `jwt.expiration-ms`.

### :exclamation: JWT Secret
Przed uruchomieniem aplikacji należy ustawić zmienną środowiskową **`TELETEXT_JWT_SECRET`** podając w niej co najmniej 32 bajtowy ciąg znaków zakodowany w Base64.

#### Generowanie secretu:
**Linux / macOS (bash):**
```bash
openssl rand -base64 32
```

**Windows (PowerShell):**
```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object {Get-Random -Maximum 256}))
```
### Role i konta
> Aktualnie jedyną rolą jest `ADMIN`

Aplikacja **nie posiada mechanizmu rejestracji użytkowników**, ponieważ z założenia telegazeta ma być publiczna.
Nowe konta administratorów tworzone są przez istniejących adminsitratorów.

> :exclamation: Przy pierwszym uruchomieniu aplikacji należy zmienić domyślne hasło administratora (domyślnie `admin`).

## Integracje z zewnętrznymi serwisami

Aplikacja integruje się z siedmioma zewnętrznymi API dostarczającymi dane do wyświetlenia w telegazecie:
- [Narodowy Bank Polski](https://api.nbp.pl/) - kursy walut
- [OpenMeteo](https://api.open-meteo.com/) - dane pogodowe
- [Lotto](https://developers.lotto.pl/) - dane losowań lotto
- [News Data](https://newsdata.io/) - wiadomości
- [Jooble](https://jooble.org/) - oferty pracy
- [Mój codzienny horoskop](https://www.moj-codzienny-horoskop.com/) - horoskop
- [Highlightly](https://sports.highlightly.net/) - dane piłkarskie

Klienci webowi i DTO znajdują się w folderze `src/main/java/pl/studia/teletext/teletext_backend/clients`.

Wszystkie dane połączeniowe są zdefiniowane w pliku `application.properties`, secrety (api keys) przekazywane są przez zmienne środowiskowe.

Dane z integracji są odpowiednio mapowane na DTO `ExternalDataResponse`, dzięki czemu frontend może w łatwy sposób wyświetlić je na stronach telegazety.

Szczegółowe informacje na temat integracji znajdują się w dokumentacji integracji: [Dokumentacja](https://github.com/collegiumwitelona/2025-inf-wdzpd-lab-all-telegazeta/new/main/zaliczenie/docs/integrations/README.md)

---

## Informacje dla współtwórców

### Formatowanie kodu

Projekt korzysta z narzędzia `Spotless` do automatycznego formatowania kodu źródłowego. Przed zatwierdzeniem zmian do systemu kontroli wersji, należy uruchomić polecenie Maven:

```bash
mvn spotless:apply
```

dzięki któremu wszystkie pliki zostaną sformatowane zgodnie z ustalonym stylem kodowania.

Aby utrzymać jednolity format plików w repozytorium, przyjęty został styl kodowania oparty na [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

Jeśli chcesz jedynie sprawdzić format kodu bez automatycznego formatowania, możesz użyć polecenia:

```bash
mvn spotless:check
```

---

## Autorzy
- [Sebastian Górski](https://github.com/sgorski00/)
- [Jakub Grzymisławski](https://github.com/jgrzymislawski/)
- [Łukasz Szenkiel](https://github.com/lukaszsz1991/)
- [Rafał Wilczewski](https://github.com/Rafal-wq/)

---

> Projekt wykonywany w ramach kursu *Projektowanie i programowanie systemów internetowych II*