SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS item;




/* Create Tables */

CREATE TABLE item
(
	id int unsigned NOT NULL AUTO_INCREMENT,
	name varchar(10),
	price int,
	version int unsigned,
	PRIMARY KEY (id)
);



