-- V1__init.sql

CREATE TABLE winner_user (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(50),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    credit INTEGER,
    lastConnexion VARCHAR(50),
    subscribeDate VARCHAR(50),
    role VARCHAR(50),
    selectedOptions TEXT,
    parrainer_par VARCHAR(255),
    reset_password_token VARCHAR(255) UNIQUE,
    reset_password_expiry TIMESTAMP
);

-- Relations "OneToMany" / "OneToOne" seront gérées dans les tables Fillol, Payment, Subscription
-- avec des FOREIGN KEY pointant vers winner_user(user_id)
-- Table token_table
CREATE TABLE token_table (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255),
    created_at TIMESTAMP,
    expires_at TIMESTAMP,
    validated_at TIMESTAMP,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY(user_id) REFERENCES winner_user(user_id) ON DELETE CASCADE
);
-- Table subscriptions
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount_paid DOUBLE PRECISION,
    currency VARCHAR(10) DEFAULT 'EUR',
    payment_method VARCHAR(255),
    transaction_id VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT now(),
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_subscription_user FOREIGN KEY(user_id) REFERENCES winner_user(user_id) ON DELETE CASCADE
);
-- Table payments
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DOUBLE PRECISION,
    currency VARCHAR(10) DEFAULT 'EUR',
    transaction_id VARCHAR(255),
    status VARCHAR(50) DEFAULT 'succeeded',
    paid_at TIMESTAMP DEFAULT now(),
    user_id INTEGER NOT NULL,
    subscription_id UUID NOT NULL,
    CONSTRAINT fk_payment_user FOREIGN KEY(user_id) REFERENCES winner_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_payment_subscription FOREIGN KEY(subscription_id) REFERENCES subscriptions(id) ON DELETE CASCADE
);
-- Table fillol
CREATE TABLE fillol (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(50),
    user_id INTEGER,
    CONSTRAINT fk_fillol_user FOREIGN KEY(user_id) REFERENCES winner_user(user_id) ON DELETE CASCADE
);
-- Table traffic_log
CREATE TABLE traffic_log (
    id BIGSERIAL PRIMARY KEY,
    target_url VARCHAR(255),
    ip_address VARCHAR(50),
    timestamp TIMESTAMP
);
