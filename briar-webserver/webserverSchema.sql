-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema briar_server
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `briar_server` ;

-- -----------------------------------------------------
-- Schema briar_server
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `briar_server` DEFAULT CHARACTER SET utf8 ;
USE `briar_server` ;

-- -----------------------------------------------------
-- Table `briar_server`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `briar_server`.`users` ;

CREATE TABLE IF NOT EXISTS `briar_server`.`users` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `phone_generated_id` VARCHAR(64) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `ip` VARCHAR(20) NULL DEFAULT NULL,
  `port` INT(5) UNSIGNED NOT NULL,
  `is_active` TINYINT(1) NOT NULL,
  `created` TIMESTAMP NULL DEFAULT NULL,
  `modified` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `briar_server`.`contacts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `briar_server`.`contacts` ;

CREATE TABLE IF NOT EXISTS `briar_server`.`contacts` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_user` INT(10) UNSIGNED NOT NULL,
  `first_user_contact_acceptance` TINYINT(1) NOT NULL,
  `second_user` INT(10) UNSIGNED NOT NULL,
  `second_user_contact_acceptance` TINYINT(1) NOT NULL,
  `is_active` TINYINT(1) NOT NULL,
  `created` TIMESTAMP NULL DEFAULT NULL,
  `modified` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_contacts_users_idx` (`first_user` ASC),
  INDEX `fk_contacts_users1_idx` (`second_user` ASC),
  CONSTRAINT `fk_contacts_users`
    FOREIGN KEY (`first_user`)
    REFERENCES `briar_server`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_contacts_users1`
    FOREIGN KEY (`second_user`)
    REFERENCES `briar_server`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
