# Phase 2 — Clean-architecture rebuild branch

> **Branch:** `phase1-rebuild-clean`
> **Base:** `jsp-rebuild`
> **Status:** Architecture rebuild in progress. Functionality matches `jsp-rebuild`.

## What's different from `jsp-rebuild`

This branch ships the same features (receptionist panel, slot scheduler,
Google Calendar push) on cleaner architectural bones:

- **Vertical-slice packages** — features grouped by bounded context
  (`enquiry/`, `booking/`, `scheduling/`, `calendar/`, `notification/`,
  `content/`, `auth/`) instead of layer-by-type.
- **`in.thehealingpresence`** base package (matches `CLAUDE.md` convention).
- **Persistence-ignorant domain** — `BookingRequest` JPA entity preserved as
  the persistence detail. Services depend on `Enquiry` / `Booking` domain
  records via `EnquiryMapper` / `BookingMapper`.
- **`CalendarPort`** interface decouples `BookingNotificationListener` from
  the Google Calendar adapter.
- **Typed `@ConfigurationProperties`** beans for Google Calendar,
  notifications, LDAP — replaces 10 scattered `@Value` injections.
- **`Result<T, E>`** sealed type for `SlotSchedulerService.tryBook` (was
  `IllegalStateException` for control flow).
- **`controller/pages/` vs `controller/api/`** split per slice.
- **`TherapyType`** enum (was free-text `String`).

## ⚠️ Flyway has been stripped

This branch boots with `spring.jpa.hibernate.ddl-auto=update` and seeds via
`src/main/resources/data.sql`. The `db/migration/` directory and the
`flyway-core` / `flyway-mysql` dependencies were removed.

**Before any production deploy from this branch:**

1. Re-add `flyway-core` and `flyway-mysql` to `pom.xml`.
2. Generate a `V1__baseline.sql` from the current MySQL schema
   (`mysqldump --no-data healingpresence | flyway-prepare > V1__baseline.sql`
   or equivalent).
3. Set `spring.flyway.enabled=true` and `spring.flyway.baseline-on-migrate=true`
   in `application-prod.yml`.
4. Switch `spring.jpa.hibernate.ddl-auto` from `update` → `validate` so
   Flyway owns schema and Hibernate just verifies it.
5. Migrate `data.sql` seed inserts back to `V2__seed_reference_data.sql`.

The `jsp-rebuild` branch already has Flyway wired correctly (V1–V5); use it
as the reference when restoring.

## Running locally

```
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

The dev DB (`healingpresence` on local MySQL) is shared with `jsp-rebuild`.
Hibernate `update` is additive — switching between branches should not
require dropping the DB. If you see schema drift errors, drop and recreate:

```
mysql -u root -proot -e "DROP DATABASE healingpresence; CREATE DATABASE healingpresence;"
```

Then re-boot — Hibernate recreates tables and `data.sql` seeds rows.
