#!/bin/bash
cd rigsg
./mvnw install
# NOTE Might not work in Linux due the space between "-D" and "app.deploy.projectId"
./mvnw package appengine:deploy -D app.deploy.projectId=sapient-symbol-388402