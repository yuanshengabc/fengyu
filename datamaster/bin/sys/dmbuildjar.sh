#!/bin/bash
cd ${DMPATH}
git pull

./gradlew clean build -Denv=$1
