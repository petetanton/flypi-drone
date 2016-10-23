#!/usr/bin/env bash
mvn clean package
cp target/*-jar-with-dependencies.jar target/fly-pi-drone.jar