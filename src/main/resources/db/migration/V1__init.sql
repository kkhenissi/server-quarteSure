-- V1__init.sql
-- Création de toutes les tables de l'application

-- ===================== winner_user =====================
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
    selectedOptions VARCHAR(255),
    parrainer_par VARCHAR(255),
    reset_password_token VARCHAR(255) UNIQUE,
    reset_password_expiry TIMESTAMP
);

-- ===================== casaque =====================
CREATE TABLE casaque (
    casaque_id VARCHAR(255) PRIMARY KEY,
    casaque BYTEA
);

-- ===================== fillol =====================
CREATE TABLE fillol (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    status VARCHAR(50),
    user_id INTEGER REFERENCES winner_user(user_id) ON DELETE CASCADE
);

-- ===================== subscriptions =====================
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amountPaid DOUBLE PRECISION,
    currency VARCHAR(10) DEFAULT 'EUR',
    paymentMethod VARCHAR(50),
    transactionId VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active',
    createdAt TIMESTAMP DEFAULT now(),
    user_id INTEGER REFERENCES winner_user(user_id) ON DELETE CASCADE
);

-- ===================== payments =====================
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DOUBLE PRECISION,
    currency VARCHAR(10) DEFAULT 'EUR',
    transactionId VARCHAR(255),
    status VARCHAR(50) DEFAULT 'succeeded',
    paidAt TIMESTAMP DEFAULT now(),
    user_id INTEGER REFERENCES winner_user(user_id) ON DELETE CASCADE,
    subscription_id UUID REFERENCES subscriptions(id) ON DELETE CASCADE
);

-- ===================== token_table =====================
CREATE TABLE token_table (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255),
    createdAt TIMESTAMP,
    expiresAt TIMESTAMP,
    validatedAt TIMESTAMP,
    user_id INTEGER REFERENCES winner_user(user_id) ON DELETE CASCADE
);

-- ===================== traffic_log =====================
CREATE TABLE traffic_log (
    id SERIAL PRIMARY KEY,
    target_url VARCHAR(255),
    ipAddress VARCHAR(50),
    timestamp TIMESTAMP
);
