CREATE TABLE processed_payments (
    id BIGSERIAL PRIMARY KEY,
    payment_intent_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    credits INT NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT NOW()
);
