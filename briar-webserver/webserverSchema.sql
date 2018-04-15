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
  `status_id` INT(1) UNSIGNED NOT NULL,
  `avatar_id` INT(2) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8;
-- -----------------------------------------------------
-- Table `briar_server`.`articles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `briar_server`.`articles` ;
CREATE TABLE IF NOT EXISTS `briar_server`.`articles` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `publication_date` DATE NOT NULL,
  `author` VARCHAR(64) NOT NULL,
  `title` MEDIUMTEXT NOT NULL,
  `body` LONGBLOB NOT NULL,
  `created` DATETIME NOT NULL,
  `added_by` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_articles_users1_idx` (`added_by` ASC),
  CONSTRAINT `fk_articles_users1`
    FOREIGN KEY (`added_by`)
    REFERENCES `briar_server`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
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
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8;
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

set @phoneGeneratedId = "adminUser";
set @password = "qwerty";
set @ip = "111.111.111.111";
set @port = 1234;
set @statusId = 0;
set @avatarId = 0;

insert into briar_server.users
            (phone_generated_id, password, ip, port, status_id, avatar_id,
            is_active, created, modified)
            values (@phoneGeneratedId, @password, @ip, @port,
            @statusId, @avatarId,
            1,
            current_timestamp(), current_timestamp());

set @publicationDate = current_date();
set @author = "benjamin";
set @title = "article title";
set @body = "paragraph1%#%paragraph2%#%paragraph3%#%lastParagraph";
set @addedBy := (SELECT id from briar_server.users where phone_generated_id = @phoneGeneratedId);

insert into briar_server.articles
            (publication_date, author, title, body, created, added_by)
            values (@publicationDate, @author, @title, @body,
            current_timestamp(), @addedBy);

set @author2 = "daniel";
set @title2 = "article title2";

insert into briar_server.articles
            (publication_date, author, title, body, created, added_by)
            values (@publicationDate, @author2, @title2, @body,
            current_timestamp(), @addedBy);