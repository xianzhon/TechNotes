#!/bin/bash

# Find all .md files recursively (safely handles spaces in filenames)
files=()
while IFS= read -r -d $'\0' file; do
    files+=("$file")
done < <(find . -type f -name "*.md" -print0)
## Uses find with -print0 to safely handle filenames with spaces or special characters

# Check if any .md files were found
if [ ${#files[@]} -eq 0 ]; then
    echo "No .md files found in current directory or subdirectories."
    exit 1
fi

# Select a random file
random_index=$((RANDOM % ${#files[@]}))
random_file="${files[random_index]}"

# Open the file with the default application
open "$random_file"

echo "Opened: $random_file"
