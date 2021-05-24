#!/usr/bin/env bash
#mvn clean verify -DskipTests

while true
do
  date
  echo "Running Mayhem!"
  ! time java -Djava.security.egd=file:/dev/./urandom -XX:+UseG1GC -Djava.awt.headless=true -jar target/mayhem-jar-with-dependencies.jar
done
