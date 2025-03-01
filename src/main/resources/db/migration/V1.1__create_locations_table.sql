create table if not exists locations(
    id bigserial primary key,
    name varchar(255),
    latitude decimal,
    longitude decimal,
    userId bigint references users(id)
)