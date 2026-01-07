CREATE TABLE IF NOT EXISTS winner_user (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(20),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    credit INTEGER,
    lastConnexion VARCHAR(50),
    subscribeDate VARCHAR(50),
    role VARCHAR(50),
    selectedOptions TEXT,
    parrainer_par VARCHAR(100),
    reset_password_token VARCHAR(255) UNIQUE,
    reset_password_expiry TIMESTAMP
);
