-- V3__create_subscription_table.sql
CREATE TABLE IF NOT EXISTS subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount_paid NUMERIC(15, 2),
    currency VARCHAR(10) DEFAULT 'EUR',
    payment_method VARCHAR(50),
    transaction_id VARCHAR(100),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_subscription_user
        FOREIGN KEY(user_id)
        REFERENCES winner_user(user_id)
        ON DELETE CASCADE
);
