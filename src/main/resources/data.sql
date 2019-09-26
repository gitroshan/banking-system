insert into customer values(10001,'Roshan', 'E1234567');
insert into customer values(10002,'Achini', 'A1234568');

insert into account values(10001,'000000000001', 0.00, false);
insert into account values(10002,'000000000002', 0.00, true);

insert into account_customer values(10001, 10001);
insert into account_customer values(10002, 10002);
insert into account_customer values(10002, 10001);