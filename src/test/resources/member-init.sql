create schema if not exists testcode;
create table if not exists testcode.member (
    id    serial       not null,
    name  varchar(30)  null,
    email varchar(100) null,
    constraint member_pkey primary key (id)
    );
create unique index if not exists member_email_idx on testcode.member (email);
