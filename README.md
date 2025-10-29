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
*   **Frontend:** Vue, JavaScript, Vite
*   **Backend:** JPA, Lombok, SpringBoot, Gradle, Axios, Nginx
*   **Database:** PostgreSQL, Liquibase
*   **Deployment:** Docker
*   **Documentation:** OpenAPI

## Project setup in IntelliJ for development

### Frontend

* **Start the project:**
* go to frontend folder:
```sh
cd frontend
```
* Installing Project Dependencies (if needed):
```sh
npm install
```
* Launching the dev server
```sh
npm run dev
```
* For production build
```sh
npm run build
```
The production-ready files will be generated in the dist/ folder.


* **Recommended Browser Setup**
Chromium-based browsers (Chrome, Edge, Brave, etc.):
- Vue.js devtools
- Turn on Custom Object Formatter in Chrome DevTools
Firefox:
- Vue.js devtools
- Turn on Custom Object Formatter in Firefox DevTools
- Customize configuration
- See Vite Configuration Reference.

### Backend

* **Docker Desktop:** install and open it.
* **Open IntelliJ**: backend as project directory.
* **Activate docker:** Command in terminal window:
```sh
docker compose up
```
* **IntelliJ Database:** add a new PostgreSQL data source to connect to your Docker database.
* **For production build:** Paste sensitive information in application.properties and build a project by running a
command:
```sh
.\gradlew build
```
The executable JAR file will be generated in the backend/build/libs/ directory.
* **Run application:** run VeebirakenduseProjektApplication.java

### Usage

* During development use for frontend http://localhost:5173/
* Backend uses localhost:5433 for SQL Database and 8080 for requests

## Project setup in production

This guide outlines the steps to deploy the application on a Linux server using Docker. The architecture consists of three containers managed by Docker Compose:

* PostgreSQL Database: The application's database.
* Spring Boot Backend: The Java API.
* Nginx Frontend: A web server that serves the static Vue.js files and forwards API requests to the Spring Boot backend.

Prerequisites: 
* A Linux server (e.g., Ubuntu 22.04) with a public IP address and SSH access.
* Docker and Docker Compose installed on the server.
* A Docker Hub account to host your backend image.
* The server's firewall is configured to allow incoming traffic on port 80 (HTTP).

### Part 1: Build and Push Docker images

#### Part 1.1. The Backend Image (on your local machine)

This step is done on your development computer whenever you make changes to the backend.

Build the application JAR. Use of -x test to avoid tests is optional:
```sh
./gradlew clean build -x test
```
The Dockerfile can be found in Git.
Build the Docker image: (Replace projectprogressline with your Docker Hub username, if needed)
```sh
docker build -t projectprogressline/progress-line-backend .
```

Tag the image as latest:
```sh
docker tag progress-line projectprogressline/progress-line-backend:latest
```

Log in to Docker Hub:
```sh
docker login
```

Push the image to Docker Hub:
```sh
docker push projectprogressline/progress-line-backend:latest
```
#### Part 1.2. The frontend image  (on your local machine)
TODO

### Part 2: Configure the Server (once)

These steps are performed once on your production server.

#### Part 2.1. Add Docker Image to backend (once)
Create the backend configuration directory and file:
```sh
sudo mkdir /opt/progress-line
```
Change ownership to the current user (e.g., ubuntu):
```sh
sudo chown $USER:$USER /opt/progress-line
```
Create and open the properties file for editing:
```sh
nano /opt/progress-line/application.properties
```
Add content to application.properties based on file application.properties-server-template.yml
The hostname "postgres-db" MUST match the database service name in docker-compose.yml
Save and exit nano.

Create the docker-compose.yml file:
```sh
nano docker-compose.yml
```
Add content to docker-compose.yml based on file docker-compose-server-template.yml
Save and exit nano.

#### Part 2.2. Add Nginx reverse proxy (once)

Nginx handles traffic coming to http://{ip-address} so that backend port 8080 is not exposed to the internet.

Install Nginx to server:
```sh
sudo apt update
sudo apt install nginx
```
Check if the server has IP6 address (inet6 other than link-local fe80... or loopback ::1/128 in output):
```sh
ip addr
```
If the server has no IPV6 address, then change configuration file:
```sh
sudo nano /etc/nginx/sites-enabled/default
```
Comment out the line:
```sh
# listen [::]:80 default-server
```
Start Nginx:
```sh
sudo service nginx start
```
Check, if address http://{server-ip} shows Nginx welcome message.

Configure reverse proxy:
```sh
sudo nano /etc/nginx/sites-available/default
```
Add these lines:
```sh
        location /api {
                proxy_pass http://localhost:8080/api;
        }
```
To assign domain name, specify server_name under "server":
```sh
        server_name myapp.com www.yourdomain.com;
```
Test for Nginx configuration errors:
```sh
sudo nginx -t
```
Restart Nginx:
```sh
sudo systemctl restart nginx
```
### Part 3: Add Docker image for frontend
TODO

### Part 4: Launch the Application

You are now ready to start the entire application stack.

Pull the latest backend image from Docker Hub:
 ```sh
docker-compose pull
```

Build the frontend image and start all services:
```sh
docker-compose up --build -d
```
--build: This tells Docker Compose to build the frontend image using its Dockerfile.
-d: This runs the containers in detached mode (in the background).

Verify the status:
```sh
docker-compose ps
```
All three containers (progress-line-frontend, progress-line-backend, postgres-db) should show a state of Up.

Your application should now be accessible in a web browser via your server's IP address: http://<your_server_ip>

### Application Management

To View backend logs:
```sh
docker-compose logs -f progress-line
```
To view frontend logs
```sh
docker-compose logs -f frontend
```
To stop the application:
```sh
docker-compose down
```


  