USE delegator;

create table users (
user_name varchar(100) primary key,
pass varchar(50),
phone varchar(50),
image blob
);


create table groups(
group_id integer primary key auto_increment,
group_name varchar(300),
group_owner integer references users(user_name)
);

create table user_to_groups(
user_name varchar(100) references users(user_name),
group_id integer references groups(group_id),
primary key (user_name, group_id)
);

create table subordinates(
manager varchar(100) references users(user_name),
subordinate varchar(100) references users(user_name),
primary key (manager, subordinate)
);

create table tasks(
id integer primary key auto_increment,
reporter varchar(100) references users(user_name),
assignee varchar(100) references users(user_name),
`status` varchar(50),
description text,
priority integer,
`completion` integer,
start_date varchar(200),
deadline varchar(200),
title varchar(500)
);
