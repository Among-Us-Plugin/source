#!/bin/bash

# Check if screen is installed
if ! command -v screen >/dev/null 2>&1; then
    echo "Error: screen is not installed. Please install screen and try again."
    exit 1
fi
echo "screen is installed. Proceeding..."

# Define how often to check for remote changes
DELAY=3

# Define color variables for output
ACTION='\033[1;90m'
FINISHED='\033[1;96m'
READY='\033[1;92m'
NOCOLOR='\033[0m' # No Color
ERROR='\033[0;31m'

# Start the MC server in a screen session
screen -dmS minecraft ./gradlew runServer

while true; do

    echo -e ${ACTION}Checking Git repo${NOCOLOR}

    # Ensure on the correct branch
    BRANCH=$(git rev-parse --abbrev-ref HEAD)
    if [ "$BRANCH" != "main" ]; then
    echo -e ${ERROR}Not on main. Aborting.${NOCOLOR}
    exit 0
    fi

    # Fetch the latest changes
    git fetch

    # Get the latest commit hashes
    HEADHASH=$(git rev-parse HEAD)
    UPSTREAMHASH=$(git rev-parse main@{upstream})

    # Compare and reset if necessary
    if [ "$HEADHASH" != "$UPSTREAMHASH" ]; then
        echo -e ${ACTION}Updating local repository to match remote...${NOCOLOR}
        git reset --hard main@{upstream}
        echo -e ${FINISHED}Local repository updated to match remote.${NOCOLOR}

        sleep 1

        # Send STOP to the server
        screen -S minecraft -X stuff "`echo -ne \"stop\r\"`"

        # Wait for the server to terminate
        sleep 15
        

        # Restart the server
        screen -dmS minecraft ./gradlew runServer
        

    else
        echo -e ${FINISHED}Current branch is up to date with origin/main.${NOCOLOR}
    fi

    sleep ${DELAY}
done
