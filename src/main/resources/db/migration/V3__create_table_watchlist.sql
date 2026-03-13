-- Create the "watchlist" table

CREATE TYPE status_work AS ENUM ('watched', 'pending');

CREATE TABLE watchlist (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) DEFAULT 'My Watchlist',
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    work_id UUID REFERENCES works(id) ON DELETE CASCADE,
    type type_work NOT NULL,
    status status_work DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(user_id, work_id),
    UNIQUE(user_id, name)
);