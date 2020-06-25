#!/bin/bash

echo "GPG passphrase:"
read -s line
./gradlew clean build publish -Dorg.gradle.project.release=true -Dorg.gradle.project.signing.gnupg.passphrase="$line"
