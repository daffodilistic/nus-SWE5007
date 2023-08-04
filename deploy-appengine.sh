#!/bin/bash
cd rigsg
./mvnw install
# NOTE Might not work in Linux due the space between "-D" and "app.deploy.projectId"
# ./mvnw --projects ${{ matrix.services }} --also-make clean package spring-boot:repackage -DskipTests=true
./mvnw  --projects demo clean package spring-boot:repackage appengine:deploy -D app.deploy.projectId=sapient-symbol-388402 -D app.deploy.version=test-v1
