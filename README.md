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

## Docker Setup
Backend services are built via `docker build` and run via `docker run`.
The `docker-compose.yml` file is used to orchestrate the services.
Note that the JAR file is built during the GitHub Actions workflow.
A simple inject and extract method is used to copy the JAR file into the 
custom Docker image. See `Dockerfile` for more details.

To run the services locally for development use, use the following command:
```shell
docker compose up --remove-orphans
```

## Github Actions setup
The Github Actions workflow builds the JAR file and directly deploys the
application to Google App Engine. The workflow is triggered on every tag pushed
to the repository.

See the workflow YAML files defined in the `.github/workflows` folder for details.

The following Github Secrets are required:
- `POSTGRES_USERNAME`: The username for the Postgres database
- `POSTGRES_PASSWORD`: The password for the Postgres database