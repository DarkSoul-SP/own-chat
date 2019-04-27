delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
(1, 'user', '$2a$08$F6o5ACw6D5fNZpuLCtP1du1AjHYKPvf7SgDIIrdtahOSc5JZy2vt6', true),
(2, 'mike', '$2a$08$F6o5ACw6D5fNZpuLCtP1du1AjHYKPvf7SgDIIrdtahOSc5JZy2vt6', true);

insert into user_role(user_id, roles) values
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER');