-- Phase 1.5 fix G3 — widen status + booking_source columns.
--
-- The old enum (NEW/READ/REPLIED) had max length 7, so Hibernate created
-- booking_requests.status as VARCHAR(7). The new enum adds CONFIRMED (9) and
-- CANCELLED (9), which truncate on insert. Widen to VARCHAR(20) to fit any
-- future enum addition.
--
-- booking_source was added by Hibernate when the new field landed; it should
-- already be a VARCHAR(20) but we set it explicitly here to be defensive.

ALTER TABLE booking_requests
    MODIFY COLUMN status VARCHAR(20) NOT NULL;

ALTER TABLE booking_requests
    MODIFY COLUMN booking_source VARCHAR(20) NOT NULL DEFAULT 'PUBLIC_FORM';
