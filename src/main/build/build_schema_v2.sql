-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema trippy2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema trippy2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trippy2` DEFAULT CHARACTER SET utf8 ;
USE `trippy2` ;

-- -----------------------------------------------------
-- Table `trippy2`.`categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`categories` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`categories` (
  `Id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Id_UNIQUE` (`Id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 24
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`places`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`places` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`places` (
  `Name` VARCHAR(200) NOT NULL,
  `Lat` DOUBLE NOT NULL,
  `Lon` DOUBLE NOT NULL,
  `Id` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `YagoId_UNIQUE` (`Id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`places_categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`places_categories` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`places_categories` (
  `PlaceId` VARCHAR(200) NOT NULL,
  `CategoryId` INT(10) NOT NULL,
  INDEX `IX_CATEGORY_ID` (`CategoryId` ASC),
  INDEX `FK_PLACE_ID_idx` (`PlaceId` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`places_props`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`places_props` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`places_props` (
  `PlaceId` VARCHAR(200) NOT NULL,
  `PropId` INT(10) UNSIGNED NOT NULL,
  `yesVotes` INT(20) NULL DEFAULT NULL,
  `noVotes` INT(20) NULL DEFAULT NULL,
  INDEX `FK_PLACE_ID` (`PlaceId` ASC),
  INDEX `FK_PROP_ID` (`PropId` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`properties`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`properties` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`properties` (
  `Id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Id_UNIQUE` (`Id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`user_roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`user_roles` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`user_roles` (
  `user_id` VARCHAR(40) NOT NULL,
  `role` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`users` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`users` (
  `user_id` VARCHAR(40) NOT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `access_token` VARCHAR(500) NULL DEFAULT NULL,
  `enabled` TINYINT(4) NULL DEFAULT '1',
  `userscol` VARCHAR(45) NULL DEFAULT NULL,
  `sent_data_counter` INT(20) UNSIGNED NULL DEFAULT '0',
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`users_check_in`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`users_check_in` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`users_check_in` (
  `user_id` VARCHAR(40) NOT NULL,
  `place_id` VARCHAR(200) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`users_search_props_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`users_search_props_history` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`users_search_props_history` (
  `user_id` VARCHAR(40) NOT NULL,
  `prop_id` INT(10) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`uservotes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`uservotes` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`uservotes` (
  `userId` VARCHAR(40) NOT NULL,
  `placeId` VARCHAR(40) NOT NULL,
  `propId` INT(11) NOT NULL,
  `vote` INT(11) NOT NULL,
  `fTimestamp` VARCHAR(45) NOT NULL,
  `is_opened` BINARY(1) NOT NULL DEFAULT '0',
  `nPlaceId` INT(11) NOT NULL,
  PRIMARY KEY (`nPlaceId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

USE `trippy2` ;

-- -----------------------------------------------------
-- Placeholder table for view `trippy2`.`places_props_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trippy2`.`places_props_view` (`placeId` INT, `propId` INT);

-- -----------------------------------------------------
-- Placeholder table for view `trippy2`.`user_votes_agg_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trippy2`.`user_votes_agg_view` (`placeId` INT, `propId` INT, `votesRank` INT, `max(fTimestamp)` INT);

-- -----------------------------------------------------
-- procedure CreateForeignKeys
-- -----------------------------------------------------

USE `trippy2`;
DROP procedure IF EXISTS `trippy2`.`CreateForeignKeys`;

DELIMITER $$
USE `trippy2`$$
CREATE DEFINER=`trippy2`@`localhost` PROCEDURE `CreateForeignKeys`()
BEGIN

    ALTER TABLE placesprops
	ADD CONSTRAINT FK_PLACE_ID
	FOREIGN KEY (PlaceId)
	REFERENCES places(Id);

    ALTER TABLE placesprops
	ADD CONSTRAINT FK_PROP_ID
	FOREIGN KEY (PropId)
	REFERENCES properties(Id);

    ALTER TABLE placescategories
	ADD CONSTRAINT FK_CATEGORY_ID_
	FOREIGN KEY (CategoryId)
	REFERENCES categories(YagoId);

    ALTER TABLE placescategories
	ADD CONSTRAINT FK_PLACE_ID_
	FOREIGN KEY (PlaceId)
	REFERENCES places(Id);

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure RemoveForeignKeys
-- -----------------------------------------------------

USE `trippy2`;
DROP procedure IF EXISTS `trippy2`.`RemoveForeignKeys`;

DELIMITER $$
USE `trippy2`$$
CREATE DEFINER=`trippy2`@`localhost` PROCEDURE `RemoveForeignKeys`()
BEGIN

    ALTER TABLE placesprops
	  DROP FOREIGN KEY FK_PLACE_ID;


    ALTER TABLE placesprops
	  DROP FOREIGN KEY FK_PROP_ID;


    ALTER TABLE placescategories
	  DROP FOREIGN KEY FK_CATEGORY_ID_;

    ALTER TABLE placescategories
	  DROP FOREIGN KEY FK_PLACE_ID_;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `trippy2`.`places_props_view`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `trippy2`.`places_props_view` ;
DROP TABLE IF EXISTS `trippy2`.`places_props_view`;
USE `trippy2`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`trippy2`@`localhost` SQL SECURITY DEFINER VIEW `trippy2`.`places_props_view` AS select `trippy2`.`uservotes`.`placeId` AS `placeId`,`trippy2`.`uservotes`.`propId` AS `propId` from `trippy2`.`uservotes` group by `trippy2`.`uservotes`.`placeId`,`trippy2`.`uservotes`.`propId` having (sum(`trippy2`.`uservotes`.`vote`) > 0);

-- -----------------------------------------------------
-- View `trippy2`.`user_votes_agg_view`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `trippy2`.`user_votes_agg_view` ;
DROP TABLE IF EXISTS `trippy2`.`user_votes_agg_view`;
USE `trippy2`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`trippy2`@`localhost` SQL SECURITY DEFINER VIEW `trippy2`.`user_votes_agg_view` AS select `trippy2`.`uservotes`.`placeId` AS `placeId`,`trippy2`.`uservotes`.`propId` AS `propId`,sum(`trippy2`.`uservotes`.`vote`) AS `votesRank`,max(`trippy2`.`uservotes`.`fTimestamp`) AS `max(fTimestamp)` from `trippy2`.`uservotes` group by `trippy2`.`uservotes`.`placeId`,`trippy2`.`uservotes`.`propId`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
