-- Initial schema for the application

-- Create the "users" table
CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   password_hash VARCHAR(255) NOT NULL,
   date_birth DATE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);