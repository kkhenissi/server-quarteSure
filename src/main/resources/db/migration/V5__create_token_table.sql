-- V5__create_token_table.sql

CREATE TABLE IF NOT EXISTS token_table (
    id BIGINT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    validated_at TIMESTAMP NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_user_token FOREIGN KEY (user_id)
        REFERENCES winner_user(user_id)
        ON DELETE CASCADE
);

-- Index pour accélérer la recherche par token
CREATE UNIQUE INDEX idx_token ON token_table(token);
