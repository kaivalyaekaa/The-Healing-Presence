# The Healing Presence

A modern website for a holistic therapy and training centre, built with Spring Boot 3.3 + JSP + MySQL + LDAP.

## Tech Stack

| Layer        | Technology                                                                          |
|--------------|-------------------------------------------------------------------------------------|
| Backend      | Spring Boot 3.3, Java 21 (Temurin)                                                  |
| Views        | JSP 3.0 + JSTL 3.0 + JSP tag files (`/WEB-INF/tags/`)                               |
| Styling      | Bootstrap 5.3 (CDN) + Bootstrap Icons + custom `thp.css`                            |
| Client JS    | Vanilla ES2020 `fetch` AJAX + jQuery 3.7 (CDN). No HTMX, no Tailwind, no Alpine.    |
| Database     | MySQL 8 (utf8mb4, IST timezone)                                                     |
| ORM          | Spring Data JPA / Hibernate 6                                                       |
| Auth         | Spring Security 6 + LDAP (embedded UnboundID for dev, real LDAP/AD for prod)        |
| Email        | Spring `JavaMailSender` (SMTP), dispatched via `@TransactionalEventListener`        |
| Build        | Maven 3.9 (wrapper included). WAR packaging.                                        |

## Prerequisites

- **Java 21** (Temurin recommended). The Maven wrapper invokes `JAVA_HOME\bin\java.exe`, so make sure `JAVA_HOME` points at JDK 21.
- **MySQL 8** running on `localhost:3306` (port can be overridden via `DB_URL`).
- **Maven 3.9+** (wrapper included).

If your `JAVA_HOME` points to a different JDK, use the helper batch file [build-with-jdk21.cmd](build-with-jdk21.cmd) which sets `JAVA_HOME=C:\Program Files\Java\jdk-21` for the duration of the wrapper invocation.

## Running locally

```cmd
.\build-with-jdk21.cmd spring-boot:run
```

The DB `healingpresence` is auto-created on first connect (`createDatabaseIfNotExist=true`).
The app starts on http://localhost:8080.

### Default dev credentials

- **MySQL**: `root` / `root` (override via `DB_PASS` env var)
- **LDAP** (embedded UnboundID, dev profile only):
  - `admin` / `admin123`  &mdash; ROLE_ADMIN, ROLE_STAFF
  - `therapist` / `therapist123` &mdash; ROLE_STAFF

The login page shows these credentials only when the `dev` profile is active.

## Environment variables

| Variable          | Purpose                                  | Default                                             |
|-------------------|------------------------------------------|-----------------------------------------------------|
| `DB_URL`          | JDBC URL                                 | `jdbc:mysql://localhost:3306/healingpresence?...`   |
| `DB_USER`         | DB username                              | `root` (dev), `thp_app` (prod)                      |
| `DB_PASS`         | DB password                              | `root` (dev)                                        |
| `MAIL_HOST`       | SMTP server                              | `smtp.gmail.com`                                    |
| `MAIL_PORT`       | SMTP port                                | `587`                                               |
| `MAIL_USER`       | SMTP user                                |                                                     |
| `MAIL_PASS`       | SMTP password                            |                                                     |
| `APP_NOTIFY_TO`   | Where contact-form notifications go      | `info@thehealingpresence.in`                        |
| `APP_NOTIFY_FROM` | "From" address on outbound mails         | `noreply@thehealingpresence.in`                     |

## Image assets

Public images live in `src/main/resources/static/images/`. The home page references:

| File                       | Where it shows                                |
|----------------------------|-----------------------------------------------|
| `logo.png`                 | Navbar + footer wordmark (color-inverted)     |
| `home-hero.jpg`            | Why-Choose arched photo (reception)           |
| `team-photo.jpg`           | Holistic Healing Therapy big card             |
| `therapist-portrait.jpg`   | Certified Expert-Led Training big card        |
| `rent-space.jpg`           | Peaceful Tranquil Space card                  |
| `merchandise.jpg`          | Hand-Picked Merchandise card                  |
| `founders.jpg`             | "Your Partner In Holistic Healing" hero photo |
| `about-space.jpg`          | About page side image                         |

If a file is missing the page shows a gold placeholder with the `data-fallback` text.

## Architecture

- `controller/` &mdash; page controllers and form-POST handlers (classic + AJAX)
- `service/` &mdash; business logic. `ContentService` is `@Cacheable`. `*Service.save()` publishes a domain event after persist.
- `event/` &mdash; form-submitted events + `EmailNotificationListener` (fires after transaction commit, so emails never go on rollback)
- `domain/` &mdash; JPA entities with `@CreationTimestamp`
- `repository/` &mdash; Spring Data JPA interfaces
- `dto/` &mdash; form-backing beans with Bean Validation constraints
- `config/` &mdash; `SecurityConfig` (LDAP + headers), `WebConfig` (resource handlers), `MailConfig`
- `advice/GlobalModelAttributes` &mdash; exposes `currentUri`, `activeProfile`, `isDevProfile` to every JSP

## Building a WAR

```cmd
.\build-with-jdk21.cmd -DskipTests package
```

Produces `target/healing-presence.war` (executable, embedded Tomcat) plus the unrepackaged `*.war.original` for deploying inside a standalone Tomcat 10.1.

## Running tests

```cmd
.\build-with-jdk21.cmd test
```

Tests use H2 in-memory and a `TestSecurityConfig` that mocks LDAP. Service tests verify the after-commit email dispatch path with a mocked `EmailService`.

## Production deploy

Set:
- `SPRING_PROFILES_ACTIVE=prod`
- `DB_URL`, `DB_USER`, `DB_PASS` for the real MySQL
- `MAIL_*` for the real SMTP
- LDAP environment: replace embedded LDAP with `spring.ldap.urls=...` pointing at your corporate LDAP/AD

The `users.ldif` file in `src/main/resources/ldap/` is **dev only**. Never deploy it.
