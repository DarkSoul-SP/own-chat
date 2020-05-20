-- MySQL
-- delete from message;
-- delete from hibernate_sequence;
--
-- insert into message(id, text, tag, user_id) values
-- (1, 'first', 'my-tag', 1),
-- (2, 'second', 'more', 1),
-- (3, 'third', 'my-tag', 1),
-- (4, 'fourth', 'another', 2);
--
-- INSERT INTO hibernate_sequence (next_val) VALUES(10);

-- Pg
delete from message;

insert into message(id, text, tag, user_id) values
(1, 'first', 'my-tag', 1),
(2, 'second', 'more', 1),
(3, 'third', 'my-tag', 1),
(4, 'fourth', 'another', 2);

alter sequence hibernate_sequence restart with 10;