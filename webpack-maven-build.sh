#!/bin/bash

cd server
mvn clean 
mkdir -p target/classes/static
cd - && cd ui
npm run build
rsync -avz build/* ../server/target/classes/static/
cd - && cd server
mvn package -DskipTests=true
