DROP TABLE IF EXISTS mysql_reader_writer_test;
DROP TABLE IF EXISTS mysql_reader_writer_test_1;

CREATE TABLE mysql_reader_writer_test (
  dhid INT AUTO_INCREMENT PRIMARY KEY,
  uid INT,
  name VARCHAR(255),
  description VARCHAR(255),
  ip CHAR(16),
  port INT,
  username VARCHAR(255),
  password VARCHAR(255),
  created_on TIMESTAMP default CURRENT_TIMESTAMP,
  modified_on DATETIME
)DEFAULT CHARSET=utf8mb4;


