#!/bin/bash
[ ! -f "$1" ] && { echo "fichier \"$1\" non trouvé"; exit 1; }
echo "Nous sommes le $(date +"%d %B"), il est : $(cat "$1")."

