CREATE TABLE IF NOT EXISTS FSM(ID INT PRIMARY KEY AUTO_INCREMENT,PARENT_ID INT,STATE VARCHAR(50),TRANSITIONS BLOB,DEF_VERSION INT NOT NULL,VERSION INT NOT NULL,STATUS INT NOT NULL,CREATED TIMESTAMP NOT NULL,MODIFIED TIMESTAMP NOT NULL)