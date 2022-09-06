CREATE TABLE IF NOT EXISTS "user"
(
    id              uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    first_name      VARCHAR NOT NULL,
    last_name       VARCHAR NOT NULL,
    email           VARCHAR NOT NULL,
    phone           VARCHAR NOT NULL,
    activity_points BIGINT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);