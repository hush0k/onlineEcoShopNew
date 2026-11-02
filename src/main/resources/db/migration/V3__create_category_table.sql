create table categories (
                            id uuid primary key default gen_random_uuid(),
                            name varchar(255) not null,

                            constraint uq_categories_name unique (name)
);
