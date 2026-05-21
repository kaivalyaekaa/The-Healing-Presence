-- Phase 1.5 Tier 2 schema additions.
ALTER TABLE therapists   ADD COLUMN credentials_line VARCHAR(120) NULL;
ALTER TABLE testimonials ADD COLUMN avatar_path VARCHAR(255) NULL;
