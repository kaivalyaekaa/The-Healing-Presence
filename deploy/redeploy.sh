#!/usr/bin/env bash
# =============================================================================
# Redeploy the WAR to the Oracle Cloud VM.
#
# Run from the project root (where pom.xml lives) on your laptop.
#
#   ./deploy/redeploy.sh                  # full: package + scp + restart
#   ./deploy/redeploy.sh --skip-build      # just scp the existing WAR
#
# Pre-requisites:
#   - You can ssh to ubuntu@$VM_IP with key auth (no password prompts)
#   - The first-time setup-oracle.sh has been run on the VM
#   - VM_IP env var is set (or replace the default below)
# =============================================================================
set -euo pipefail

VM_USER="${VM_USER:-ubuntu}"
VM_IP="${VM_IP:?Set VM_IP env var to the public IP of your Oracle Cloud VM (e.g. export VM_IP=129.x.x.x)}"
REMOTE_DIR="/opt/healingpresence"
WAR_PATH="target/healing-presence.war"

if [[ "${1:-}" != "--skip-build" ]]; then
    echo "==> Building WAR..."
    if [[ -x ./mvnw ]]; then
        ./mvnw -DskipTests package
    else
        ./build-with-jdk21.cmd -DskipTests package
    fi
fi

if [[ ! -f "${WAR_PATH}" ]]; then
    echo "[!] WAR not found at ${WAR_PATH}. Build failed?" >&2
    exit 1
fi

echo "==> Uploading WAR to ${VM_USER}@${VM_IP}..."
scp "${WAR_PATH}" "${VM_USER}@${VM_IP}:/tmp/healing-presence.war"

echo "==> Moving into place + restarting service..."
ssh "${VM_USER}@${VM_IP}" <<'EOF'
sudo mv /tmp/healing-presence.war /opt/healingpresence/healing-presence.war
sudo chown thp:thp /opt/healingpresence/healing-presence.war
sudo systemctl restart healing-presence
sleep 3
sudo systemctl is-active healing-presence && echo "[OK] healing-presence is running"
EOF

echo "==> Done. Tail logs with:"
echo "      ssh ${VM_USER}@${VM_IP} 'sudo journalctl -u healing-presence -f'"
