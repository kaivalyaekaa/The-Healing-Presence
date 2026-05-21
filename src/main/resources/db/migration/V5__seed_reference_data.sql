-- Phase 1.6 — Port seed reference data from the legacy `data.sql` into a Flyway
-- migration. Idempotent via `WHERE NOT EXISTS` on a natural key (no unique
-- constraints exist, so `INSERT IGNORE` would still create duplicates):
--   * therapists           keyed by `name`
--   * training_programs    keyed by `code`
--   * testimonials         keyed by `client_name`
--   * faqs                 keyed by `question`
--
-- Safe on:
--   * fresh DBs                — V1→V5 run from scratch, 17 rows seeded.
--   * existing dev DBs         — rows already loaded by the old `data.sql` are
--                                 skipped by the NOT EXISTS guard.

-- ============================================================
-- Therapists (2 rows)
-- ============================================================
INSERT INTO therapists (name, title, bio, photo_path, display_order, created_at)
SELECT
  'Maj. Upma Pant (Retd.)',
  'Integrative Therapist',
  'An Army veteran and integrative therapist, helping people navigate through the challenges of life. My decade in the Indian army taught me camaraderie. With the same spirit, I now work in the therapeutic space - helping you navigate the challenges of life.',
  '/images/therapist-upma.jpg',
  1,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM therapists WHERE name = 'Maj. Upma Pant (Retd.)');

INSERT INTO therapists (name, title, bio, photo_path, display_order, created_at)
SELECT
  'Amitanshu Nath',
  'Holistic Healer',
  'An IITian turned healer, my journey moved from corporate boardrooms to therapy rooms. After years in HR and entrepreneurship, I found purpose in holistic healing. At The Healing Presence, I offer a safe, integrative space using regression therapy, hypnotherapy, Redikall Healing, and more.',
  '/images/therapist-amitanshu.jpg',
  2,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM therapists WHERE name = 'Amitanshu Nath');

-- ============================================================
-- Training Programs (4 rows)
-- ============================================================
INSERT INTO training_programs (code, title, description, slug, video_url, thumbnail_path, display_order, created_at)
SELECT
  '01',
  'Ekaa Clinical Hypnotherapy',
  'Become an EKAA-certified hypnotherapist through a comprehensive training program that covers the principles and techniques of clinical hypnotherapy, empowering you to facilitate deep healing and transformation in others.',
  'ekaa-clinical-hypnotherapy',
  NULL,
  NULL,
  1,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM training_programs WHERE code = '01');

INSERT INTO training_programs (code, title, description, slug, video_url, thumbnail_path, display_order, created_at)
SELECT
  '02',
  'Ekaa Decode Yourself',
  'Deepen your understanding of human behaviour and psychology with the EKAA Decode training. Explore personality frameworks and techniques to decode the complexities of the human mind, enhancing your ability to support personal growth and self-awareness.',
  'ekaa-decode-yourself',
  NULL,
  NULL,
  2,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM training_programs WHERE code = '02');

INSERT INTO training_programs (code, title, description, slug, video_url, thumbnail_path, display_order, created_at)
SELECT
  '03',
  'TASSO',
  'Journey into past lives and explore the depths of the soul with TASSO regression therapy training. Learn techniques to guide individuals through transpersonal experiences, helping them heal unresolved issues and gain profound insights.',
  'tasso',
  NULL,
  NULL,
  3,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM training_programs WHERE code = '03');

INSERT INTO training_programs (code, title, description, slug, video_url, thumbnail_path, display_order, created_at)
SELECT
  '04',
  'Crystal Healing Training',
  'Deepen your knowledge of crystals and their healing properties through crystal healing training. Explore the diverse world of crystals, learning techniques to harness their energy for personal and professional healing practices.',
  'crystal-healing-training',
  NULL,
  NULL,
  4,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM training_programs WHERE code = '04');

-- ============================================================
-- Testimonials (6 rows)
-- ============================================================
INSERT INTO testimonials (client_name, body, rating, published, created_at)
SELECT
  'Abraham Rodrigues',
  'Doctor Amitanshu is a wonderful person and has helped a lot through the session. I can feel a big change in me, I went from level 1 to level 10 of feeling happy.',
  5,
  TRUE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM testimonials WHERE client_name = 'Abraham Rodrigues');

