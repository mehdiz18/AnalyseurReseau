#!/bin/bash
javac -cp "./lib/jar/*" ./src/*.java -d ./bin
java -cp "./lib/jar/*:./bin/:." App $1