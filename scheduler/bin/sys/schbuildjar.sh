#!/bin/bash
cd ${SCHPATH}
git pull

./gradlew clean build -Denv=$1
