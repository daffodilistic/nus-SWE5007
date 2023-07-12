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
- NodeJS 18 LTS

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