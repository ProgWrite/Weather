CREATE TABLE IF NOT EXISTS sessions (
                                        id UUID PRIMARY KEY,
                                        user_id INT NOT NULL,
                                        expires_at TIMESTAMP NOT NULL,
                                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);