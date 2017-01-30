# Script för att skapa NewtonBank databas

DROP SCHEMA IF EXISTS NewtonBank;
CREATE SCHEMA NewtonBank CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL ON NewtonBank.* TO root@localhost IDENTIFIED BY 'root';

USE NewtonBank;

CREATE TABLE Customer ( 
	personNr BIGINT UNSIGNED NOT NULL,
	name VARCHAR(30) NOT NULL,
	PRIMARY KEY(personNr)
) ENGINE=INNODB;

CREATE TABLE Account (
		accountId INT UNSIGNED NOT NULL AUTO_INCREMENT,
		personNr BIGINT UNSIGNED NOT NULL,
		accountType ENUM('SPARKONTO', 'KREDITKONTO') NOT NULL,
		rate DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0,
		balance DECIMAL(12, 2) NOT NULL DEFAULT 0,
		credit INT UNSIGNED,
		creditRate DECIMAL(4, 2) UNSIGNED,
		PRIMARY KEY(accountId),
	 	FOREIGN KEY (personNr) REFERENCES Customer(personNr) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=1001;

CREATE TABLE Transaction (
		transId INT UNSIGNED NOT NULL AUTO_INCREMENT,
		accountId INT UNSIGNED NOT NULL,
		transDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
		transType ENUM('IN', 'UT') NOT NULL,
		amount DECIMAL(12, 2) NOT NULL,
		balance DECIMAL(12, 2) NOT NULL,
		PRIMARY KEY(transId),
	 	FOREIGN KEY (accountId) REFERENCES Account(accountId) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=INNODB;


#********************* TEST DATA ***************************************************************

INSERT INTO `customer` (`personNr`,`name`) VALUES (199104213054,'Hampus Bohlin');
INSERT INTO `customer` (`personNr`,`name`) VALUES (199204213054,'Anna Nyugen');
INSERT INTO `customer` (`personNr`,`name`) VALUES (199304213054,'Åsa Nilsson');
INSERT INTO `customer` (`personNr`,`name`) VALUES (199404213054,'Cam Svensson');
INSERT INTO `customer` (`personNr`,`name`) VALUES (199504213054,'Hamid R');

INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1001,199104213054,'SPARKONTO',1.00,600.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1002,199204213054,'SPARKONTO',1.00,800.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1003,199304213054,'SPARKONTO',1.00,0.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1004,199404213054,'SPARKONTO',1.00,249.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1005,199504213054,'SPARKONTO',1.00,420.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1006,199504213054,'SPARKONTO',1.00,496.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1007,199404213054,'SPARKONTO',1.00,1250.00,NULL,NULL);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1009,199104213054,'KREDITKONTO',0.50,400.00,5000,7.00);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1010,199204213054,'KREDITKONTO',0.50,200.00,5000,7.00);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1011,199304213054,'KREDITKONTO',0.50,0.00,5000,7.00);
INSERT INTO `account` (`accountId`,`personNr`,`accountType`,`rate`,`balance`,`credit`,`creditRate`) VALUES (1013,199304213054,'SPARKONTO',1.00,0.00,NULL,NULL);

INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (1,1001,'2015-12-15 11:41:37','IN',500.00,500.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (2,1001,'2015-12-15 11:41:43','IN',300.00,800.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (3,1001,'2015-12-15 11:41:55','UT',-200.00,600.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (4,1009,'2015-12-15 11:42:31','IN',300.00,300.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (5,1009,'2015-12-15 11:42:37','IN',200.00,500.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (6,1009,'2015-12-15 11:42:46','UT',-50.00,450.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (7,1009,'2015-12-15 11:44:36','UT',-50.00,400.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (8,1002,'2015-12-15 11:45:50','IN',700.00,700.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (9,1002,'2015-12-15 11:45:57','IN',500.00,1200.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (10,1002,'2015-12-15 11:46:06','UT',-400.00,800.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (11,1010,'2015-12-15 11:46:30','IN',200.00,200.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (12,1010,'2015-12-15 11:46:42','IN',600.00,800.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (13,1010,'2015-12-15 11:46:52','UT',-600.00,200.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (14,1004,'2015-12-15 11:47:57','IN',400.00,400.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (15,1004,'2015-12-15 11:48:06','UT',-100.00,300.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (16,1004,'2015-12-15 11:48:14','UT',-50.00,249.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (17,1007,'2015-12-15 11:48:43','IN',350.00,350.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (18,1007,'2015-12-15 11:48:50','IN',700.00,1050.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (19,1007,'2015-12-15 11:48:55','IN',200.00,1250.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (20,1005,'2015-12-15 11:49:25','IN',120.00,120.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (21,1005,'2015-12-15 11:49:30','IN',800.00,920.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (22,1005,'2015-12-15 11:49:39','UT',-500.00,420.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (23,1006,'2015-12-15 11:49:54','IN',1000.00,1000.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (24,1006,'2015-12-15 11:50:07','UT',-300.00,700.00);
INSERT INTO `transaction` (`transId`,`accountId`,`transDate`,`transType`,`amount`,`balance`) VALUES (25,1006,'2015-12-15 11:50:12','UT',-200.00,496.00);

#********************* TEST CODE ***************************************************************

# testa skapa ny kund 12345
#insert into customer values (199501012222, 'Anna Nguey');

# testa skapa nytt konto till kund 12345
#insert into account (personNr, accountType, rate, balance, credit) values (199501012222, 'sparkonto', 5, 7500.50, 5000);
#insert into account (personNr, accountType, rate, balance, credit) values (199501012222, 'kreditkonto', 1, 3000, 5000);

# testa skapa ny transaction till konto 1001 och 1002 (kontonr genereta automatisk)
#insert into transaction (accountId, transType, amount, balance) values (1001, 'IN', 100, 7600.50);
#insert into transaction (accountId, transType, amount, balance) values (1001, 'UT', -2000, 5600.50);
#insert into transaction (accountId, transType, amount, balance) values (1002, 'IN', 200, 3200);
#insert into transaction (accountId, transType, amount, balance) values (1002, 'UT', -1000, 2200);

# testa ta bort transaction som tillhör konto 1001
#delete from transaction where accountId=1001;

# testa ta bort konto som tillhör kund 12345
#delete from account where personNr=12345;

# testa ta bort kund 12345
#delete from customer where personNr=12345;
