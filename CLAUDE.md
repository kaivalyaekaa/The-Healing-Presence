# The Healing Presence (THP) — Project Conventions

## Stack
- **Framework:** Spring Boot 3.3.x with WAR packaging (Spring MVC 6.1 + Tomcat 10.1)
- **Views:** JSP 3.0 + JSTL 3.0 + JSP tag files for layout
- **Styling:** Bootstrap 5.3 CDN + custom CSS (`thp.css`)
- **Client JS:** Vanilla ES2020 + jQuery 3.7 where needed
- **ORM:** Hibernate 6 / Jakarta Persistence via Spring Data JPA
- **Database:** MySQL 8.0 (InnoDB, utf8mb4)
- **Migrations:** Flyway 10
- **Auth:** Spring Security 6 + Google OAuth2
- **Build:** Maven 3.9 via Maven Wrapper (`mvnw.cmd`)
- **Java:** 21 (Temurin)

## Package Structure
Base package: `in.thehealingpresence`
- `config/` — Spring configuration classes
- `controller/` — `@Controller` for JSP pages
- `controller/api/` — `@RestController` for AJAX JSON endpoints
- `service/` — Business logic
- `domain/` — JPA entities and enums
- `repository/` — Spring Data JPA interfaces
- `dto/` — Form-backing beans and JSON DTOs
- `util/` — Shared helpers

## Conventions
- **Timezone:** All DB timestamps stored as `DATETIME` in IST (`Asia/Kolkata`). Display in IST.
- **Currency:** INR (Indian Rupees). Use `DECIMAL(10,2)` in MySQL, `BigDecimal` in Java.
- **JSP layout:** Use the tag file at `WEB-INF/tags/layout.tag` for page wrapper.
- **CSS:** Use CSS custom properties defined in `thp.css` for brand colors.
- **No Lombok** — write explicit getters/setters for clarity.
- **Flyway:** Migrations in `src/main/resources/db/migration/`. Never modify existing migrations.
- **Secrets:** Never commit API keys. Use `application-local.yml` (gitignored) or environment variables.

## Running Locally
```
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```
App starts on `http://localhost:8080`

## Database
```
mysql -u thp_app -p thp_dev
```
