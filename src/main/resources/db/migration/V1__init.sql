CREATE TABLE users (
  user_id bigint NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  type varchar(8),
  PRIMARY KEY (user_id)
);