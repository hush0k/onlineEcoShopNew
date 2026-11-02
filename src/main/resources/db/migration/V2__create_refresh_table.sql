create sequence seq_refresh_token start with 1 increment by 50;

create table refresh_token (
                               id bigint primary key,
                               user_id bigint not null,
                               token_hash varchar(255) not null,
                               expires_at timestamptz not null,
                               revoked bool not null default false,
                               created_at timestamptz not null default now(),

                               constraint refresh_token_user foreign key (user_id) references users(id) on delete cascade,
                               constraint uq_token_hash unique (token_hash)
);

create index idx_refresh_token_user_id on refresh_token(user_id);