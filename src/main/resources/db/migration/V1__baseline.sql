-- Flyway baseline placeholder.
--
-- The existing dev/prod database already has the tables Hibernate auto-created
-- before Flyway was adopted (therapists, training_programs, testimonials, faqs,
-- contact_submissions, booking_requests, space_enquiries, oauth_tokens).
--
-- `spring.flyway.baseline-on-migrate=true` + `baseline-version=1` tells Flyway to
-- treat this V1 as the baseline and skip executing it. All real schema changes
-- live in V2 and later.
SELECT 'baseline' AS flyway_baseline;
