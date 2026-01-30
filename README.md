# Accounts Server

Serwis backendowy do zarządzania kontami (accounts) udostępniający REST API. Projekt jest przygotowany do pracy lokalnie oraz w środowisku kontenerowym (Docker Compose) i zawiera gotową kolekcję Postman do ręcznego testowania endpointów.

## Najważniejsze cechy

- REST API do operacji na kontach (typowo: tworzenie, odczyt, aktualizacja, usuwanie)
- Warstwa persystencji oparta o **Spring Data JPA**
- Warstwa web oparta o **Spring MVC**
- **Java 21**
- Konfiguracja uruchomieniowa przez **Gradle**
- Środowisko developerskie przez **Docker Compose**
- Kolekcja testowa: `Accounts-Server.postman_collection.json`

## Wymagania

- **JDK 21**
- **Docker + Docker Compose** (opcjonalnie, jeśli uruchamiasz zależności w kontenerach)
- (opcjonalnie) **Postman** do testowania API

## Szybki start (lokalnie)

1. Zbuduj projekt:
   ```bash
   ./gradlew clean build
   ```

2. Uruchom aplikację:
   ```bash
   ./gradlew bootRun
   ```

3. Aplikacja powinna wystartować lokalnie (host/port zależą od konfiguracji).

> Jeśli w projekcie są profile/zmienne środowiskowe (np. dla bazy danych), ustaw je przed uruchomieniem.

## Szybki start (Docker Compose)

1. Uruchom zależności (np. bazę danych) przez Compose:
   ```bash
   docker compose up -d
   ```

2. Uruchom aplikację lokalnie lub dodaj do Compose (zależnie od tego, jak masz to skonfigurowane).

3. Zatrzymanie:
   ```bash
   docker compose down
   ```

## Testowanie API (Postman)

W repozytorium znajduje się kolekcja:
- `Accounts-Server.postman_collection.json`

Instrukcja:
1. Zaimportuj plik do Postmana
2. Ustaw zmienne środowiskowe (np. `baseUrl`) zgodnie z tym, gdzie działa aplikacja
3. Odpal requesty z kolekcji

## Konfiguracja

Typowe elementy konfiguracyjne (zależnie od tego, jak projekt jest ustawiony):
- URL/host/port bazy danych
- Dane dostępowe (user/hasło)
- Port aplikacji
- Profile środowiskowe (dev/test/prod)

Jeśli chcesz, mogę dopisać tę sekcję konkretnie pod Twoje wartości (podaj proszę używany port aplikacji i typ bazy z `docker-compose.yml`).

## Struktura projektu (wysoki poziom)

- `src/` – kod źródłowy aplikacji
- `build.gradle` / `settings.gradle` – konfiguracja Gradle
- `docker-compose.yml` – uruchamianie usług pomocniczych w Dockerze
- `Accounts-Server.postman_collection.json` – testy manualne API
- `LICENSE` – licencja projektu

## Rozwój

Przydatne komendy:
- Testy:
  ```bash
  ./gradlew test
  ```
- Sprawdzenie builda:
  ```bash
  ./gradlew clean build
  ```

## Licencja

Zobacz plik `LICENSE`.