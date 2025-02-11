#!/bin/bash

mvn package

# Default (hardcoded) file paths
DEFAULT_FILE1_PATH="/Users/waheedashraf/Downloads/dublin_trees.json"
DEFAULT_FILE2_PATH="/Users/waheedashraf/Downloads/dublin-property.csv"

# Check if arguments are provided; otherwise, use default paths
if [ "$#" -eq 2 ]; then
    DUBLIN_TREE_PATH="$1"
    DUBLIN_PROPERTY_PATH="$2"
    echo "Using custom file paths:"
else
    DUBLIN_TREE_PATH="$DEFAULT_FILE1_PATH"
    DUBLIN_PROPERTY_PATH="$DEFAULT_FILE2_PATH"
    echo "Using default file paths:"
fi

# Display the selected paths
echo "DUBLIN_TREE_PATH: $DUBLIN_TREE_PATH"
echo "DUBLIN_PROPERTY_PATH: $DUBLIN_PROPERTY_PATH"

# Export environment variables
export DUBLIN_TREE_PATH
export DUBLIN_PROPERTY_PATH


# Run the Java application
java -jar target/brightbeam-api-0.0.1.jar
