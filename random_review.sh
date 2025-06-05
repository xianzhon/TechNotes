#!/bin/bash
#
# Random Markdown File Opener
# ===========================
# Finds and opens a random .md file in the current directory and subdirectories,
# while excluding specified directories (node_modules, .git, etc.).
#
# Usage:
#   ./open_random_md.sh [OPTIONS] [SEARCH_ROOT]
#
# Arguments:
#   SEARCH_ROOT  Directory to begin search (default: current directory)
#
# Options:
#   -c, --command       The command to open the file (default `open` in OSX)
#   -e, --exclude DIRS  Additional directories to exclude (comma-separated)
#   -h, --help          Show this help message
#   -v, --verbose       Show detailed processing information
#
# Examples:
#   ./random_review.sh                     # Search current directory
#   ./random_review.sh ~/notes             # Search in ~/notes
#   ./random_review.sh -c code ~/notes     # Search in ~/notes, and open file with vs code
#   ./random_review.sh -e "cache,dist"     # Exclude cache/ and dist/
#   ./random_review.sh -v -e "build" .     # Verbose search from current dir
#

# Initialize default configuration
exclude_dirs=("node_modules" ".git" "tmp" "*todo*")
verbose=false
search_root="."
open_comd="open"

# Parse command line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        -e|--exclude)
            IFS=',' read -r -a extra_exclude <<< "$2"
            exclude_dirs+=("${extra_exclude[@]}")
            shift 2
            ;;
        -h|--help)
            sed -n '3,25p' "$0" | grep -E '^#' | sed -E 's/^#+//'
            exit 0
            ;;
        -c|--command)
            open_cmd="$2"
            shift 2
            ;;
        -v|--verbose)
            verbose=true
            shift
            ;;
        *)
            search_root="$1"
            shift
            ;;
    esac
done

# Validate search root exists
if [[ ! -d "$search_root" ]]; then
    echo "Error: Directory '$search_root' does not exist" >&2
    exit 1
fi

$verbose && echo "Searching in: $search_root"
$verbose && echo "Excluding: ${exclude_dirs[*]}"

# Build find command with exclusions
find_cmd="find \"$search_root\" -type f -name '*.md'"
for dir in "${exclude_dirs[@]}"; do
    find_cmd+=" -not \( -path '*/$dir/*' -prune \)"
done

$verbose && echo "Find command: $find_cmd"

# Find all .md files recursively (safely handles spaces in filenames)
files=()
while IFS= read -r -d $'\0' file; do
    files+=("$file")
    $verbose && echo "Found: $file"
done < <(eval "$find_cmd -print0")

# Check if any .md files were found
if [[ ${#files[@]} -eq 0 ]]; then
    echo "No .md files found in '$search_root' (excluding: ${exclude_dirs[*]})." >&2
    exit 1
fi

$verbose && echo "Total files found: ${#files[@]}"

# Select a random file
random_index=$((RANDOM % ${#files[@]}))
random_file="${files[random_index]}"

# Open the file with the default application
$verbose && echo "Opening: $random_file"
$open_cmd "$random_file"

echo "Opened: $random_file"
exit 0
