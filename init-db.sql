-- Database initialization script
-- This script runs when the PostgreSQL container is first created

-- Create database if it doesn't exist (handled by POSTGRES_DB env var)
-- Create user if it doesn't exist (handled by POSTGRES_USER env var)

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE xholacracy TO holacracy_user;

-- Enable extensions if needed
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Note: Schema migrations are handled by Flyway in the Spring Boot application
