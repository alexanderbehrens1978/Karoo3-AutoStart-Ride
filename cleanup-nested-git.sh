#!/usr/bin/env bash
set -euo pipefail

echo "🧹 Cleanup verschachtelter .git-Ordner…"

# Projekt-Root sicherstellen
if ! git rev-parse --show-toplevel >/dev/null 2>&1; then
  echo "❌ Bitte im Projekt-Root ausführen (wo das Haupt-.git liegt)!"
  exit 1
fi

ROOT="$(git rev-parse --show-toplevel)"
cd "$ROOT"

echo "🔎 Suche nach verschachtelten .git-Verzeichnissen…"
MAPFILE=()
while IFS= read -r -d '' d; do
  # Root-.git überspringen
  if [[ "$d" != "$ROOT/.git" ]]; then
    MAPFILE+=("$d")
  fi
done < <(find "$ROOT" -path "$ROOT/.git" -prune -o -name ".git" -type d -print0)

if [[ ${#MAPFILE[@]} -eq 0 ]]; then
  echo "✅ Keine verschachtelten .git gefunden."
else
  echo "⚠️  Entferne folgende verschachtelten .git-Verzeichnisse:"
  printf ' - %s\n' "${MAPFILE[@]}"
  for d in "${MAPFILE[@]}"; do
    rm -rf "$d"
  done
fi

# Index bereinigen: Verzeichnisse neu hinzufügen (falls Git sie als Subrepo markiert hatte)
echo "🧾 Bereinige Git-Index…"
git add -A

echo "✅ Cleanup abgeschlossen. Du kannst jetzt committen/pushen."

