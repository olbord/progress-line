# Progress Line

## Description
Progress Line is a Java web application designed to visualize and support student academic progress. 
It enables to view individual student progress lines and document interventions (like sending an email reminder). 

## Features
* **Interactive Progress Graphs:** 
    Dynamic charts showing cumulative points over time for individual students.
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
*   **Backend:** JPA, Liquibase, Lombok, SpringBoot, Gradle, Axios
*   **Database:** PostgreSQL
*   **Deployment:** Docker

## Project setup in IntelliJ

### Frontend

* **Start the project:**
* npm install (if needed)
* npm run dev

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

* **Docker Desktop:** install and open.
* **Open IntelliJ**: backend as project directory.
* **Activate docker:** Command in terminal window: docker compose up
* **IntelliJ Database:** add new source postgreSQL.
* **Build:** run build.gradle
* **Run application:** run VeebirakenduseProjektApplication.java

## Usage

* During development use for frontend http://localhost:5173/
* Backend uses localhost:5433 for SQL Database and 8080 for requests