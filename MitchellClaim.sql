use test;
drop table  if exists Claims;
drop table  if exists  LossInfoType;
drop table if exists  VehicleInfoType;

create table Claims(
Claim_id int primary key not null auto_increment,
ClaimNumber varchar(200) unique,
ClaimFirstName varchar(200),
ClaimLastName varchar(200),
Status varchar(10),
LossDate date,
AssignedAdjusterId varchar(10)
);

create table LossInfoType(

Loss_id int primary key not null auto_increment,
Claim_id varchar(200) references claims(claim_id),
CauseOfLossCode varchar(200),
ReportedDate date,
LossDescription varchar(200)
);

create table VehicleInfoType(
Vehicle_id int primary key not null auto_increment ,
Claim_id varchar(200) references claims(claim_id),
ModelYear integer(10),
MakeDescription varchar(200),
ModelDescription varchar(200),
EngineDescription varchar(200),
ExteriorColor varchar(200),
Vin varchar(200),
LicPlate varchar(200),
LicPlateState varchar(200),
DamageDescription varchar(200),
LicPlateExpDate date,
Mileage integer(10)
);

/* insert some dummy data for testing purpose*/
insert into Claims(ClaimNumber,ClaimFirstName,ClaimLastName,Status,LossDate,AssignedAdjusterId) values('abcd','Paras','Patel','OPEN','2014-2-21',1254);
insert into LossInfoType(Claim_id,CauseOfLossCode,ReportedDate,LossDescription) values(1,'FIRE','2015-01-10','Hit the tree');
insert into VehicleInfoType(Claim_id,ModelYear,MakeDescription,ModelDescription,EngineDescription,ExteriorColor,Vin,LicPlate,LicPlateState,DamageDescription,LicPlateExpDate,Mileage) values(1,2015,'Ferari','x2','v2x','Blue','!MNBDH','PP','state','Headlight damaged','2011-8-9',10245);


select * from LossInfoType;



