-- V20261031__jeu_parrain.sql
-- Jeux du parrain : instantané complet d'une course jouée (pari, chevaux retirés, parts des filleuls, arrivée, gain).
-- (Hibernate ddl-auto=update crée la même structure ; IF NOT EXISTS évite tout conflit.)

CREATE TABLE IF NOT EXISTS jeu_parrain (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    course_key VARCHAR(255) NOT NULL,
    course VARCHAR(255),
    depart_ms BIGINT,
    pari VARCHAR(255),
    mise NUMERIC(12, 2),
    gain NUMERIC(12, 2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    snapshot TEXT NOT NULL,
    CONSTRAINT uk_jeu_parrain_user_course UNIQUE (user_id, course_key)
);

CREATE INDEX IF NOT EXISTS idx_jeu_parrain_user ON jeu_parrain (user_id, depart_ms DESC);
