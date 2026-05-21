# Deploying The Healing Presence to Oracle Cloud Always Free

A step-by-step runbook for putting this Spring Boot app on a free, always-on
Oracle Cloud Ampere VM with HTTPS and MySQL. Total time: ~30 minutes for the
first run; ~2 minutes per redeploy after that.

---

## 1. Sign up for Oracle Cloud (free tier)

1. Go to https://www.oracle.com/cloud/free/
2. Click **Start for free** → fill in details (a credit card is required for
   identity verification but **will not be charged** unless you upgrade out of
   the free tier).
3. Pick the **home region** carefully — it cannot be changed later. For India
   the best is **Mumbai (ap-mumbai-1)** or **Hyderabad (ap-hyderabad-1)**.
4. Verify your phone + email and finish.

## 2. Create an "Ampere A1" VM (the free-forever one)

> Oracle's Always Free tier gives you up to **4 OCPUs + 24 GB RAM** on
> ARM Ampere VMs. We'll use **2 OCPU / 12 GB** which is more than enough.

1. From the Oracle Cloud Console: **Menu → Compute → Instances**
2. Click **Create instance**.
3. Settings:
   - Name: `healing-presence-prod`
   - Image: **Canonical Ubuntu 22.04** (under "Change image")
   - Shape: **Change shape → Ampere → VM.Standard.A1.Flex**
   - OCPUs: 2; Memory: 12 GB (well within Always Free)
   - Networking: leave defaults (creates a VCN + public subnet)
   - SSH keys: **Generate a key pair for me** → download both files and keep
     them safe. You'll need the `.key` private key to SSH in.
4. Click **Create**. Wait ~1 minute. Copy the **public IP address** from the
   instance detail page.

## 3. Open ports 80 and 443 on the Security List

Oracle Cloud's default Security List only opens port 22 (SSH). We need 80 + 443
for Caddy / Let's Encrypt.

1. **Networking → Virtual Cloud Networks** → click your VCN
2. → **Security Lists** → click the default Security List
3. → **Add Ingress Rules** with:
   - Source CIDR: `0.0.0.0/0`, Destination port: `80`
   - Source CIDR: `0.0.0.0/0`, Destination port: `443`

## 4. SSH in + run the setup script

From your laptop:

```bash
chmod 600 ~/Downloads/healing-presence-prod.key
ssh -i ~/Downloads/healing-presence-prod.key ubuntu@<PUBLIC_IP>
```

Once on the VM:

```bash
curl -sLO https://raw.githubusercontent.com/kaivalyaekaa/The-Healing-Presence/jsp-rebuild/deploy/setup-oracle.sh
curl -sLO https://raw.githubusercontent.com/kaivalyaekaa/The-Healing-Presence/jsp-rebuild/deploy/healing-presence.service
curl -sLO https://raw.githubusercontent.com/kaivalyaekaa/The-Healing-Presence/jsp-rebuild/deploy/Caddyfile
chmod +x setup-oracle.sh
sudo bash setup-oracle.sh
```

The script will:
- Install Java 21, MySQL 8, Caddy
- Create the `healingpresence` database + `thp_app` MySQL user (random password
  is generated and written to `/etc/healing-presence.env`)
- Create the `thp` system user and `/opt/healingpresence/`
- Drop in the systemd unit + Caddyfile
- Enable UFW (ports 22, 80, 443 only)

When it finishes, the script prints next-steps. **Read them.**

## 5. Upload the WAR + start the service

From your **laptop** (project root):

```bash
export VM_IP=<PUBLIC_IP>
export VM_USER=ubuntu
bash deploy/redeploy.sh
```

This packages the WAR with Maven, scps it to the VM, and restarts the systemd
service.

On the **VM**, edit `/etc/healing-presence.env` and fill in real secrets:

```bash
sudo nano /etc/healing-presence.env
# Required: MAIL_USER, MAIL_PASS (for contact form notifications)
# Required for receptionist→Google Calendar push: GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET
# (See Section 7 below for the Google Cloud setup)
```

