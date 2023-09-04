$env:APP_ENGINE_PROJECT = "shaped-splicer-397009"
#$env:APP_ENGINE_SERVICE = "demo"
$env:APP_ENGINE_SERVICE = "info-collection-system"
$env:PORT = 8080
cd rigsg
.\mvnw.cmd install
# NOTE server port specified via CLI takes precedence over application.yml
.\mvnw.cmd --projects $env:APP_ENGINE_SERVICE --also-make clean package spring-boot:repackage appengine:deploy -D server.port=$env:PORT -D skipTests=true -D app.deploy.projectId=$env:APP_ENGINE_PROJECT -D app.deploy.version=test-v1
