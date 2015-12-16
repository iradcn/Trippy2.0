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
DROP SCHEMA IF EXISTS `trippy2` ;

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
  `Id` INT(10) UNSIGNED NOT NULL,
  `Name` VARCHAR(45) NULL DEFAULT NULL,
  `Presentation_Name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Id_UNIQUE` (`Id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`places`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`places` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`places` (
  `n_id` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(200) NOT NULL,
  `Lat` DOUBLE NOT NULL,
  `Lon` DOUBLE NOT NULL,
  `Id` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`n_id`),
  UNIQUE INDEX `Id_UNIQUE` (`Id` ASC),
  UNIQUE INDEX `nId_UNIQUE` (`n_id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 53654
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`places_categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`places_categories` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`places_categories` (
  `PlaceId` VARCHAR(200) NOT NULL,
  `CategoryId` INT(10) NOT NULL,
  INDEX `IX_CATEGORY_ID` (`CategoryId` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trippy2`.`places_categories_bin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trippy2`.`places_categories_bin` ;

CREATE TABLE IF NOT EXISTS `trippy2`.`places_categories_bin` (
  `placeId` INT(11) NOT NULL,
  `categoryId` INT(10) NOT NULL,
  `vote` INT(1) NULL DEFAULT '1')
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
AUTO_INCREMENT = 24
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
  `is_opened` INT(1) NOT NULL DEFAULT '1',
  `nPlaceId` INT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

USE `trippy2` ;

-- -----------------------------------------------------
-- Placeholder table for view `trippy2`.`places_props_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trippy2`.`places_props_view` (`placeId` INT, `propId` INT, `nPlaceid` INT, `rank` INT, `rank_bin` INT);

-- -----------------------------------------------------
-- Placeholder table for view `trippy2`.`user_votes_agg_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trippy2`.`user_votes_agg_view` (`placeId` INT, `propId` INT, `votesRank` INT);

-- -----------------------------------------------------
-- View `trippy2`.`places_props_view`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `trippy2`.`places_props_view` ;
DROP TABLE IF EXISTS `trippy2`.`places_props_view`;
USE `trippy2`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`trippy2`@`localhost` SQL SECURITY DEFINER VIEW `trippy2`.`places_props_view` AS select `trippy2`.`uservotes`.`placeId` AS `placeId`,`trippy2`.`uservotes`.`propId` AS `propId`,`trippy2`.`uservotes`.`nPlaceId` AS `nPlaceid`,sum(`trippy2`.`uservotes`.`vote`) AS `rank`,(case when (sum(`trippy2`.`uservotes`.`vote`) > 0) then 1 else 0 end) AS `rank_bin` from `trippy2`.`uservotes` group by `trippy2`.`uservotes`.`placeId`,`trippy2`.`uservotes`.`propId`,`trippy2`.`uservotes`.`nPlaceId` having (sum(`trippy2`.`uservotes`.`vote`) > 0);

-- -----------------------------------------------------
-- View `trippy2`.`user_votes_agg_view`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `trippy2`.`user_votes_agg_view` ;
DROP TABLE IF EXISTS `trippy2`.`user_votes_agg_view`;
USE `trippy2`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`trippy2`@`localhost` SQL SECURITY DEFINER VIEW `trippy2`.`user_votes_agg_view` AS select `trippy2`.`uservotes`.`nPlaceId` AS `placeId`,`trippy2`.`uservotes`.`propId` AS `propId`,(case when (sum(`trippy2`.`uservotes`.`vote`) > 0) then 1 else 0 end) AS `votesRank` from `trippy2`.`uservotes` group by `trippy2`.`uservotes`.`nPlaceId`,`trippy2`.`uservotes`.`propId` union select `trippy2`.`places_categories_bin`.`placeId` AS `placeId`,`trippy2`.`places_categories_bin`.`categoryId` AS `propId`,`trippy2`.`places_categories_bin`.`vote` AS `votesRank` from `trippy2`.`places_categories_bin`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


USE `trippy2`;
insert into categories (`name`, `id`,`Presentation_Name`)
values("airport",1,"Airport"),
("amusement_park",2,"Amusement Park"),
("aquarium",3, "Aquarium"),
("art_gallery",4, "Art Gallery"),
("bakery",5, "Bakery"),
("bar",6, "Bar"),
("cafe",7, "Cafe"),
("casino",8,"Casino"),
("clothing_store",9,"Clothing Store"),
("convenience_store",10, "Convenience Store"),
("department_store",11, "Department Store"),
("food",12, "Food"),
("grocery_or_supermarket",13, "Grocery Or Superkmarket"),
("gym",14, "Gym"),
("health",15, "Health"),
("movie_theater",16, "Movie Theatre"),
("museum",17, "Museum"),
("night_club",18, "Night Club"),
("park",19, "Park"),
("restaurant",20, "Restaurant"),
("shopping_mall",21, "Shopping Mall"),
("spa",22, "Spa"),
("zoo",23, "Zoo");

USE `trippy2`;
INSERT INTO properties (`name`) VALUES("Dog Friendly"),("For Students"),("For Famillies"),("Religious"),("First Date"),("Beautiful Girls");

