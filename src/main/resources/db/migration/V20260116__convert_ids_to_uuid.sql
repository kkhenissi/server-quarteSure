-- V20260116__convert_ids_to_uuid.sql
ALTER TABLE subscriptions
    ALTER COLUMN id TYPE uuid USING id::uuid;

ALTER TABLE payments
    ALTER COLUMN id TYPE uuid USING id::uuid;
