CREATE TABLE IF NOT EXISTS sessions(
    id UUID PRIMARY KEY default gen_random_uuid(),
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL

)