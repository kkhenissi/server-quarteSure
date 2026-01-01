-- V4__create_payment_table.sql
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC(15, 2),
    currency VARCHAR(10) DEFAULT 'EUR',
    transaction_id VARCHAR(100),
    status VARCHAR(50) DEFAULT 'succeeded',
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER NOT NULL,
    subscription_id UUID NOT NULL,
    CONSTRAINT fk_payment_user
        FOREIGN KEY(user_id)
        REFERENCES winner_user(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_payment_subscription
        FOREIGN KEY(subscription_id)
        REFERENCES subscriptions(id)
        ON DELETE CASCADE
);
