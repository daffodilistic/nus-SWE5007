param (
    [string]$applicationYml = ""
)
$env:APP_VERSION = "1"
$env:APP_ENGINE_PROJECT = "apt-terrain-401911"
$env:PROJECT_NAME = "demo"
$env:POSTGRESQL_USERNAME = "prd-ric-app-user"
$env:POSTGRESQL_PASSWORD = "password"
$env:PORT = 8080
Set-Location ..\rigsg
if ($applicationYml -ne "") {
    # Backup existing application.yml
    Copy-Item -Path .\$env:PROJECT_NAME\src\main\resources\application.yml -Destination .\$env:PROJECT_NAME\src\main\resources\application.yml.bak -Force
    # Replace application.yml with the specified file
    Copy-Item -Path .\$env:PROJECT_NAME\src\main\resources\$applicationYml -Destination .\$env:PROJECT_NAME\src\main\resources\application.yml -Force
}
.\mvnw.cmd install
# NOTE server port specified via CLI takes precedence over application.yml
.\mvnw.cmd -e -X --projects $env:PROJECT_NAME --also-make clean package spring-boot:repackage appengine:deploy `
    -D server.port=$env:PORT `
    -D skipTests=true `
    -D app.deploy.projectId=$env:APP_ENGINE_PROJECT `
    -D app.deploy.version=$env:APP_VERSION `
