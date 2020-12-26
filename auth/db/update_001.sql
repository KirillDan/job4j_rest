create table person (
   id serial primary key not null,
   login varchar(2000),
   password varchar(2000)
);

create table employee (
	id serial primary key not null,
	date_emp timestamp,
	inn varchar(20),
	name varchar(30),
	secondname varchar(30)
);

create table employee_accounts (
	employee_id serial not null,
	accounts_id serial not null unique,
	foreign key (employee_id) references employee(id),
	foreign key (accounts_id) references person(id)
);


insert into person (login, password) values ('parsentev', '123');
insert into person (login, password) values ('ban', '123');
insert into person (login, password) values ('ivan', '123');

insert into employee(date_emp, inn, "name", secondname) 
values('2020-12-26 18:20:34.859', '1', '1', '1');

insert into employee_accounts(employee_id, accounts_id) values(1, 1);