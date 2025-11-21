# Progress Line

## Description
Progress Line is a Java web application designed to visualize and support student academic progress. 
It enables to view individual student progress lines and document interventions (like sending an email reminder). 

## Features
* **Interactive Progress Graphs:** 
    Dynamic charts showing cumulative points over time for individual students. 
    Clicking on a line enables to see details for this students.
* **User Authentication:**
    Secure login for educators.
* **Direct Intervention Tracking:** 
  Empowers educators to log and track specific interventions directly on a student's timeline.
  This feature creates a comprehensive record of support provided, allowing for better monitoring of its impact. 

  This includes actions such as:
  * **Email Reminders:** Recording when a reminder email was sent to a student.
  * **Sending email using template:** Sending email including students data.
  * **Custom Interventions:** The ability to add other relevant notes or actions taken to support student progress.

## Technologies Used
*   **Frontend:** Vue, JavaScript, Vite, Chart.js, Axios
*   **Backend:** SpringBoot, JPA, Lombok, Gradle
*   **Database:** PostgreSQL, Liquibase
*   **Deployment:** Docker, Nginx
*   **Documentation:** OpenAPI

## Local Development Setup

Prerequisites:
* Node.js (v18 or newer)
* JDK (v21 or newer)
* Docker Desktop
* IntelliJ IDEA or a similar IDE

### Local Backend Setup

The backend relies on a PostgreSQL database running in a Docker container.

#### 1. Start the Database

* **Docker Desktop:** install and open it.
* **Activate docker:** navigate to directory `backend` (if needed) and run:
```sh
docker compose up
```
The database is now available on localhost:5432.

#### 2. Run the Application

