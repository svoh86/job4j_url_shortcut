CREATE TABLE IF NOT EXISTS url
(
    id      SERIAL PRIMARY KEY,
    address TEXT   NOT NULL UNIQUE,
    code    TEXT   NOT NULL UNIQUE,
    count   BIGINT NOT NULL,
    site_id INT    NOT NULL REFERENCES site (id)
);

comment on table url is 'URL адрес';
comment on column url.id is 'Идентификатор URL';
comment on column url.address is 'адрес URL';
comment on column url.code is 'Уникальный код вместо URL';
comment on column url.count is 'Количество посещений данного URL';
comment on column url.site_id is 'Внешний ключ на сайт';