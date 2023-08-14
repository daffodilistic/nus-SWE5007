cd rigsg
.\mvnw.cmd install
# NOTE server port specified via CLI takes precedence over application.yml
.\mvnw.cmd --projects info-collection-system --also-make clean package spring-boot:repackage appengine:deploy -D server.port=8080 -D skipTests=true -D app.deploy.projectId=sapient-symbol-388402 -D app.deploy.version=test-v1
