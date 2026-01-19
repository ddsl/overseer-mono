create table if not exists users (
    blocked BOOLEAN DEFAULT false,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    deleted_at timestamp(6),
    email varchar(255) not null unique,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    login varchar(255) not null unique,
    password varchar(255),
    uuid UUID not null,
    primary key (uuid)
);

create table if not exists user_roles (
    role varchar(255) not null,
    user_uuid UUID not null,

    primary key (role, user_uuid)
);

alter table if exists user_roles
    add constraint FKb4bms60ebskkrd05297us35x9
        foreign key (user_uuid)
        references users (uuid);

