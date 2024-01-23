drop database if exists zeuzGym;
create database if not exists zeuzGym;

use zeuzGym;

create table user(
 userId int(5) not null primary key,
 passsword varchar(12),
 userName varchar(40) not null,
 contact varchar(10) not null,
 role varchar (30)
 );

create table member(
 memberId varchar(5)unique not null primary key,
 name varchar(40),
 age int not null,
 gender varchar(10),
 contact int(10) not null
 );

create table membership(
 membershipId varchar(5) not null primary key,
 memberId varchar(5),
 type varchar(30),
 amount decimal(10,2),
 date date,
 sigunupFee decimal(10,2),
 constraint foreign key (memberId) references member (memberId)
 );

create table payment(
  paymentId varchar(5)primary key,
  memberId varchar(5),
  date date,
  amount decimal(10,2),
  constraint foreign key(memberId) references member(memberId)
  on delete cascade on update cascade
  );

create table employee(
  employeeId varchar(10) not null primary key,
  role varchar(30),
  name varchar(40)not null,
  salary decimal(10,2),
  contact varchar(10) not null
  );

create table service(
   serviceId varchar(5)primary key,
   employeeId varchar(10),
   fee decimal(10,2),
   serviceType varchar(40),
   constraint foreign key(employeeId) references employee(employeeId)
   );

create table appoinment(
   appoinmentId varchar(10)not null primary key,
   date date,
   memberId varchar(5),
   time varchar(10),
   description varchar(40),
   constraint foreign key(memberId) references member(memberId)
   );

create table workoutPlan(
   planId varchar(5) not null primary key,
   memberId varchar(5),
   description varchar(40),
   employeeId varchar(10),
   constraint foreign key(memberId) references member (memberId),
   constraint foreign key(employeeId) references employee (employeeId)
   );

create table equipment(
   equipmentId varchar(5) not null primary key,
   equName varchar(25),
   memberId varchar(5),
   type varchar(20),
   constraint foreign key (memberId) references member(memberId)
   on delete cascade on update cascade
    );

create table employeeAssignDetail(
    serviceId varchar(5),
    employeeId varchar(10),
    constraint foreign key(serviceId)references service (serviceId),
    constraint foreign key(employeeId)references employee(employeeId)
    );

create table memberWorkoutPlanDetils(
   memberId varchar(5),
   planId varchar(5),
   constraint foreign key(memberId)references member (memberId),
   constraint foreign key(planId)references workoutPlan(planId)
   );
   desc customer;













