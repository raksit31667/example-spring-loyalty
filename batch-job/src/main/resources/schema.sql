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

CREATE TABLE IF NOT EXISTS "user_subscription"
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    user_id       uuid      NOT NULL,
    product_id    VARCHAR   NOT NULL,
    product_name  VARCHAR   NOT NULL,
    subscribed_on TIMESTAMPTZ,
    active_until  TIMESTAMPTZ
);

ALTER SEQUENCE "user_subscription_id_seq" START 1 INCREMENT 1000 CACHE 1;

CREATE MATERIALIZED VIEW IF NOT EXISTS "user_subscription_count" AS
SELECT user_id, COUNT(*) as number_of_subscriptions
FROM "user_subscription"
GROUP BY user_id
WITH DATA;