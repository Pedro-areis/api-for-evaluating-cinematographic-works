-- Amend tables comments, posts and works

ALTER TABLE coments RENAME TO comments;

ALTER TABLE coment_likes RENAME TO comment_likes;

ALTER TABLE posting_likes RENAME TO post_likes;

ALTER TABLE works
ALTER COLUMN score SET DEFAULT 0.0;