INSERT INTO testimonials (client_name, body, rating, published, created_at)
SELECT
  'Aayana Yoga',
  'I cannot express enough gratitude for the transformative experience I have had at The Healing Presence. From the moment I walked in, I felt peace. The practitioners are incredibly knowledgeable, compassionate, and dedicated to their craft.',
  5,
  TRUE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM testimonials WHERE client_name = 'Aayana Yoga');

INSERT INTO testimonials (client_name, body, rating, published, created_at)
SELECT
  'Karthik Murthy',
  'I have been going to The Healing Presence since June 2024. Upma and Amitanshu are excellent therapists. I have had life changing experiences here.',
  5,
  TRUE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM testimonials WHERE client_name = 'Karthik Murthy');

INSERT INTO testimonials (client_name, body, rating, published, created_at)
SELECT
  'Ananya Mishra',
  'The Healing Presence is more than just a facility — it is a sanctuary of serenity, thoughtfully designed to inspire calm, reflection, and rejuvenation.',
  5,
  TRUE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM testimonials WHERE client_name = 'Ananya Mishra');

INSERT INTO testimonials (client_name, body, rating, published, created_at)
SELECT
  'Ajay Resika',
  'Had one of the most amazing sessions at The Healing Presence. Amitanshu was brilliant and I look forward to being connected to them.',
  5,
  TRUE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM testimonials WHERE client_name = 'Ajay Resika');

INSERT INTO testimonials (client_name, body, rating, published, created_at)
SELECT
  'Deepali Ninchani',
  'The Healing Presence truly lives up to its name in every sense. From the moment you step into their peaceful sanctuary, you feel enveloped in calm and care.',
  5,
  TRUE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM testimonials WHERE client_name = 'Deepali Ninchani');

-- ============================================================
-- FAQs (5 rows, therapy category)
-- ============================================================
INSERT INTO faqs (question, answer, display_order, category, created_at)
SELECT
  'Is Hypnotherapy like the mind control we see in movies?',
  'No, it''s not mind control. Hypnotherapy is a collaborative process where you''re in control at all times. It''s about accessing your own subconscious to bring about positive change.',
  1,
  'therapy',
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM faqs WHERE question = 'Is Hypnotherapy like the mind control we see in movies?');

INSERT INTO faqs (question, answer, display_order, category, created_at)
SELECT
  'How many sessions will I need to see results?',
  'The number of sessions varies depending on the individual and the nature of the concern. Some clients experience significant improvement in three to five sessions, while deeper issues may require a longer commitment. Your therapist will discuss a personalised plan during your initial consultation.',
  2,
  'therapy',
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM faqs WHERE question = 'How many sessions will I need to see results?');

INSERT INTO faqs (question, answer, display_order, category, created_at)
SELECT
  'Can anyone be hypnotized, or is it only for certain people?',
  'Most people can be hypnotized to varying degrees. Hypnotherapy works best when you are open to the process and willing to engage. Your therapist will guide you through the experience at a pace that feels comfortable for you.',
  3,
  'therapy',
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM faqs WHERE question = 'Can anyone be hypnotized, or is it only for certain people?');

INSERT INTO faqs (question, answer, display_order, category, created_at)
SELECT
  'Are the changes permanent, or will I need ongoing sessions?',
  'For most concerns, the changes achieved through hypnotherapy are long-lasting. Some clients benefit from occasional follow-up sessions to reinforce the work, while others find a single course of treatment sufficient. Your therapist will recommend the approach that best supports your goals.',
  4,
  'therapy',
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM faqs WHERE question = 'Are the changes permanent, or will I need ongoing sessions?');

INSERT INTO faqs (question, answer, display_order, category, created_at)
SELECT
  'Is Hypnotherapy safe, and are there any side effects?',
  'Yes, hypnotherapy is a safe and well-established therapeutic practice when conducted by a trained professional. You remain in control throughout the session and cannot be made to do anything against your will. Side effects are rare and typically limited to a brief feeling of relaxation or grogginess immediately after a session.',
  5,
  'therapy',
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM faqs WHERE question = 'Is Hypnotherapy safe, and are there any side effects?');
