create table public.products
(
    id          varchar(255)   not null
        primary key,
    active      boolean        not null,
    created_at  timestamp(6)   not null,
    category    varchar(20)    not null
        constraint products_category_check
            check ((category)::text = ANY
        ((ARRAY [
        'PLANT'::character varying,
        'TOOL'::character varying,
        'FERTILIZER'::character varying,
        'SOIL'::character varying,
        'ACCESSORY'::character varying])::text[])),
    name        varchar(255)   not null,
    price       numeric(10, 2) not null,
    stock       integer        not null,
    description text           not null
);



