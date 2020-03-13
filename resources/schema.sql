-- CSVREAD() is an H2 Feature and can also read from the classpath:
drop table meta if exists;
create table meta (x integer, y integer, z integer)
as select * from csvread('classpath:meta.csv');

drop table data if exists;
create table data (x integer, yt varchar, zt varchar)
as select * from csvread('classpath:data.csv');

