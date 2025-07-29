# Stock Watchlist Application



## Features

- **User Authentication**: Register and login functionality
- **Stock Search**: Search for stocks by symbol and company, retrieving real-time data and hostoric (mock data)
- **Watchlist**: Add and remove stocks from your personal watchlist
## Technology Stack

### Frontend
- **React 19** with TypeScript
- **Vite** for build tooling
- **Tailwind CSS** for styling
- **React Router** for navigation
- **Axios** for API calls
- **Heroicons** for icons

### Backend
- **Spring Boot 3.5** with Kotlin
- **PostgreSQL** database
- **JPA/Hibernate** for ORM
- **BCrypt** for password hashing
- **OkHttp** for external API calls

### External Services
- **Yahoo Finance API** via RapidAPI for stock data

## Project Structure

```
stock-watchlist/
├── client/                 # React frontend
│   ├── src/
│   │   ├── api/           # API service layer
│   │   ├── components/    # Reusable React components
│   │   ├── dto/           # TypeScript data transfer objects
│   │   ├── view/          # Page components
│   │   └── ...
│   └── ...
├── server/                # Spring Boot backend
│   └── watchlist/
│       └── src/main/kotlin/com/stock/watchlist/
│           ├── controllers/   # REST controllers
│           ├── services/      # Business logic
│           ├── repositories/  # Data access layer
│           ├── models/        # Entity models
│           └── ...
├── database/              # Database scripts
│   ├── init.sql          # Database initialization
│   ├── cleanup.sql       # Database cleanup
│   └── db.bash           # Database setup script
└── README.md
```

This README provides a comprehensive overview of your stock watchlist application, including setup instructions, API documentation, and project structure. It's structured to help both users and developers understand and work with the application effectively.

## Prerequisites

- **Node.js** (v18 or higher)
- **Java 21**
- **PostgreSQL** (v12 or higher)
- **RapidAPI Key** for Yahoo Finance API

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd stock-watchlist
```

### 2. Database Setup
```bash
# Make sure PostgreSQL is running
# Run the database initialization script
chmod +x database/db.bash
./database/db.bash
```

### 3. Backend Setup
```bash
cd server/watchlist

# Set up environment variable for RapidAPI
export RAPIDAPI_KEY="your-rapidapi-key-here"

# Make the start script executable
chmod +x start.bash

# Start the backend server
./start.bash
```

The backend will be available at `http://localhost:8080`

### 4. Frontend Setup
```bash
cd client

# Install dependencies
npm install

# Start the development server
npm run dev
```

The frontend will be available at `http://localhost:5173`

## API Endpoints

### User Service
- `POST /api/users/register` - Register a new user
- `POST /api/users/login` - User login
- `DELETE /api/users/{username}`

### Stock Operations
- `GET /api/stocks` - Get all stocks
- `GET /api/stocks/search?query=${query}` - Search stocks by symbol

### Watchlist Management
- `GET /api/watchlist/{username}` - Get user's watchlist
- `POST /api/watchlist/{symbol}` - Add stock to watchlist
- `DELETE /api/watchlist/{symbol}` - Remove stock from watchlist

## Database Schema

### Tables
- **users**: User authentication data
- **stocks**: Stock information (symbol, company name, price)
- **watchlist_items**: User watchlist entries
