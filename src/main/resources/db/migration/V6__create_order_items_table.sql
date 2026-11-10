create sequence order_items_seq start with 1 increment by 50;

create table order_items (
                             id bigint primary key,
                             order_id bigint not null,
                             product_id uuid not null,
                             quantity integer not null,
                             price numeric(12, 2) not null,

                             constraint fk_order_items_order foreign key (order_id) references orders(id) on delete cascade,
                             constraint fk_order_items_product foreign key (product_id) references products(id),
                             constraint chk_order_items_quantity check ( quantity > 0 )
);

create index idx_order_items_order_id on order_items(order_id);
create index idx_order_items_product_id on order_items(product_id);
