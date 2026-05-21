# Hosting The Healing Presence — free-tier options

A working app needs three things: a Java 21 runtime that's always-on, a MySQL database, and HTTPS. As of May 2026 these are the realistic options:

## Recommended for the client demo: **Render** ✅

**Why Render** — easiest "connect GitHub, push, get URL" experience for Spring Boot. No credit card needed for the free tier; auto HTTPS; auto-redeploy on every push to `main`.

**Caveats** — Render's free web service spins down after 15 min idle (cold-start adds 30-60s on the next request). Their free database tier is **PostgreSQL only**, so you have two sub-options:

| Sub-option | What changes | Effort |
|---|---|---|
| **A. Switch to PostgreSQL on Render** | Add `org.postgresql:postgresql` dependency; change `spring.jpa.database-platform` to `PostgreSQLDialect`; change connection URL. Hibernate `ddl-auto: update` handles the schema on first boot. | ~30 min |
| **B. Keep MySQL, host the DB on Aiven** | Aiven offers a 1-month MySQL free trial (then $19/mo). Render runs the Spring Boot WAR; Aiven runs MySQL. | ~20 min, DB costs after month 1 |

**Steps (Sub-option A — recommended):**
1. Sign up at https://render.com using GitHub.
2. **New > Web Service** → pick the `kaivalyaekaa/The-Healing-Presence` repo.
3. Runtime: Java 21. Build command: `./mvnw -DskipTests package`. Start command: `java -jar target/healing-presence.war`.
4. Add env vars: `ADMIN_USERNAME`, `ADMIN_PASSWORD`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `MAIL_USER`, `MAIL_PASS`, `APP_NOTIFY_TO`, `APP_NOTIFY_FROM`, `SPRING_PROFILES_ACTIVE=prod`.
5. **New > PostgreSQL** → free plan → attach to the web service (Render injects `DATABASE_URL`).
6. Push to `main` → first deploy completes in 5-10 min; you get `https://healing-presence.onrender.com` with TLS.

## Best long-term if you want MySQL + always-on: **Oracle Cloud Always Free** 🏆

24 GB RAM Ampere A1 VM, always-on, MySQL 8 included, free forever (no time limit). Requires credit-card verification at signup (no charge). End-to-end runbook below (originally `HOSTING.md` v1).

## Other free tiers (worth knowing)

| Provider | Free tier | Spring Boot fit | MySQL? |
|---|---|---|---|
| **Fly.io** | 3 small machines, 256 MB RAM each | Good (Dockerfile-based) | No — Fly Postgres only |
| **Koyeb** | 1 nano service free, 512 MB, no sleep | Good | No — external |
| **Northflank** | Hobby plan with 1 service | Good | Self-managed container |
| **AWS Free Tier** | t2.micro 12 months | Tight (1 GB RAM) | RDS MySQL 12 months free |
| **GCP Free Tier** | e2-micro always free | Tight (1 GB shared) | Cloud SQL is paid |
| **Heroku** | ❌ Free tier removed in 2022 | — | — |
| **Railway** | ❌ Removed free tier in 2023 | — | — |

## Bottom line

- **For the client demo this week**: deploy to **Render with PostgreSQL** (sub-option A). Fastest, no DNS hassle.
- **For the production cut-over**: migrate to **Oracle Cloud Always Free** with MySQL (instructions further down).

---

# Google Calendar integration setup

The `CalendarPort` adapter no-ops when `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` are unset (default in dev). To make `/admin/google-calendar/connect` actually work, do the one-time setup below.

## One-time setup (per environment)

1. **Create a Google Cloud project** at https://console.cloud.google.com/projectcreate (project name doesn't matter — only you see it).

2. **Enable the Google Calendar API**: APIs & Services > Library > "Google Calendar API" > Enable.

