cd rigsg
.\mvnw.cmd install
.\mvnw --projects info-collection-system --also-make clean package spring-boot:repackage appengine:deploy -D skipTests=true -D app.deploy.projectId=sapient-symbol-388402 -D app.deploy.version=test-v1
