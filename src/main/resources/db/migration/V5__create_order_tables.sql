create sequence orders_seq start with 1 increment by 50;

create table orders (
                        id bigint primary key,
                        user_id bigint not null,
                        created_at timestamptz not null default now(),
                        predicted_date_of_delivery timestamptz,
                        status varchar(30) not null,
                        sub_price numeric(12, 2),
                        delivery_price numeric(12, 2),
                        discount numeric(12, 2),
                        total_price numeric(12, 2),
                        currency varchar(10),
                        delivery_city varchar(100),
                        delivery_address varchar(255),

                        constraint fk_orders_user foreign key (user_id) references users(id) on delete cascade,
                        constraint chk_orders_status check ( status in ('CREATED', 'PENDING_PAYMENT', 'PAID', 'PROCESSING',
                                                                        'SHIPPED', 'IN_TRANSIT', 'READY_FOR_PICKUP',
                                                                        'DELIVERED', 'CANCELLED', 'RETURNED') ),
                        constraint chk_orders_currency check ( currency is null or currency in ('KZT', 'USD', 'RUB', 'CNY') )
);

create index idx_orders_user_id on orders(user_id);

create sequence order_histories_seq start with 1 increment by 50;

create table order_histories (
                                 id bigint primary key,
                                 order_id bigint not null,
                                 status varchar(30) not null,
                                 timestamp timestamptz not null default now(),

                                 constraint fk_order_histories_order foreign key (order_id) references orders(id) on delete cascade
);

create index idx_order_histories_order_id on order_histories(order_id);
