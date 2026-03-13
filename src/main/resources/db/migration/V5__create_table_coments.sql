-- Create table for coments and coments likes

CREATE TABLE coments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    author_id UUID REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID REFERENCES posts(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    coment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE coment_likes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    coment_id UUID NOT NULL REFERENCES coments(id) ON DELETE CASCADE,
    like_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(user_id, coment_id)
);