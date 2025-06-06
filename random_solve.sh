#!/bin/bash
#
# Random Markdown File Opener for Todo Directories
# ================================================
# Finds and opens a random .md file in directories matching '*todo*' pattern,
# while excluding common development directories (node_modules, .git, etc.).
#
# Usage:
#   ./random_solve.sh [START_DIR]
#
# Arguments:
#   START_DIR  - (Optional) Directory to begin search (default: current directory)
#
# Examples:
#   ./random_solve.sh           # Search from current directory
#   ./random_solve.sh ~/notes   # Search from ~/notes directory
#

### CONFIGURATION - Adjust these to your needs ###

# Base directory to start searching (uses first argument or current dir)
SEARCH_DIR="${1:-.}"

# Pattern to match target directories (case-insensitive)
DIR_PATTERN="*todo*"

# Directories to exclude from search
EXCLUDE_DIRS=("node_modules" ".git" "tmp" "vendor")

### MAIN SCRIPT LOGIC ###

# Build the find command with these components:
# 1. First find all directories matching our pattern
# 2. Then search those directories for .md files
# 3. Apply all specified directory exclusions
find_cmd="find \"$SEARCH_DIR\" -type d -iname \"$DIR_PATTERN\" -print0 | while IFS= read -r -d $'\0' target_dir; do"
find_cmd+=" find \"\$target_dir\" -type f -name '*.md'"

# Add each exclusion clause to the find command
for dir in "${EXCLUDE_DIRS[@]}"; do
    find_cmd+=" -not \( -path \"*/$dir/*\" -prune \)"
done

find_cmd+=" -print0; done"

### FILE COLLECTION ###

# Read null-delimited results into array (safe for special characters)
files=()
while IFS= read -r -d $'\0' file; do
    files+=("$file")
done < <(eval "$find_cmd")

### VALIDATION ###

if [ ${#files[@]} -eq 0 ]; then
    echo "No .md files found in '$DIR_PATTERN' directories (excluding: ${EXCLUDE_DIRS[*]})."
    exit 1
fi

### RANDOM SELECTION ###

# Select random file using bash $RANDOM modulo array length
random_file="${files[RANDOM % ${#files[@]}]}"

### FILE OPENING ###

# Open with default application (MacOS) and show confirmation
open "$random_file"
echo "Opened: $random_file"

exit 0
