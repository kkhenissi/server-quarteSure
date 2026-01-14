
CREATE TABLE casaque (
    id BIGSERIAL PRIMARY KEY,
    couleur VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);
