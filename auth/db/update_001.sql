create table person (
   id serial primary key not null,
   login varchar(2000),
   password varchar(2000),
   role_id serial not null references role(id)
);

create table employee (
	id serial primary key not null,
	date_emp timestamp,
	inn varchar(20),
	name varchar(30),
	secondname varchar(30)
);

create table message (
	id serial primary key not null,
	text varchar(255),
	time timestamp
);

create table room (
	id serial primary key not null,
	name varchar(30),
	description varchar(100),
	time timestamp,
	person_creater serial not null references person(id)
);

CREATE TABLE role (
  id serial primary key,
  authority VARCHAR(50) NOT NULL unique
);

create table employee_accounts (
	employee_id serial not null,
	accounts_id serial not null unique,
	foreign key (employee_id) references employee(id),
	foreign key (accounts_id) references person(id)
);

create table person_messages (
	person_id serial not null,
	messages_id serial not null,
	primary key (person_id, messages_id),
	foreign key (person_id) references person(id),
	foreign key (messages_id) references message(id)
);

create table room_messages (
	room_id serial not null,
	messages_id serial not null,
	primary key (room_id, messages_id),
	foreign key (room_id) references room(id),
	foreign key (messages_id) references message(id)
);

insert into person (login, password, role_id) values ('parsentev', '123', 1);
insert into person (login, password, role_id) values ('ban', '123', 1);
insert into person (login, password, role_id) values ('ivan', '123', 1);

insert into role (authority) values ('ROLE_USER');
insert into role (authority) values ('ROLE_ADMIN');

insert into employee(date_emp, inn, "name", secondname) 
values('2020-12-26 18:20:34.859', '1', '1', '1');

insert into employee_accounts(employee_id, accounts_id) values(1, 1);
