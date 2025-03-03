create table if not exists locations(
    id bigserial primary key,
    name varchar(50),
    user_id bigint references users(id),
    latitude decimal,
    longitude decimal
)