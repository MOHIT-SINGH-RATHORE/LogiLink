# LogiLink - Transport Logistics Application

This is a full-stack application with a React frontend and Spring Boot backend.

## Prerequisites

Before running the application, make sure you have the following installed:

1. **Java 17** or higher
2. **Node.js** (v16 or higher) and **npm**
3. **Maven** (usually comes with Spring Boot projects)
4. **Docker** and **Docker Compose** (for MySQL database)
5. **MySQL** (if not using Docker)

## Project Structure

```
LogiLink/
├── frontend/          # React + Vite frontend (Port 5173)
├── backend/           # Spring Boot backend (Port 8080)
└── docker-compose.yaml # MySQL database configuration
```

## Step-by-Step Setup Instructions

### Step 1: Start the MySQL Database

You have two options:

#### Option A: Using Docker Compose (Recommended)
```bash
# From the project root directory
docker-compose up -d
```

This will start MySQL on port 3306 with:
- Database name: `transport_db`
- Username: `root`
- Password: `rootpassword`

#### Option B: Using Local MySQL
If you have MySQL installed locally, create a database:
```sql
CREATE DATABASE transport_db;
```

Make sure MySQL is running on port 3306 with the credentials matching `backend/src/main/resources/application.properties`.

### Step 2: Start the Backend (Spring Boot)

1. Open a terminal/command prompt
2. Navigate to the backend directory:
   ```bash
   cd backend
   ```
3. Run the Spring Boot application:
   ```bash
   # Using Maven Wrapper (Windows)
   .\mvnw.cmd spring-boot:run
   
   # OR if you have Maven installed globally
   mvn spring-boot:run
   ```

The backend will start on **http://localhost:8080**

Wait for the message: `Started BackendApplication in X.XXX seconds`

### Step 3: Start the Frontend (React)

1. Open a **new** terminal/command prompt
2. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
3. Install dependencies (first time only):
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ```

The frontend will start on **http://localhost:5173**

## Accessing the Application

Once both servers are running:
- **Frontend**: Open your browser and go to `http://localhost:5173`
- **Backend API**: Available at `http://localhost:8080/api/users`

## Troubleshooting

### Backend won't start
- **Check MySQL connection**: Make sure MySQL is running and accessible
- **Check port 8080**: Ensure no other application is using port 8080
- **Check Java version**: Run `java -version` to verify Java 17+ is installed

### Frontend won't start
- **Check Node.js**: Run `node -v` to verify Node.js is installed
- **Check port 5173**: Ensure no other application is using port 5173
- **Clear cache**: Try deleting `node_modules` and running `npm install` again

### Registration fails
- **Check backend is running**: The frontend needs the backend API to be running
- **Check database connection**: Verify MySQL is running and the database exists
- **Check console errors**: Open browser DevTools (F12) to see detailed error messages

### Database connection errors
- **Verify Docker container**: Run `docker ps` to check if MySQL container is running
- **Check credentials**: Verify the database credentials in `application.properties` match your MySQL setup
- **Check MySQL port**: Ensure port 3306 is not blocked by firewall

## Stopping the Application

1. **Stop Frontend**: Press `Ctrl+C` in the frontend terminal
2. **Stop Backend**: Press `Ctrl+C` in the backend terminal
3. **Stop MySQL** (if using Docker):
   ```bash
   docker-compose down
   ```

## Development Commands

### Backend
```bash
cd backend
.\mvnw.cmd spring-boot:run    # Run the application
.\mvnw.cmd clean install      # Build the project
```

### Frontend
```bash
cd frontend
npm run dev      # Start development server
npm run build    # Build for production
npm run preview  # Preview production build
```

## API Endpoints

- `POST /api/users/register` - Register a new user
- `GET /api/users/` - Get all registered users

## Notes

- The backend automatically creates/updates database tables on startup (Hibernate DDL auto-update)
- CORS is configured to allow requests from `http://localhost:5173`
- The application uses case-insensitive username and email checks to prevent duplicates