* Open the `backend` directory as a project in IntelliJ.
* Ensure your `application.properties` or `application-local.properties` file is configured to connect to the local Docker database.
* **(Recommended) Connect IDE to Database:** 
 To easily view the schema, run queries, and inspect data during development, it is recommended to connect your IDE's database tool 
 (e.g., IntelliJ's "Database" panel) to the running Docker container.
* Run the application by executing the main method in `VeebirakenduseProjektApplication.java`.

The backend API is now running at http://localhost:8080.


### Local Frontend Setup

#### 1. Install Dependencies

* **Start the project:** navigate to `frontend` directory:
```sh
cd frontend
```
* **Install dependencies** listed in `package.json`:
```sh
npm install
```

#### 2. Run the frontend application

* **Launch the Dev Server**:
```sh
npm run dev
```

The frontend application is now available at `http://localhost:5173`


#### Recommended Browser Setup
Prefer chromium-based browsers (Chrome, Edge, Brave, etc.):
- Vue.js devtools
- Turn on Custom Object Formatter in Chrome DevTools
Firefox:
- Vue.js devtools
- Turn on Custom Object Formatter in Firefox DevTools
- Customize configuration
- See Vite Configuration Reference.


## Production Setup

This project is configured for automated deployment to a Linux server using GitLab CI/CD. 
The production architecture consists of:
- PostgreSQL & Spring Boot Backend: Running as Docker containers managed by Docker Compose.
- Nginx Frontend: A web server that serves the static Vue.js files and acts as a reverse proxy for the backend API.

Prerequisites: 
* A Linux server (e.g., Ubuntu 22.04) with a public IP address and SSH access.
* Docker, Docker Compose and Nginx installed on the server.
* A Docker Hub account to host the backend image.
* The server's firewall configured to allow incoming traffic on port 80 (HTTP).

### 1. One-Time Server Configuration

These steps must be performed **once** on the production server to prepare it for deployment.

#### 1.1. Configure Backend Services

* Install Nginx to server:
```sh
sudo apt update
sudo apt install nginx
```
* Start Nginx:
```sh
sudo service nginx start
```

Create the backend configuration directory and necessary files.
* Create directory and set permissions:
```sh
sudo mkdir /opt/progress-line
sudo chown $USER:$USER /opt/progress-line
```
* Create the properties file:
```sh
cd /opt/progress-line
nano application.properties
```
* Add content based on the `application.properties-server-template.yml` file. 
The hostname `postgres-db` MUST match the database service name in `docker-compose.yml`.

* Create the docker-compose.yml file:
```sh
nano docker-compose.yml
```
* Add content based on the `docker-compose-server-template.yml` file.

#### 1.2. Configure Nginx Reverse Proxy

Nginx handles traffic coming to `http://{ip-address}` so that backend port 8080 is not exposed to the internet.

* Check if the server has IP6 address (inet6 other than link-local fe80... or loopback ::1/128 in output):
```sh
ip addr
```
* Open the Nginx configuration file:
```sh
sudo nano /etc/nginx/sites-enabled/default
```
If the server has no IPV6 address, then comment out the line:
```sh
# listen [::]:80 default-server
```
* Replace the content of the configuration file:
```sh
server {
  listen 80 default_server;
  server_name api.jairus.ee; # Replace with your actual domain or IP

    root /var/www/html;
    index index.html;

    # This line ensures that refreshing the page on any sub-route still loads the app.
    location / {
        try_files $uri $uri/ /index.html;
    }

    # This block forwards all requests starting with /api/ to the backend
    location /api/ {
        proxy_pass http://localhost:8080/api/;
    }
}
```
* Test for Nginx configuration errors:
```sh
sudo nginx -t
```
* Restart Nginx:
```sh
sudo systemctl restart nginx
```
* Create ssh key for server (CMD in your local machine) without password:
```sh
ssh-keygen -t rsa -b 4096 -C "gitlab-ci-deploy-key" -f %USERPROFILE%\.ssh\gitlab_ci_deploy_key
```
* Inside server create directory .ssh
```sh
mkdir -p ~/.ssh
chmod 700 ~/.ssh
```
* Show public key (CMD in your local machine):
```sh
type %USERPROFILE%\.ssh\gitlab_ci_deploy_key.pub
```
* Copy public key to server
```sh
echo "PASTE_YOUR_PUBLIC_KEY_HERE" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```
* Copy private key (in your local machine) to CI/CD variables in Git

#### 1. Enable frontend

* Set permissions for web root directory:
```sh
sudo chown -R $USER:$USER /var/www/html
```

### 2. CI/CD Pipeline

After the one-time server setup, deployments are handled by the GitLab CI/CD pipeline 
defined in `.gitlab-ci.yml`. 
When changes are pushed to the main branch, the pipeline automatically builds and deploys the application.

For the pipeline to work, set the following CI/CD variables in your GitLab project's settings:
* DOCKER_USERNAME
* DOCKER_PASSWORD
* SSH_PRIVATE_KEY
* SERVER_IP
* SERVER_USER

When using Microsoft login, then also specify:
* VITE_AZURE_SCOPES
* VITE_AZURE_TENANT_ID
* VITE_AZURE_CLIENT_ID
* VITE_AZURE_AUTHORITY

Application is now be accessible in a web browser via your server's 
IP address: `http://<your_server_ip>` or `http://<your_server_name>`

### Application Management (on the server)

After the one-time server setup, all deployments are handled automatically by the GitLab CI/CD pipeline.

The steps below are for manual management of the application on the server. 
All commands should be run from the `/opt/progress-line` directory.

#### 1. Manual update of backend image (optional)
* Pull the latest backend image from Docker Hub:
 ```sh
docker-compose pull
```

* Start all services:
```sh
docker-compose up -d
```
--build: This tells Docker Compose to build the frontend image using its Dockerfile.
-d: This runs the containers in detached mode (in the background).


All three containers (`progress-line-frontend`, `progress-line-backend`, `postgres-db`) should show a state of Up.

### Application Debugging (on the server)

* Verify the Docker status:
```sh
docker-compose ps
```
* View backend logs:
```sh
docker-compose logs -f progress-line
```
* View database logs
```sh
docker-compose logs -f postgres
```
* Stop the application:
```sh
docker-compose down
```
* Check for access requests and status codes (404, 502, etc.)
```sh
tail -f /var/log/nginx/access.log
```
* Check for Nginx startup or configuration errors
```sh
tail -f /var/log/nginx/error.log
```
## Documentation

OpenAPI is used for documentation.

* Interactive UI: `http://localhost:8080/swagger-ui/index.html`
* Raw Spec JSON file: `http://localhost:8080/v3/api-docs`