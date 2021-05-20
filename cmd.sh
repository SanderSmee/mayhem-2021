#!/usr/bin/env bash
mvn clean verify

while true
do
  date
  echo "Running framboos"
  ! time java -Djava.security.egd=file:/dev/./urandom -XX:+UseG1GC -noverify -Djava.awt.headless=true -jar target/mayhem-jar-with-dependencies.jar
done
