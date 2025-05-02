create table if not exists transactions
(
    id              uuid  default random_uuid() primary key,
    amount          decimal not null,
    creation_time   timestamp with time zone not null,
    reference       varchar(255) not null,
    bank_slogan     varchar(255) not null,
    receiving_user  varchar(255) not null
);