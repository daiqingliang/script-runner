## DDL
create table daiql_test (
    id int,
    name varchar(100)
);

## DML
## should roll back
insert into daiql_test (id, name) value (1, 'test1');
insert into daiql_test (id, name) value (2, 'test2');
update daiql_test set name = 'test2' where id = 1;

## Error sql
god bless no bug !
