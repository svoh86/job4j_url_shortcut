CREATE TABLE IF NOT EXISTS site
(
    id           SERIAL PRIMARY KEY,
    domain       TEXT NOT NULL UNIQUE,
    login        TEXT NOT NULL,
    password     TEXT NOT NULL,
    registration BOOLEAN DEFAULT false
);

comment on table site is 'Сайт';
comment on column site.id is 'Идентификатор сайта';
comment on column site.domain is 'Домен сайта';
comment on column site.login is 'Логин сайта';
comment on column site.password is 'Пароль сайта';
comment on column site.registration is 'Флаг регистрации сайта';