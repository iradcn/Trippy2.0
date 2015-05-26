-- -----------------------------------------------------
-- Create User and GRANT ACCESS
-- -----------------------------------------------------
CREATE USER 'DbMysql11'@'localhost' IDENTIFIED BY 'DbMysql11';
GRANT ALL ON DbMysql11.* TO 'DbMysql11'@'localhost';

