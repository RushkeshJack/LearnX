create database LearnX;

create table admindata(
Id int,
email varchar(100),
password varchar(8)
);

insert into admindata(Id, email, password)
values(1, 'rk@gmail.com', '12345678');

use learnx;
select * from userdata;
select * from userdatail;
select * from admindata;

select * from userdata where email='rk@gmail.com';

create table userdata(
Id int,
name varchar(100),
email varchar(100),
password varchar(8)
);

desc userdata;
desc admindata;

insert into userdata(id,email, name, password,job_level,office,role,unit)
values(10101,'rk@gmail.com','Rushikesh khandekar', '11111111','JL3','pune1','Mainframe developer','Healthcare');



update userdata set unit='Application Development' where id=10102;

ALTER TABLE userdata
ADD CONSTRAINT email UNIQUE (email);


update userdata SET name='Rushikesh khandekar' where name='Rushikesh khandeakr';

Alter table userdata  MODIFY column password varchar(60);

delete from userdata where id=10118;
delete from admindata;
drop table userdata;

select * from userdata where access=1;