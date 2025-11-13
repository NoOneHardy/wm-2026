# WM 2026 ⚽

A comprehensive betting game platform for the FIFA World Cup 2026. Place bets on matches, compete with friends, and climb the leaderboard!

## 📋 Table of Contents

- [About](#about)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Installation](#installation)
  - [Development Setup](#development-setup)
  - [Environment Configuration](#environment-configuration)
- [Usage](#usage)
  - [Running the Application](#running-the-application)
  - [Building](#building)
  - [Testing](#testing)
- [Project Structure](#project-structure)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## 🎯 About

WM 2026 is a full-stack betting platform designed for the FIFA World Cup 2026. Users can create accounts, place bets on matches in both group and knockout stages, earn points based on prediction accuracy, and track their progress on a global leaderboard. The application features a modern Angular frontend with Material Design, a robust Spring Boot backend, and comprehensive email notification system.

## ✨ Features

- **User Authentication & Authorization**
  - Secure user registration with email confirmation
  - JWT-based authentication
  - Role-based access control (User, Admin, Unconfirmed)

- **Bet Management**
  - Place bets on group stage and knockout matches
  - Set joker multipliers (1x-3x) for strategic betting
  - Auto-save functionality
  - Lock bets 5 minutes before match starts

- **Leaderboard & Rankings**
  - Real-time global leaderboard
  - Track position changes and movement indicators
  - View top 3 performing bets
  - Personal ranking history

- **Dashboard**
  - Personalized greeting based on time of day
  - Open bets overview with priority highlighting
  - Recent bet history
  - Personal and global statistics

- **Notifications**
  - In-app notifications for results, rankings, and reminders
  - Email notifications (configurable)
  - Notification preferences in user settings

- **User Settings**
  - Update profile information (username, email)
  - Change password
  - Avatar upload
  - Toggle notification preferences
  - Account deletion

- **Admin Panel**
  - User management and account confirmation
  - Result management for matches
  - Accept/deny user registrations

## 🛠️ Technology Stack

### Frontend
- **Framework**: Angular 18.2
- **UI Library**: Angular Material 18.2
- **State Management**: NgRx (Store & Effects) 18.1
- **Language**: TypeScript 5.5
- **Build Tool**: Angular CLI
- **Testing**: Jasmine + Karma

### Backend
- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Build Tool**: Gradle 8.9
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA
- **Database**: MySQL 8.3.0
- **Testing**: JUnit + Spring Boot Test

### DevOps & Infrastructure
- **Containerization**: Docker & Docker Compose
- **Reverse Proxy**: Nginx
- **CDN**: Nginx (for static assets)
- **Email**: Jakarta Mail (SMTP)

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Docker** (version 20.10+) and **Docker Compose** (version 2.0+)
- **Git**
- **Java 21** (for local development without Docker)
- **Node.js 18+** and **npm** (for local frontend development)
- **Gradle 8.9** (or use the included wrapper)

## 🚀 Getting Started

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/NoOneHardy/wm-2026.git
   cd wm-2026
   ```

2. **Set up environment variables**
   
   Create environment configuration files from examples:
   
   ```bash
   # Backend service configuration
   cp service/.env.example service/.env
   
   # MySQL database configuration
   cp mysql/.env.example mysql/.env
   
   # Proxy configuration
   cp proxy/.env.example proxy/.env
   ```

3. **Configure environment variables**
   
   Edit the created `.env` files with your configuration. See [Environment Configuration](#environment-configuration) for details.

### Development Setup

The easiest way to run the application for development is using Docker Compose:

```bash
# Start all services (frontend, backend, database, proxy, cdn)
docker-compose up

# Or run in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

This will start:
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:26001
- **Proxy**: http://localhost:26000
- **MySQL Database**: localhost:26006
- **CDN**: Accessible internally

### Environment Configuration

#### Service (.env)

Required environment variables for the backend service:

```bash
DATABASE_HOST=wm-dev-mysql       # Database hostname (use service name in Docker)
DATABASE_USER=your_db_user       # Database username
DATABASE_PW=your_db_password     # Database password

SECRET_KEY=your_secret_key       # JWT secret key (use strong random string)

SMTP_HOST=smtp.example.com       # SMTP server hostname
SMTP_PORT=587                    # SMTP server port
SMTP_USER=your_smtp_user         # SMTP username
SMTP_PW=your_smtp_password       # SMTP password
```

**Note**: Generate a secure SECRET_KEY using a tool like https://cryptii.com/pipes/hmac

#### MySQL (.env)

```bash
MYSQL_USER=your_db_user
MYSQL_PASSWORD=your_db_password
```

#### Proxy (.env)

Configure proxy settings as needed for your environment.

## 💻 Usage

### Running the Application

**With Docker Compose (Recommended for Development)**:
```bash
docker-compose up
```

**Running Components Individually**:

Frontend:
```bash
cd frontend
npm install
npm run start
# Access at http://localhost:4200
```

Backend:
```bash
cd service
./gradlew bootRun
# API available at http://localhost:26001
```

### Building

**Frontend**:
```bash
cd frontend
npm run build
# Production build output in dist/
```

**Backend**:
```bash
cd service
./gradlew build
# JAR file created in build/libs/
```

**Docker Images**:
```bash
# Build all images
docker-compose -f docker-compose.prod.yaml build

# Build specific service
docker build -f frontend/Dockerfile -t wm-2026-frontend .
docker build -f service/Dockerfile -t wm-2026-service .
```

### Testing

**Frontend**:
```bash
cd frontend

# Run tests
npm test

# Run tests in CI mode (headless)
npm run test-ci

# Run linter
npm run lint

# Fix linting issues
npm run lint:fix
```

**Backend**:
```bash
cd service

# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport
```

## 📁 Project Structure

```
wm-2026/
├── frontend/              # Angular frontend application
│   ├── src/
│   │   ├── app/          # Application components, services, and modules
│   │   ├── assets/       # Static assets (images, icons)
│   │   └── styles.css    # Global styles
│   ├── Dockerfile        # Frontend container definition
│   ├── package.json      # Node.js dependencies
│   └── angular.json      # Angular configuration
│
├── service/              # Spring Boot backend application
│   ├── src/
│   │   ├── main/java/    # Java source code
│   │   └── test/java/    # Test files
│   ├── Dockerfile        # Backend container definition
│   ├── build.gradle      # Gradle build configuration
│   └── .env.example      # Environment variables template
│
├── proxy/                # Nginx reverse proxy configuration
│   ├── Dockerfile
│   └── nginx.conf
│
├── mysql/                # MySQL configuration
│   └── .env.example      # Database environment template
│
├── docs/                 # Project documentation
│   └── SPEC.md          # Detailed feature specifications
│
├── docker-compose.yaml       # Development environment setup
├── docker-compose.prod.yaml  # Production environment setup
├── LICENSE              # GPL-3.0 License
└── README.md           # This file
```

## 🚢 Deployment

For production deployment:

1. **Build Docker images** for your registry:
   ```bash
   docker build -t your-registry/wm-2026-frontend:latest -f frontend/Dockerfile .
   docker build -t your-registry/wm-2026-service:latest -f service/Dockerfile .
   docker build -t your-registry/wm-2026-proxy:latest -f proxy/Dockerfile .
   ```

2. **Push images to your registry**:
   ```bash
   docker push your-registry/wm-2026-frontend:latest
   docker push your-registry/wm-2026-service:latest
   docker push your-registry/wm-2026-proxy:latest
   ```

3. **Deploy using production compose file**:
   ```bash
   docker-compose -f docker-compose.prod.yaml up -d
   ```

4. **Set up persistent storage** for MySQL data and CDN assets.

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

### Commit Convention

We use conventional commits for clear and consistent git history:

| Commit Type | Title                   | Description                                                      |
|-------------|-------------------------|------------------------------------------------------------------|
| `feat`      | Feature                 | A new feature                                                    |
| `fix`       | Bug Fixes               | A bug fix                                                        |
| `docs`      | Documentation           | Documentation only changes                                       | 
| `lint`      | ES-Lint                 | Code structure or linting changes                                |
| `refactor`  | Code Refactoring        | A code change that neither fixes a bug nor adds a feature        |
| `test`      | Tests                   | Adding missing tests or correcting existing tests                |
| `ci`        | Continuous Integrations | Changes to our CI configuration files and scripts or Dockerfiles |

**Example**:
```bash
git commit -m "feat: add user avatar upload functionality"
git commit -m "fix: resolve leaderboard sorting issue"
git commit -m "docs: update installation instructions"
```

### Development Workflow

1. Fork the repository
2. Create a feature branch (`git checkout -b feat/amazing-feature`)
3. Make your changes
4. Run tests and linting
5. Commit your changes using conventional commits
6. Push to your branch (`git push origin feat/amazing-feature`)
7. Open a Pull Request

## 📄 License

This project is licensed under the **GNU General Public License v3.0** - see the [LICENSE](LICENSE) file for details.

---

**Happy betting! May the best predictor win! 🏆**
