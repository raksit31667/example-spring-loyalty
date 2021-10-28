CREATE TABLE transaction
(
    id           UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id      UUID        NOT NULL REFERENCES users,
    points       BIGINT      NOT NULL,
    performed_on TIMESTAMPTZ NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL
);