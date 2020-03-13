-- You cannot drop table if a view depends on it:
drop view report if exists;
drop table meta if exists;
drop table data if exists;

-- CSVREAD() is an H2 Feature and can also read from the classpath:
create table meta (x integer, y integer, z integer)
as select * from csvread('classpath:meta.csv');

create table data (x integer, yt varchar, zt varchar)
as select * from csvread('classpath:data.csv');

create view report
as select m.x, m.y, m.z, d.yt, d.zt
from meta m
join data d
on m.x = d.x;

