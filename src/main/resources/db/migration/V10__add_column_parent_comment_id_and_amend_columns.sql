ALTER TABLE comments
    ADD COLUMN parent_comment_id UUID REFERENCES comments(id) ON DELETE CASCADE;

ALTER TABLE comments
    RENAME COLUMN coment_date TO comment_date;

ALTER TABLE comment_likes
    RENAME COLUMN coment_id TO comment_id;
