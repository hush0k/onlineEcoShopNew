alter table users
    add column is_deleted boolean not null default false,
    add column deleted_at date;

create index idx_users_is_deleted_deleted_at on users (is_deleted, deleted_at);
