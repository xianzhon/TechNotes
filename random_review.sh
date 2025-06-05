#!/bin/bash

# Define directories to exclude (add more as needed)
exclude_dirs=("node_modules" ".git" "tmp")

# Build find command with exclusions
find_cmd="find . -type f -name '*.md'"
for dir in "${exclude_dirs[@]}"; do
    find_cmd+=" -not \( -path '*/$dir/*' -prune \)"
done

# Find all .md files recursively (safely handles spaces in filenames)
files=()
while IFS= read -r -d $'\0' file; do
    files+=("$file")
done < <(eval "$find_cmd -print0")

# Check if any .md files were found
if [ ${#files[@]} -eq 0 ]; then
    echo "No .md files found (excluding: ${exclude_dirs[*]})."
    exit 1
fi

# Select a random file
random_index=$((RANDOM % ${#files[@]}))
random_file="${files[random_index]}"

# Open the file with the default application
open "$random_file"

echo "Opened: $random_file"
