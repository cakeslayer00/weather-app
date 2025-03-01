create table if not exists Users(
    id bigserial primary key,
    login varchar(255) not null,
    password varchar(255) not null
)