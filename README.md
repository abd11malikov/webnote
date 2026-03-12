# 🌐 WebNote: Secure Full‑Stack CMS

WebNote is a professional-grade Content Management System (CMS) designed for high performance, scalability, and security. Built from the ground up to demonstrate a modern, enterprise-ready technical stack, it powers a live blogging platform.

## 🚀 Live Demo
Visit the live application: https://abdumalikov.tech

---

## 🏗️ Technical Architecture

The system follows a **decoupled architecture**, ensuring that backend logic and frontend presentation remain independent and scalable.

### Backend (The Brain)
- **Java 21 & Spring Boot 3:** The core API engine.
- **Security:** Stateless **JWT authentication** with **RBAC** (Role-Based Access Control) implemented using Spring Security 6.
- **Database:** **PostgreSQL** for managing relational data.
- **Storage:** **Cloudflare R2** (S3-compatible) for secure, high-performance media storage.
- **DevOps:** Fully **Dockerized** with multi-stage builds and deployed on a Linux VPS.

### Frontend (The Face)
- **Next.js 14:** Uses Server-Side Rendering (SSR) for improved SEO and social media previews (Open Graph).
- **Tailwind CSS:** Provides a modern and responsive design system.
- **State Management:** Custom authentication context to manage user sessions and JWT storage.

---

## 📂 Project Structure

```text
src/main/java/com/otabek/career
├── controller      # REST API endpoints
├── service         # Business logic and DTO mapping
├── repository      # JPA data access layer
├── entity          # Database schema (SQL + JSONB)
└── security        # JWT filter chain and security configuration
```

---

## 🛠️ Key Engineering Challenges Solved

**Hybrid Database Model**  
The database is optimized by separating searchable fields (email, GPA, role) into standard SQL columns while storing complex, nested data in PostgreSQL **JSONB** columns. This maintains flexibility without sacrificing query performance.

**Stateless Authentication**  
A custom `JwtAuthFilter` handles authentication without server-side sessions, allowing the API to scale horizontally across multiple instances.

**Cloud Deployment**  
Cloud firewall restrictions were solved by migrating from SMTP (which was blocked) to an HTTP-based Email API. An **Nginx/Caddy reverse proxy** was also configured for SSL management.

**Full-Stack Integration**  
A custom mapper layer converts Java entities into nested JSON DTOs so the frontend receives only the necessary and secure data.

---

## 🚀 How to Run Locally

### Prerequisites
- Java 21+
- Docker & Docker Compose
- PostgreSQL (or a local Docker container)

### Quick Start

**1. Clone the repository**
```bash
git clone https://github.com/abd11malikov/webnote.git
```

**2. Configure environment variables**

Create a `.env` file in the project root:

```text
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_long_secret_key
```

**3. Run with Docker**
```bash
docker compose up --build
```

---

**Lead Developer:** Otabek Abdumalikov