create table products (
                          id uuid primary key default gen_random_uuid(),
                          name varchar(255) not null,
                          description text,
                          category_id uuid,
                          price numeric(12, 2) not null,
                          weight numeric(12, 3),
                          weight_type varchar(20) not null default 'GRAM',
                          stock_quantity integer not null default 0,
                          created_at timestamptz not null default now(),
                          updated_at timestamptz not null default now(),

                          constraint fk_products_category foreign key (category_id) references categories(id) on delete set null,
                          constraint chk_products_price check ( price >= 0 ),
                          constraint chk_products_weight check ( weight is null or weight >= 0 ),
                          constraint chk_products_stock_quantity check ( stock_quantity >= 0 ),
                          constraint chk_products_weight_type check ( weight_type in ('MILLIGRAM', 'GRAM', 'KILOGRAM', 'TONNE') )
);

create index idx_products_category_id on products(category_id);
