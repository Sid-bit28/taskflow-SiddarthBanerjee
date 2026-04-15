# TaskFlow API

## 1. Overview
TaskFlow is a secure, containerized REST API for task and project management. It allows users to register, create projects, and manage tasks with priority and status tracking.

**Tech Stack:**
* **Backend:** Java 21, Spring Boot 3+ (REST, Data JPA, Security)
* **Database:** PostgreSQL 15
* **Migrations:** Flyway
* **Authentication:** Spring Security with stateless JSON Web Tokens (JWT), BCrypt hashing
* **Build/Deployment:** Gradle, Docker, Docker Compose (Multi-stage builds)

## 2. Architecture Decisions
* **Containerization First:** The application relies on a multi-stage `Dockerfile`. Stage 1 uses a full Gradle JDK image to compile the `.jar`, while Stage 2 uses a minimal Alpine JRE to run it. This drastically reduces the final image size and eliminates the need for the host machine to have Java or Gradle installed.
* **Strict Database Versioning:** Flyway is used to strictly manage the schema. The teardown (`U`) and setup (`V`) scripts are separated. Seed data uses `ON DELETE CASCADE` to ensure referential integrity when dropping users, ensuring no orphaned projects or tasks are left behind.
* **Stateless Security:** Implemented JWT-based authentication over session cookies. This makes the API completely stateless and easier to scale horizontally.
* **Configuration Management:** Transitioned from `application.yml` to `application.properties` to ensure robust configuration loading and zero dependencies on external YAML parsing libraries in minimal Spring Boot environments. Secrets (like the Database password and JWT key) are securely passed via Docker environment variables.
* **Tradeoffs & Exclusions:** * A Refresh Token architecture was intentionally left out to keep the authentication flow simple and scope-focused.
    * Soft-deletes were excluded in favor of hard cascades to keep the initial database footprint small and queries straightforward.

## 3. Running Locally
You only need Docker and Docker Compose installed on your machine. No local Java or Gradle setup is required.

```bash
# Clone the repository
git clone [https://github.com/your-username/taskflow.git](https://github.com/your-username/taskflow.git)
cd taskflow

# Set up environment variables (if applicable, or configure directly in docker-compose)
# cp .env.example .env

# Build the images and start the containers in detached mode
sudo docker compose up --build -d

# Check the logs to verify Spring Boot has started
sudo docker logs taskflow_api