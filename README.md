# SWE5007 Project
It's the final semester

![Final Countdown](https://upload.wikimedia.org/wikipedia/en/2/22/The_Final_Countdown_single.png)

## Software Requirements
### Backend
- JDK 17
- Spring Boot 3.1.0
- Maven 3.9
- Docker 24.0
### Frontend
1. Prerequisites
- Ensure Vue.js cli installed. To install cli run this command `npm install -g @vue/cli`. To check if the cli is already installed run the following command `vue --version`.

2. Project setup
run this command `npm install`

3. The command json-server will start a server using the data in our data.json file. [OPTIONAL IF USING LOCAL DATA]
run this command `json-server data.json`

4. Open "Frontend" folder >> Open config.js >> update the latest bearer token.

5. Compiles and hot-reloads for development
run this command `npm run serve`

6. Compiles and minifies for production
run this command `npm run build'

## Local Development & Docker Setup
Backend services are built via `docker build` and run via `docker run`.
The `docker-compose.yml` file is used to orchestrate the services.
Note that the JAR file is built during the GitHub Actions workflow.
A simple inject and extract method is used to copy the JAR file into the 
custom Docker image. See `Dockerfile` for more details.

To run the services locally for development use, use the following command:
```shell
docker compose up --remove-orphans
```

## CI/CD Settings and Deployment Setup
### Deployment Instructions
1. Ensure that Cloud SQL instance is running before deploying the application.
2. Push/merge branches to `production/backend` or `production/frontend`

### Github Actions setup
The Github Actions workflow builds the JAR file and directly deploys the
application to Google App Engine. The workflow is triggered on every tag pushed
to the repository.

See the workflow YAML files defined in the `.github/workflows` folder for
details.

The following Github Secrets are required:
- `POSTGRESQL_USERNAME`: The username for the Postgres database
- `POSTGRESQL_PASSWORD`: The password for the Postgres database
- `GCP_CREDENTIALS_JSON`: Base64 encoded version of the JSON credentials file
- 
### Google App Engine setup
1. App Engine Admin API needs to be enabled in the Google Cloud Console, under
the menu option "APIs and services".
2. Ensure that the correct default service account used for App Engine is set 
in App Engine -> Settings.
3. If using a custom service account key, ensure that the following roles are
assigned to it (replace admin roles with deployer/viewer/editor if required):
   - App Engine Admin
   - Cloud Build Service Account
   - Cloud SQL Client
   - Storage Admin
4. If you want the custom service account key to use the App Engine default
service account, add the custom service account key as a principal to the
App Engine default service account
