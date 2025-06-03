#!/bin/bash

# Get all .md files in the current directory
files=( *.md )

# Check if any .md files exist
if [ ${#files[@]} -eq 0 ]; then
    echo "No .md files found in the current directory."
    exit 1
fi

# Select a random file
random_file="${files[RANDOM % ${#files[@]}]}"

# Open the file with the default application
open "$random_file"

echo "Opened: $random_file"
