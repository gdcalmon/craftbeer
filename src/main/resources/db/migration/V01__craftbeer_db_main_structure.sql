CREATE TABLE IF NOT EXISTS beer (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(256) NOT NULL,
	ingredients TEXT NOT NULL,
	alcohol_content VARCHAR(32) NOT NULL,
	price DECIMAL(10,2) NOT NULL,
	category VARCHAR(256) NOT NULL,
	PRIMARY KEY (id)
);
