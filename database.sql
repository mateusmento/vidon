drop database if exists vidon;
create database vidon;
use vidon;

drop user vidon@localhost;
create user vidon@localhost identified by 'asmp32hj26';
grant all privileges on *.* to vidon@localhost;

create table users (
	id int not null auto_increment,
	name varchar(64) not null,
	username varchar(32) not null,
	password varchar(32) not null,
	primary key (id),
	unique key (username)
);
