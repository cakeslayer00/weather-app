create table if not exists sessions(
    id uuid primary key,
    user_id bigint references users(id),
    expires_at timestamp
)