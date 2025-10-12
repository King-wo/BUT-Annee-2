#!/bin/bash
[ $# -eq 0 ] && { echo "usage: $(basename $0) <heure>"; exit 1; }
echo "Nous sommes le $(date +"%d %B"), il est : $1."

