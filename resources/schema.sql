-- CSVREAD() is an H2 Feature and can also read from the classpath:
drop table meta if exists;
create table meta (x integer, y integer, z integer)
as select * from csvread('classpath:meta.csv');

drop table zyx if exists;
create table zyx (x integer, y integer, z integer);

