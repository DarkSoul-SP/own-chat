-- MySQL
-- insert into hibernate_sequence values ( 1 );
--
-- insert into usr(id, username, password, active, email)
--   values (
--     1,
--     'admin',
--     '$2a$08$bxbg/6gMSIOTyRMYXyIM3OWvdb7QgCrlqGJZ4aeD6wzkK/m4hJoia',
--     true,
--     'dsoul2981@gmail.com'
--   );
--
-- insert into user_role(user_id, roles)
--   values (1, 'USER'), (1, 'ADMIN');

-- Pg
insert into usr (id, username, password, active, email)
    values (1, 'admin', 'qwerty123', true, 'dsoul2981@gmail.com');

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');

create extension if not exists pgcrypto;

update usr set password = crypt(password, gen_salt('bf', 8));