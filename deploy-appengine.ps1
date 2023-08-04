cd rigsg
.\mvnw.cmd install
.\mvnw  --projects demo clean package spring-boot:repackage appengine:deploy -D app.deploy.projectId=sapient-symbol-388402 -D app.deploy.version=test-v1
