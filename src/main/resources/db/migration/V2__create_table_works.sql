-- Create the "works" table

CREATE TYPE type_work AS ENUM ('movie', 'series');

CREATE TABLE works (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    synopsis VARCHAR(255) NOT NULL,
    score FLOAT,
    type type_work NOT NULL,
    release_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);