#!/bin/bash
cd rigsg
./mvnw clean package spring-boot:repackage -DskipTests=true
