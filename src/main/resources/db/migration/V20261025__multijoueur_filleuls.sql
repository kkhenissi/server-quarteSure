-- V20261025__multijoueur_filleuls.sql
-- Mode multijoueur : disponibilité du filleul et combinaisons attribuées par le parrain.
-- (Hibernate ddl-auto=update crée la même structure ; IF NOT EXISTS évite tout conflit.)

ALTER TABLE fillol ADD COLUMN IF NOT EXISTS disponible BOOLEAN DEFAULT TRUE;
ALTER TABLE fillol ADD COLUMN IF NOT EXISTS disponible_depuis TIMESTAMP;

CREATE TABLE IF NOT EXISTS attribution_jeu (
    id BIGSERIAL PRIMARY KEY,
    fillol_id INTEGER NOT NULL REFERENCES fillol(id) ON DELETE CASCADE,
    course VARCHAR(255),
    depart_ms BIGINT,
    date_envoi TIMESTAMP NOT NULL,
    tickets TEXT
);

CREATE TABLE IF NOT EXISTS combinaison_jeu (
    id BIGSERIAL PRIMARY KEY,
    attribution_id BIGINT NOT NULL REFERENCES attribution_jeu(id) ON DELETE CASCADE,
    numeros VARCHAR(255) NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'A_JOUER',
    ordre INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_attribution_jeu_fillol ON attribution_jeu (fillol_id, date_envoi DESC);
CREATE INDEX IF NOT EXISTS idx_combinaison_jeu_attribution ON combinaison_jeu (attribution_id);
