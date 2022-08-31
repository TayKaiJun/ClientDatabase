-- Update payment amount
/*******************************************************************
EXPLANATION
When a client's gross premium is updated, the new amount will need
to be paid for. Hence, a trigger can be used to update the new 
amount to be paid in the payment table 
*******************************************************************/

CREATE TRIGGER paymentUpdate
AFTER UPDATE ON client
FOR EACH ROW
UPDATE payment
SET amount = NEW.grosspremium
WHERE clientid = NEW.clientid;



-- Stored procedure for getting expiring policies
/*******************************************************************
EXPLANATION
Calling this stored procedure and entering the date range allows the
user to filter the policies based on the expiring date such that 
details of the policy wil be shown if its expiring date is within 
the entered date range.
*******************************************************************/

DELIMITER //
CREATE PROCEDURE SHOWEXPIRING(IN currentDate DATE ,IN target DATE)
	BEGIN
		SELECT DISTINCT c.clientid as ClientID, vehicleno as "VehicleNo.", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as "Effective Date", c.expirydate as "Expiry Date"
		FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t
		WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND 
		CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END AND c.expirydate > currentDate AND c.expirydate < target;
	END//
DELIMITER ;




-- Query 1: Retrieves all the payment details related to each client
/*******************************************************************
EXPLANATION
Selects all payment details for each client, even if there is null 
values, such as if the client has no referrer.

Since doing a query without considering the null cases will cause
data to not be shown, a query that doesnt have the null values will
need to be merged with one that contains the values even if there 
are null values
*******************************************************************/

-- Query 1: Retrieves all the payment details related to each client
SELECT DISTINCT t.name as Name, grosspremium, insurername, 
CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, 
CASE WHEN rpaymentdate IS NULL THEN NULL ELSE rpaymentdate END as ReferrerPDate, 
CASE WHEN rpercent IS NULL THEN NULL ELSE rpercent END as Rcomm,
CASE WHEN mypdate IS NULL THEN NULL ELSE mypdate END as MyPaymentDate, 
CASE WHEN clientpdate IS NULL THEN NULL ELSE clientpdate END as ClientPaymentDate

FROM client c, insurer, referredby, payment, referrer, 
(SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t

WHERE c.clientid = t.clientid AND c.insurerid = insurer.insurerid AND
CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END AND
c.clientid = referredby.clientid AND c.clientid = payment.clientid AND grosspremium IS NOT NULL

UNION

SELECT DISTINCT t.name as Name, grosspremium, insurername, 
CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, 
CASE WHEN c.referrerid IS NULL THEN NULL ELSE rpaymentdate END as ReferrerPaymentDate, 
CASE WHEN c.referrerid IS NULL THEN NULL ELSE rpercent END as Rcomm, 
CASE WHEN mypdate IS NULL THEN NULL ELSE mypdate END as MyPaymentDate, 
CASE WHEN clientpdate IS NULL THEN NULL ELSE clientpdate END as ClientPaymentDate

FROM client c, insurer, referredby, payment, referrer, 
(SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t

WHERE c.clientid = t.clientid AND c.insurerid = insurer.insurerid AND c.referrerid IS NULL AND c.clientid = payment.clientid AND grosspremium IS NOT NULL;




-- Query 2: Query proportion of driving exp wrt their age
/*******************************************************************
EXPLANATION
Selects everyone who can drive from the individual table (main 
insured for private vehicle policies) and driver table (every other 
named drivers), calculate their age and calculate the proportion of
driving experience with respect to their age
*******************************************************************/

-- Query 2: Query proportion of driving exp wrt their age
SELECT name as Name, DATEDIFF('2017-04-16', dob) / 365.25 AS Age, drivingexp as "Years of Driving Exp" , drivingexp/(DATEDIFF('2017-04-16', dob) / 365.25)*100 AS "Driving Exp : Age ratio"
FROM individual
WHERE drivingexp > 0
UNION
SELECT name as Name, DATEDIFF('2017-04-16', dob) / 365.25 AS Age, drivingexp as "Years of Driving Exp" , drivingexp/(DATEDIFF('2017-04-16', dob) / 365.25)*100 AS "Driving Exp : Age ratio"
FROM driver
WHERE drivingexp > 0;




-- Query 3: Retrieve companies who purchased insurance for their chinese drivers
/*******************************************************************
EXPLANATION
Search for commercial clients who bought insurance for drivers from
China
*******************************************************************/
-- Query 3: Retrieve companies who purchased insurance for their chinese drivers
SELECT companyname, name, maritalstatus, nationality, occupation
FROM commercial, driver
WHERE driver.clientid = commercial.clientid AND nationality = "China" AND occupation = "Driver";




-- Query 4: Selects clients who are related to someone who works as Design Engineer
/*******************************************************************
EXPLANATION
Selects all payment details for each client, even if there is null 
values, such as if the client has no referrer.

Since doing a query without considering the null cases will cause
data to not be shown, a query that doesnt have the null values will
need to be merged with one that contains the values even if there 
are null values
*******************************************************************/
-- Query 4: Selects clients who are related to someone who works as Design Engineer
SELECT t.name as "Client Name", driver.name as "Named Driver", occupation as "Named Driver's Occupation"
FROM driver, (SELECT clientid, name FROM individual 
			  UNION 
			  SELECT clientid, companyname FROM commercial) t
WHERE driver.clientid = t.clientid AND t.clientid IN (SELECT clientid 
													  FROM driver 
													  WHERE occupation = "Design Engineer");




