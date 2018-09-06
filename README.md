# Assembly Units Management
Small desktop project on Java about managing assembly units. Uses the JDBC and MySQL.

The app connects to DB on hosting by default. If you want to use a local MySQL server, you need to:
* Create new database named as assembly_units_db
* Create two tables using this code:
```
CREATE TABLE assembly_units (
	id INT NOT NULL AUTO_INCREMENT,
	parent_id INT NULL,
	PRIMARY KEY (id)
);

INSERT INTO assembly_units (parent_id)
VALUE (-1);

CREATE TABLE parts (
	name VARCHAR(30) NOT NULL,
	assembly_unit_id INT NOT NULL,
	amount INT NOT NULL,
	FOREIGN KEY (assembly_unit_id) REFERENCES assembly_units(id)
		ON DELETE CASCADE
);
```
* Set username as root and password as root123 (or change source code).
* Uncomment code for connecting to DB in assembly-units/src/ru/nikkozh/assemblyUnits/util/DBUtil.java
