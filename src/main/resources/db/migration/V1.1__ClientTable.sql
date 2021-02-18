CREATE TABLE IF NOT EXISTS Client(
     id INT,
     user_id INT NOT NULL,
     first_name VARCHAR (100) NOT NULL,
     last_name VARCHAR (100) NOT NULL,
     email_address VARCHAR (50) NOT NULL,
     phone_number VARCHAR (15) NOT NULL,
     gender VARCHAR (5) NOT NULL,
     delivery_address VARCHAR (100) NOT NULL
);

-- CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;


