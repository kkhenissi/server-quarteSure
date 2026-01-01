-- V2__create_fillol_table.sql
CREATE TABLE IF NOT EXISTS fillol (
    id SERIAL PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    status VARCHAR(50),
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES winner_user(user_id)
        ON DELETE CASCADE
);
