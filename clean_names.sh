#!/usr/bin/env bash
set -euo pipefail

root="${1:-.}"

find "$root" -depth -print0 | while IFS= read -r -d '' path; do
  dir="$(dirname "$path")"
  base="$(basename "$path")"

  # Enlève les accents si possible
  ascii="$(printf '%s' "$base" | iconv -f UTF-8 -t ASCII//TRANSLIT 2>/dev/null || printf '%s' "$base")"

  # Remplace espaces par underscores, supprime caractères spéciaux,
  # compresse les underscores multiples
  new="$(printf '%s' "$ascii" \
        | sed -E 's/[[:space:]]+/_/g; s/[^A-Za-z0-9._-]//g; s/_+/_/g; s/^_+|_+$//g')"

  if [[ "$new" != "$base" ]]; then
    target="$dir/$new"
    i=1
    # Évite d’écraser un fichier existant : ajoute un suffixe _1, _2, ...
    while [[ -e "$target" ]]; do
      ext="${new##*.}"
      name="${new%.*}"
      if [[ "$ext" != "$new" ]]; then
        target="$dir/${name}_$i.$ext"
      else
        target="$dir/${new}_$i"
      fi
      ((i++))
    done
    echo "Renommé : $path -> $target"
    mv "$path" "$target"
  fi
done
