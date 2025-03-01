CREATE TABLE test
(
    uno     uuid PRIMARY KEY,
    user_id bigint references users(id),
    expires_at timestamp
);