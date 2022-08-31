--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/referrer.csv' 
INTO TABLE referrer 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/insurer.csv' 
INTO TABLE insurer 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/mypaymentdetail.csv' 
INTO TABLE mypaymentdetail 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/client.csv' 
INTO TABLE client 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(clientid,referrerid,@effectivedate,@expirydate,premium,grosspremium,clienttype,insurerid,renewalstatus)
SET effectivedate = STR_TO_DATE(@effectivedate, '%d/%m/%Y'), expirydate = STR_TO_DATE(@expirydate, '%d/%m/%Y');

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/vehicle.csv' 
INTO TABLE vehicle 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(vehicleno,clientid,vmake,vmodel,@rdtaxexpiry)
SET rdtaxexpiry = STR_TO_DATE(@rdtaxexpiry,'%d/%m/%Y');
--SET rdtaxexpiry = STR_TO_DATE(case when rdtaxexpiry = null then null else rdtaxexpiry end, '%c/%e/%y' );

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/accidentrecord.csv' 
INTO TABLE accidentrecord 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(clientid,@doa,claimamount,remarks)
SET doa = STR_TO_DATE(@doa, '%d/%m/%Y');

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/referredby.csv' 
INTO TABLE referredby 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(clientid,referrerid,ramount,rpaymenttype,@rpaymentdate)
SET rpaymentdate = STR_TO_DATE(@rpaymentdate,'%d/%m/%Y');
--SET rpaymentdate = (CASE WHEN @rpaymentdate = NULL THEN NULL ELSE STR_TO_DATE(@rpaymentdate,'%c/%e/%Y'));

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/payment.csv' 
INTO TABLE payment 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(clientid,paymentid,@mypdate,myptype,@clientpdate,clientptype,amount)
SET mypdate = STR_TO_DATE(@mypdate, '%d/%m/%Y'),clientpdate = STR_TO_DATE(@clientpdate, '%d/%m/%Y');

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/individual.csv' 
INTO TABLE individual 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(clientid,ic,name,@dob,maritalstatus,occupation,nationality,drivingexp,address,homeno,phoneno,email)
SET dob = STR_TO_DATE(@dob, '%d/%m/%Y');

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/commercial.csv' 
INTO TABLE commercial 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/driver.csv' 
INTO TABLE driver 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(clientid,ic,name,@dob,maritalstatus,occupation,nationality,drivingexp)
SET dob = STR_TO_DATE(@dob, '%d/%m/%Y');

--OK
LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/relationship.csv' 
INTO TABLE relationship 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;