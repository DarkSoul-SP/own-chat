-- MySQL
-- create table user_subscriptions(
--  channel_id bigint not null references usr,
--  subscriber_id bigint not null references usr,
--  primary key (channel_id, subscriber_id)
-- ) engine=INNODB;

-- Pg
create table user_subscriptions (
    channel_id int8 not null references usr,
    subscriber_id int8 not null references usr,
    primary key (channel_id, subscriber_id)
)