3. **Configure the OAuth consent screen**:
   - User type: **External** (so Upma's personal Gmail can authorise)
   - App name: `The Healing Presence`
   - User support email + developer contact: yours
   - Authorised domains: blank for dev; `thehealingpresence.in` for prod
   - Scopes: `https://www.googleapis.com/auth/calendar.events`
   - **Test users**: add Upma's Gmail address — only test users can complete the flow until you publish

4. **Create OAuth 2.0 credentials**:
   - APIs & Services > Credentials > Create credentials > **OAuth client ID**
   - Application type: **Web application**
   - Authorised redirect URIs:
     - `http://localhost:8080/admin/google-calendar/callback` (dev)
     - `https://<your-render-url>/admin/google-calendar/callback` (prod)
   - Click Create → copy the **Client ID** and **Client Secret**

5. **Set env vars and restart**:
   ```
   GOOGLE_CLIENT_ID=<paste>
   GOOGLE_CLIENT_SECRET=<paste>
   GOOGLE_CALENDAR_ID=primary
   GOOGLE_REDIRECT_URI=<same URL as registered>
   ```

6. **Complete the OAuth dance once**:
   - Sign in as `admin` → visit `/admin/google-calendar` → **Connect Google Calendar**
   - Google's consent screen → sign in as **Upma** (the test user from step 3) → grant scope
   - Browser returns to `/admin/google-calendar?connected=true`
   - Refresh token stored in `oauth_tokens`; every receptionist booking thereafter auto-pushes to Upma's calendar

## Troubleshooting

- **"Google hasn't verified this app"** during consent → expected; click **Advanced > Go to The Healing Presence (unsafe)**. Goes away once you publish the consent screen.
- **Redirect URI mismatch** → the URL must match step 4 **exactly** (scheme, host, port, path).
- **Token expired/revoked** → re-run step 6; the new refresh token overwrites `oauth_tokens`.
- **Wrong calendar** → set `GOOGLE_CALENDAR_ID` to Upma's specific calendar id (Calendar Settings > "Integrate calendar" > "Calendar ID").

---

# Deploying The Healing Presence to Oracle Cloud Always Free

A step-by-step runbook for putting this Spring Boot app on a free, always-on Oracle Cloud Ampere VM with HTTPS and MySQL. Total time: ~30 minutes first run; ~2 minutes per redeploy.

## 1. Sign up for Oracle Cloud (free tier)

1. https://signup.cloud.oracle.com → email + payment-card verification (no charge)
2. Choose **home region** carefully (Mumbai/Singapore/Hyderabad recommended for India latency)
3. Wait for tenancy provisioning (~10 minutes)

## 2. Create the Ampere A1 VM

1. Compute → Instances → Create
2. Image: **Canonical Ubuntu 22.04** (ARM build)
3. Shape: **VM.Standard.A1.Flex** with 4 OCPU + 24 GB RAM (Always Free)
4. SSH: paste your public key
5. Networking: leave defaults; the VCN's security list will need ingress rules 80/443/22 (Oracle auto-creates 22, add 80 + 443 yourself in the security list)
6. Create

## 3. Bootstrap the VM

```bash
ssh ubuntu@<vm-public-ip>
sudo apt update
curl -sL https://raw.githubusercontent.com/kaivalyaekaa/The-Healing-Presence/main/deploy/setup-oracle.sh | sudo bash
```

`setup-oracle.sh` installs JDK 21 (Temurin), MySQL 8, Caddy, creates the `healingpresence` DB + `thp_app` user, drops the systemd unit + Caddyfile, and opens firewall ports 22/80/443.

## 4. Set environment variables

```bash
sudo nano /etc/healing-presence.env
```

```
DB_URL=jdbc:mysql://localhost:3306/healingpresence?useSSL=false&serverTimezone=Asia/Kolkata
DB_USER=thp_app
DB_PASS=<from setup-oracle.sh stdout>
ADMIN_USERNAME=admin
ADMIN_PASSWORD=<strong-password>
MAIL_USER=info@thehealingpresence.in
MAIL_PASS=<gmail-app-password>
APP_NOTIFY_TO=info@thehealingpresence.in
APP_NOTIFY_FROM=noreply@thehealingpresence.in
GOOGLE_CLIENT_ID=<from Google Cloud Console>
GOOGLE_CLIENT_SECRET=<from Google Cloud Console>
GOOGLE_REDIRECT_URI=https://thehealingpresence.in/admin/google-calendar/callback
```

## 5. Point DNS at the VM

DNS provider for `thehealingpresence.in`:
- A record: `@` → `<vm-public-ip>`
- A record: `www` → `<vm-public-ip>`

Wait for propagation (5-60 min). Caddy auto-issues Let's Encrypt certs on first request to the domain.

## 6. Deploy

From your laptop:
```bash
bash deploy/redeploy.sh
```

Builds the WAR locally, scp's it to `/opt/healingpresence/`, restarts the systemd unit. The site is live at `https://thehealingpresence.in`.

## 7. Verify

- `curl -I https://thehealingpresence.in/` → 200, valid Let's Encrypt cert
- Sign in as `admin` → `/reception` day-grid renders → create a 2h booking → cascade-block 11 AM appears → email goes out → calendar event lands on Upma's Google Calendar (if Google connected)
