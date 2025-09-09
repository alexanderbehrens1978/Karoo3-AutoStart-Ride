#!/usr/bin/env bash
set -euo pipefail

REPO_NAME_DEFAULT="karoo3-autostart-ride"
VISIBILITY="${VISIBILITY:-public}"   # public | private
COMMIT_MSG="${COMMIT_MSG:-Initial commit}"
REMOTE_NAME="origin"

echo "🚀 Push zu GitHub starten…"

# 0) Prüfe, ob wir in einem Git-Repo sind – wenn nicht: init
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "🆕 Kein Git-Repo gefunden – initialisiere…"
  git init
fi

# 1) Branch auf main
git branch -M main || true

# 2) .gitignore (Android) anlegen/ergänzen, falls nicht vorhanden
if [[ ! -f .gitignore ]]; then
  cat > .gitignore <<'EOF'
# IDE
.idea/
*.iml

# Gradle/Build
.gradle/
build/
*/build/
.cxx/

# Lokal/Secrets
local.properties
keystore.*
*.log

# OS
.DS_Store
Thumbs.db
EOF
  echo "📝 .gitignore erstellt."
fi

# 3) Alles adden & committen
git add -A
if ! git diff --cached --quiet; then
  git commit -m "$COMMIT_MSG"
else
  echo "ℹ️  Nichts zu committen (Index sauber)."
fi

# 4) Remote-URL prüfen
HAS_REMOTE=false
if git remote get-url "$REMOTE_NAME" >/dev/null 2>&1; then
  HAS_REMOTE=true
fi

# 5) Repo-Name bestimmen (Ordnername als Default)
FOLDER_NAME="$(basename "$(pwd)")"
REPO_NAME="${REPO_NAME:-$REPO_NAME_DEFAULT}"
read -rp "📦 GitHub Repo-Name [${REPO_NAME}]: " INPUT || true
REPO_NAME="${INPUT:-$REPO_NAME}"

# 6) Wenn gh vorhanden → Repo erstellen (falls kein Remote) & push
if command -v gh >/dev/null 2>&1; then
  echo "🧰 gh erkannt – verwende GitHub CLI."
  if [[ "$HAS_REMOTE" == false ]]; then
    echo "🆕 Erstelle GitHub-Repo '${REPO_NAME}' (${VISIBILITY})…"
    gh repo create "${REPO_NAME}" --"${VISIBILITY}" --source=. --remote="${REMOTE_NAME}" --push
    echo "✅ Repo erstellt & gepusht."
    exit 0
  else
    echo "🔗 Remote vorhanden: $(git remote get-url ${REMOTE_NAME})"
    echo "⬆️  Push main…"
    git push -u "${REMOTE_NAME}" main
    exit 0
  fi
fi

# 7) Fallback ohne gh: Remote-URL abfragen/setzen
echo "ℹ️  gh nicht gefunden – klassischer Push."
if [[ "$HAS_REMOTE" == false ]]; then
  read -rp "🔗 GitHub-URL (z.B. https://github.com/<user>/${REPO_NAME}.git): " REMOTE_URL
  git remote add "${REMOTE_NAME}" "${REMOTE_URL}"
fi

# 8) Falls Remote schon Commits hat → rebase versuchen, dann push
set +e
git pull --rebase "${REMOTE_NAME}" main
PULL_STATUS=$?
set -e

if [[ $PULL_STATUS -ne 0 ]]; then
  echo "⚠️  Rebase fehlgeschlagen oder kein Remote-stand – push erzwungen."
  git push --force-with-lease "${REMOTE_NAME}" main
else
  git push -u "${REMOTE_NAME}" main
fi

echo "✅ Push abgeschlossen."

