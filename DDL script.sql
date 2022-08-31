DROP DATABASE CLIENTS;
CREATE DATABASE CLIENTS;
USE CLIENTS;

CREATE TABLE client (
  clientid          integer(5) ,
  referrerid        integer(5) ,
  effectivedate     date , 
  expirydate        date ,
  premium           float ,
  grosspremium      float ,
  clienttype        integer(1) ,
  insurerid         integer(5) ,
  renewalstatus     integer(1) ,
  primary key (clientid)
);
 
CREATE TABLE vehicle (
  vehicleno         varchar(10) ,
  clientid          integer(5) ,
  vmake             varchar(25) ,
  vmodel            varchar(50) ,
  rdtaxexpiry       date ,
  primary key (vehicleno) ,
  constraint vehiclefk foreign key (clientid) references client(clientid) on update cascade on delete cascade
);

CREATE TABLE insurer (
  insurerid         integer(5)  ,
  insurername       varchar(80) ,
  address           varchar(80) ,
  officeno          integer(8)  ,
  website           varchar(100) ,
  primary key (insurerid)
);

CREATE TABLE mypaymentdetail (
  typeid            integer(3) ,
  accountno            varchar(25) ,
  accounttype          varchar(25) ,
  primary key (typeid)
);

CREATE TABLE referrer (
  referrerid        integer(5) ,
  referrername      varchar(25) ,
  rpercent          float ,
  primary key (referrerid)
);

CREATE TABLE referredby (
  clientid          integer(5) ,
  referrerid        integer(5) ,
  ramount           float ,
  rpaymenttype      integer(3) ,
  rpaymentdate      date ,
  primary key (clientid, referrerid) ,
  constraint referredby1 foreign key (clientid) references client(clientid) on update cascade on delete cascade ,
  constraint referredby2 foreign key (referrerid) references referrer(referrerid) on update cascade on delete cascade ,
  constraint referredby3 foreign key (rpaymenttype) references mypaymentdetail(typeid) on update cascade on delete cascade
);

alter table client add constraint clientfk1 foreign key (referrerid) references referrer(referrerid) on update cascade on delete cascade;
alter table client add constraint clientfk2 foreign key (insurerid) references insurer(insurerid) on update cascade on delete cascade;

CREATE TABLE accidentrecord (
  clientid          integer(5) ,
  doa               date ,
  claimamount       float ,
  remarks           text ,
  primary key (clientid , doa) ,
  constraint accidentfk foreign key (clientid) references client(clientid) on update cascade on delete cascade
);

CREATE TABLE payment (
  clientid          integer(5) ,
  paymentid         integer(5) ,
  mypdate           date ,
  myptype           integer(3) ,
  clientpdate       date ,
  clientptype       varchar(25) ,
  amount            float ,
  primary key (paymentid) ,
  constraint paymentfk1 foreign key (clientid) references client(clientid) on update cascade on delete cascade ,
  constraint paymentfk2 foreign key (myptype) references mypaymentdetail(typeid) on update cascade on delete cascade
);

CREATE TABLE individual (
  clientid          integer(5) ,
  ic                varchar(15) UNIQUE ,
  name              varchar(25) ,
  dob               date ,
  maritalstatus     varchar(10) ,
  occupation        varchar(25) ,
  nationality       varchar(20) ,
  drivingexp        integer(5) ,
  address           varchar(80) ,
  homeno            integer(8) ,
  phoneno           integer(8) ,
  email             varchar(50) ,
  primary key (clientid) ,
  constraint clientchild1 foreign key (clientid) references client(clientid) on update cascade on delete cascade
);

CREATE TABLE commercial (
  clientid          integer(5) ,
  roc               varchar(15) UNIQUE ,
  companyname       varchar(80) ,
  pic_name          varchar(25) ,
  pic_phoneno       integer(8) ,
  address           varchar(80) ,
  officeno          integer(8) ,
  email             varchar(50) ,
  primary key (clientid) ,
  constraint clientchild2 foreign key (clientid) references client(clientid) on update cascade on delete cascade
);

CREATE TABLE driver (
  clientid          integer(5) ,
  ic                varchar(15) ,
  name              varchar(25) ,
  dob               date ,
  maritalstatus     varchar(10) ,
  occupation        varchar(25) ,
  nationality       varchar(20) ,
  drivingexp        integer(5) ,
  primary key (ic) ,
  constraint driverfk foreign key (clientid) references client(clientid) on update cascade on delete cascade
);

CREATE TABLE relationship (
  clientid          integer(5) ,
  ic                varchar(15) ,
  relationship      varchar(25) ,
  primary key (clientid , ic) ,
  constraint relationfk1 foreign key (clientid) references individual(clientid) on update cascade on delete cascade ,
  constraint relationfk2 foreign key (ic) references driver(ic) on update cascade on delete cascade
);





