create table if not exists users(
    id bigserial primary key,
    login varchar(50) unique,
    password varchar(255)
)