-- -----------------------------------------------------
-- Create User and GRANT ACCESS
-- -----------------------------------------------------
CREATE USER 'trippy2'@'localhost' IDENTIFIED BY 'trippy2';
GRANT ALL ON trippy2.* TO 'trippy2'@'localhost';

