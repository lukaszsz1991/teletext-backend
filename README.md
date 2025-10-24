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

---

## Autorzy
- [Sebastian Górski](https://github.com/sgorski00/)
- [Jakub Grzymisławski](https://github.com/jgrzymislawski/)
- [Łukasz Szenkiel](https://github.com/lukaszsz1991/)
- [Rafał Wilczewski](https://github.com/Rafal-wq/)

---

> Projekt wykonywany w ramach kursu *Projektowanie i programowanie systemów internetowych II*