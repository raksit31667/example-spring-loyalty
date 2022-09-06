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

CREATE TABLE IF NOT EXISTS "transaction"
(
    id              UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id         UUID        NOT NULL REFERENCES "user",
    activity_points BIGINT      NOT NULL,
    performed_on    TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS "user_subscription"
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    user_id       uuid      NOT NULL,
    product_id    VARCHAR   NOT NULL,
    product_name  VARCHAR   NOT NULL,
    subscribed_on TIMESTAMPTZ,
    active_until  TIMESTAMPTZ
);

CREATE MATERIALIZED VIEW IF NOT EXISTS "user_subscription_count" AS
SELECT user_id, COUNT(*) as number_of_subscriptions
FROM "user_subscription"
GROUP BY user_id
WITH DATA;
