param (
    [string]$applicationYml = ""
)
$env:APP_ENGINE_PROJECT = "shaped-splicer-397009"
$env:PROJECT_NAME = "demo"
#$env:PROJECT_NAME = "info-collection-system"
$env:POSTGRESQL_USERNAME = "prd-ric-app-user"
$env:POSTGRESQL_PASSWORD = "password"
$env:PORT = 8080
Push-Location rigsg
if ($applicationYml -ne "") {
    # Backup existing application.yml
    Copy-Item -Path .\$env:PROJECT_NAME\src\main\resources\application.yml -Destination .\$env:PROJECT_NAME\src\main\resources\application.yml.bak -Force
    # Replace application.yml with the specified file
    Copy-Item -Path .\$env:PROJECT_NAME\src\main\resources\$applicationYml -Destination .\$env:PROJECT_NAME\src\main\resources\application.yml -Force
}
.\mvnw.cmd install
# NOTE server port specified via CLI takes precedence over application.yml
.\mvnw.cmd --projects $env:PROJECT_NAME --also-make clean package spring-boot:repackage `
    -D server.port=$env:PORT `
    -D skipTests=true `
    -D app.deploy.projectId=$env:APP_ENGINE_PROJECT `
    -D app.deploy.version=test-v6
New-Item -Type Directory -Path .\..\docker\volumes\$env:PROJECT_NAME -Force
Copy-Item -Path .\$env:PROJECT_NAME\target\$env:PROJECT_NAME.jar -Destination .\..\docker\volumes\$env:PROJECT_NAME\$env:PROJECT_NAME.jar -Force
Pop-Location
docker run --publish "8080:$env:PORT" `
    --volume "./docker/volumes/$env:PROJECT_NAME:/application" `
    --workdir "/application" `
    eclipse-temurin:17-jre `
    java `
    -jar $env:PROJECT_NAME.jar