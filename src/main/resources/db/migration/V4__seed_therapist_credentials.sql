-- Phase 1.5 — seed credential lines for existing therapists.
-- Idempotent: ALTER above set the column nullable; this UPDATE only touches NULL rows.
UPDATE therapists
SET credentials_line = 'Army veteran · Integrative therapist'
WHERE LOWER(name) LIKE '%upma%' AND credentials_line IS NULL;

UPDATE therapists
SET credentials_line = 'IIT alum · Regression specialist'
WHERE LOWER(name) LIKE '%amitanshu%' AND credentials_line IS NULL;