Then restart:

```bash
sudo systemctl restart healing-presence
sudo systemctl status healing-presence
```

Tail logs to confirm Tomcat started:

```bash
sudo journalctl -u healing-presence -f
```

You should see `Started HealingPresenceApplication in N.NNN seconds`.

## 6. Point your domain at the VM

In your DNS provider (GoDaddy / Cloudflare / wherever `thehealingpresence.in`
is registered), create / update the A record:

| Type | Host | Value |
|------|------|-------|
| A | `@` (or `www`) | `<PUBLIC_IP>` |
| A | `www` | `<PUBLIC_IP>` |

Wait 5–30 minutes for DNS propagation.

Then update Caddyfile on the VM to use the real domain instead of the
`.nip.io` placeholder:

```bash
sudo nano /etc/caddy/Caddyfile
# change the first line from "<IP>.nip.io {" to "thehealingpresence.in, www.thehealingpresence.in {"
sudo systemctl reload caddy
```

Caddy will auto-provision Let's Encrypt HTTPS certificates within seconds.

Verify:
```bash
curl -I https://thehealingpresence.in/
# HTTP/2 200, valid TLS cert
```

## 7. Connect Google Calendar (one-time, by Upma or Admin)

1. Go to https://console.cloud.google.com → create a new project
   (`The Healing Presence`).
2. **APIs & Services → Library** → enable **Google Calendar API**.
3. **APIs & Services → OAuth consent screen** → External, fill in app name +
   user-support email + developer contact. Add your domain. Add the scope
   `https://www.googleapis.com/auth/calendar.events`.
4. **Credentials → Create credentials → OAuth client ID** → Web application:
   - Authorised JavaScript origins: `https://thehealingpresence.in`
   - Authorised redirect URIs: `https://thehealingpresence.in/admin/google-calendar/callback`
     and (for local dev) `http://localhost:8080/admin/google-calendar/callback`
5. Copy the Client ID + Client Secret. On the VM:
   ```bash
   sudo nano /etc/healing-presence.env
   # Paste GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET
   sudo systemctl restart healing-presence
   ```
6. Sign in to the app as `admin` → visit `/admin/google-calendar` → click
   **Connect Google Calendar** → sign in with **Upma's** Google account → consent.
7. Test by creating a receptionist booking and checking Upma's Google Calendar.

## 8. Day-to-day operations

| Task | Command |
|------|---------|
| Deploy new build | `VM_IP=… bash deploy/redeploy.sh` (from laptop) |
| Tail logs | `sudo journalctl -u healing-presence -f` |
| Restart app only | `sudo systemctl restart healing-presence` |
| Reload Caddy config | `sudo systemctl reload caddy` |
| MySQL CLI | `mysql -u root healingpresence` |
| Free disk check | `df -h` |
| Free RAM check | `free -h` |
| Check active sessions | `sudo journalctl -u healing-presence --since "1 hour ago" \| grep -i login` |

## Troubleshooting

**"Cannot connect to MySQL"**
- Check service: `sudo systemctl status mysql`
- Check creds: `cat /etc/healing-presence.env | grep DB_`
- Test DSN: `mysql -u thp_app -p<pass> healingpresence -e "SELECT 1;"`

**"Caddy can't get a Let's Encrypt cert"**
- DNS not pointing at the VM yet — wait + check `dig thehealingpresence.in`.
- Port 80 or 443 blocked — re-check Oracle Cloud Security List.

**"App boots but I see 502 from Caddy"**
- App not listening on 8080 yet — give it 30 s after restart.
- Or MySQL connection failed — check `journalctl -u healing-presence`.

**"Out of memory"**
- Edit `/etc/systemd/system/healing-presence.service`, raise `-Xmx512m`
  to `-Xmx768m` or `-Xmx1g` (you have 12 GB RAM, plenty of headroom).
- `sudo systemctl daemon-reload && sudo systemctl restart healing-presence`.
