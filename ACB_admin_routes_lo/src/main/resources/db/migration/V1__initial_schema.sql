SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema acb_routes_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema acb_routes_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `acb_routes_db` DEFAULT CHARACTER SET utf8 ;
USE `acb_routes_db` ;

-- -----------------------------------------------------
-- Table `acb_routes_db`.`schools`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS schools (
    school_id BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(150) NOT NULL,
    school_code VARCHAR(45) UNIQUE NOT NULL,
    phone VARCHAR(45) NOT NULL,
    active TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY (`school_id`))
    ENGINE = InnoDB;

    CREATE INDEX idx_code_school ON schools(`school_code`);
    CREATE INDEX idx_school_name ON schools(`name`);
-- -----------------------------------------------------
-- Table `acb_routes_db`.`routes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS routes (
    route_id BIGINT NOT NULL AUTO_INCREMENT,
    shift_code ENUM('541', '542') NOT NULL,
    route_code VARCHAR(4) NOT NULL,
    description TEXT NULL,
    school_id BIGINT NOT NULL,
    scheduled_start_time TIME NOT NULL,
    guide_main_id BIGINT NOT NULL,
    guide_main_name VARCHAR(150) NULL,
    guide_support_id BIGINT NULL,
    guide_support_name VARCHAR(150) NULL,
    active TINYINT DEFAULT 1,
    PRIMARY KEY (`route_id`),
    CONSTRAINT uq_route_shift_code UNIQUE (`shift_code`, `route_code`),
    INDEX `fk_routes_schools_idx` (`school_id` ASC) VISIBLE,
    CONSTRAINT `fk_routes_schools`
    FOREIGN KEY (`school_id`)
    REFERENCES schools (`school_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

    CREATE INDEX idx_route_code ON routes(`route_code`);
-- -----------------------------------------------------
-- Table `acb_routes_db`.`meeting_points`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS meeting_points (
    meeting_point_id BIGINT NOT NULL AUTO_INCREMENT,
    meeting_point_code VARCHAR(4) NOT NULL,
    address VARCHAR(45) NOT NULL,
    distance INT UNSIGNED NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    route_id BIGINT NOT NULL,
    PRIMARY KEY (`meeting_point_id`),
    CONSTRAINT uq_meeting_point_routes UNIQUE (`meeting_point_code`, `route_id`),
    CONSTRAINT chk_distance CHECK(distance>=0),
    INDEX `fk_meeting_points_routes_idx` (`route_id` ASC) VISIBLE,
    CONSTRAINT `fk_meeting_points_routes`
    FOREIGN KEY (`route_id` )
    REFERENCES routes (`route_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `acb_routes_db`.`students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id BIGINT NOT NULL,
    `name` VARCHAR(150),
    active TINYINT NOT NULL DEFAULT 1,
    route_id BIGINT NOT NULL,
    PRIMARY KEY (`student_id`),
    INDEX `fk_students_routes_idx` (`route_id` ASC) VISIBLE,
    CONSTRAINT `fk_students_routes`
    FOREIGN KEY (`route_id`)
    REFERENCES routes (`route_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

    CREATE INDEX idx_student_name ON students(`name`);

CREATE TABLE IF NOT EXISTS students_meeting_points (
    shift_code ENUM('541', '542') NOT NULL,
    meeting_point_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, shift_code),
    INDEX `fk_students_meeting_points_meeting_points_idx` (`meeting_point_id` ASC) VISIBLE,
    INDEX `fk_students_meeting_points_students_idx` (`student_id` ASC) VISIBLE,
    -- CONSTRAINT uq_meeting_point_students_route UNIQUE (`student_id`,`shift_code`),
    CONSTRAINT `fk_students_meeting_points_meeting_points`
    FOREIGN KEY (`meeting_point_id`)
    REFERENCES meeting_points(`meeting_point_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_students_meeting_points_students`
    FOREIGN KEY (`student_id`)
    REFERENCES students(`student_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
