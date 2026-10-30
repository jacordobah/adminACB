-- Mon Oct 12 01:01:42 2026
-- Model: New Model    Version: 1.0
-- Purified for Flyway Migrations (ACB_admin_lo)

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `document_type` ENUM('CC','CE','NUIP','PPT','PPE') NOT NULL,
    `document_number` VARCHAR(45) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `address` VARCHAR(45) NOT NULL,
    `role` ENUM('GUIDE', 'LEADER', 'MANAGER', 'STUDENT', 'GUARDIAN') NOT NULL,
    `active` TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY (`user_id`))
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `guardians`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guardians` (
                                           `phone` VARCHAR(45) NOT NULL,
    `guardian_id` BIGINT NOT NULL,
    PRIMARY KEY (`guardian_id`),
    CONSTRAINT `fk_guardians_users`
    FOREIGN KEY (`guardian_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `schools`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `schools` (
    `school_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `school_code` VARCHAR(45) NOT NULL,
    `phone` VARCHAR(45) NOT NULL,
    `active` TINYINT NULL DEFAULT 1,
    PRIMARY KEY (`school_id`))
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `routes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `routes` (
    `route_id` VARCHAR(4) NOT NULL,
    `shift_code` ENUM('541', '542') NOT NULL,
    `description` TEXT NULL,
    `school_id` BIGINT NOT NULL,
    `scheduled_start_time` TIME NOT NULL,
    PRIMARY KEY (`route_id`, `shift_code`),
    INDEX `fk_routes_schools1_idx` (`school_id` ASC),
    CONSTRAINT `fk_routes_schools1`
    FOREIGN KEY (`school_id`)
    REFERENCES `schools` (`school_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `meeting_points`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `meeting_points` (
    `meeting_point_id` BIGINT NOT NULL AUTO_INCREMENT,
    `address` VARCHAR(45) NOT NULL,
    `distance` INT NOT NULL,
    `route_id` VARCHAR(4) NOT NULL,
    `shift_code` ENUM('541', '542') NOT NULL,
    `latitude` DOUBLE NOT NULL,
    `longitude` DOUBLE NOT NULL,
    PRIMARY KEY (`meeting_point_id`, `route_id`, `shift_code`),
    INDEX `fk_meeting_points_routes1_idx` (`route_id` ASC, `shift_code` ASC),
    CONSTRAINT `fk_meeting_points_routes1`
    FOREIGN KEY (`route_id` , `shift_code`)
    REFERENCES `routes` (`route_id` , `shift_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `students` (
    `grade` VARCHAR(20) NOT NULL,
    `school_shift` VARCHAR(10) NOT NULL,
    `user_id` BIGINT NOT NULL,
    `meeting_point_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`),
    INDEX `fk_student_meeting_point1_idx` (`meeting_point_id` ASC),
    CONSTRAINT `fk_student_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_student_meeting_point1`
    FOREIGN KEY (`meeting_point_id`)
    REFERENCES `meeting_points` (`meeting_point_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `guides`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guides` (
    `contract_number` VARCHAR(45) NOT NULL,
    `username` VARCHAR(45) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `zone` VARCHAR(45) NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`),
    CONSTRAINT `fk_guides_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `trips`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trips` (
    `trip_id` BIGINT NOT NULL AUTO_INCREMENT,
    `trip_code` ENUM('541', '542') NOT NULL,
    `start_time` TIMESTAMP NOT NULL,
    `end_time` TIMESTAMP NULL,
    `route_id` VARCHAR(4) NOT NULL,
    `shift_code` ENUM('541', '542') NOT NULL,
    PRIMARY KEY (`trip_id`),
    INDEX `fk_trips_routes1_idx` (`route_id` ASC, `shift_code` ASC),
    CONSTRAINT `fk_trips_routes1`
    FOREIGN KEY (`route_id` , `shift_code`)
    REFERENCES `routes` (`route_id` , `shift_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `impact_arrivals`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `impact_arrivals` (
    `impact_arrival_id` BIGINT NOT NULL AUTO_INCREMENT,
    `date` TIMESTAMP NOT NULL,
    `status` ENUM('OPEN', 'NOTIFIED', 'MINUTED') NOT NULL,
    `detail` TEXT NULL,
    `guide_id` BIGINT NOT NULL,
    PRIMARY KEY (`impact_arrival_id`, `guide_id`),
    INDEX `fk_impact_arrival_guides1_idx` (`guide_id` ASC),
    CONSTRAINT `fk_impact_arrival_guides1`
    FOREIGN KEY (`guide_id`)
    REFERENCES `guides` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `non_impact_arrivals`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `non_impact_arrivals` (
    `non_impact_arrival_id` BIGINT NOT NULL AUTO_INCREMENT,
    `date` TIMESTAMP NOT NULL,
    `status` ENUM('PENDING', 'NOTIFIED') NOT NULL,
    `counter` INT NOT NULL DEFAULT 0,
    `impact_arrival_id` BIGINT NULL,
    `guide_id` BIGINT NOT NULL,
    PRIMARY KEY (`non_impact_arrival_id`, `guide_id`),
    INDEX `fk_non_impact_arrival_impact_arrival1_idx` (`impact_arrival_id` ASC),
    INDEX `fk_non_impact_arrival_guides1_idx` (`guide_id` ASC),
    CONSTRAINT `fk_non_impact_arrival_impact_arrival1`
    FOREIGN KEY (`impact_arrival_id`)
    REFERENCES `impact_arrivals` (`impact_arrival_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_non_impact_arrival_guides1`
    FOREIGN KEY (`guide_id`)
    REFERENCES `guides` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `students_guardians`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `students_guardians` (
    `is_primary` TINYINT NOT NULL,
    `relationship` ENUM('FATHER', 'MOTHER', 'UNCLE_AUNT', 'RELATIVE', 'NEIGHBOR') NOT NULL,
    `student_id` BIGINT NOT NULL,
    `guardian_id` BIGINT NOT NULL,
    PRIMARY KEY (`student_id`, `guardian_id`),
    INDEX `fk_student_guardian_guardians1_idx` (`guardian_id` ASC),
    CONSTRAINT `fk_student_guardian_student1`
    FOREIGN KEY (`student_id`)
    REFERENCES `students` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_student_guardian_guardians1`
    FOREIGN KEY (`guardian_id`)
    REFERENCES `guardians` (`guardian_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `operations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `operations` (
    `trip_id` BIGINT NOT NULL,
    `guide_id` BIGINT NOT NULL,
    `is_primary_on_trip` TINYINT NOT NULL,
    PRIMARY KEY (`trip_id`, `guide_id`),
    INDEX `fk_operation_guides1_idx` (`guide_id` ASC),
    CONSTRAINT `fk_operation_trips1`
    FOREIGN KEY (`trip_id`)
    REFERENCES `trips` (`trip_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_operation_guides1`
    FOREIGN KEY (`guide_id`)
    REFERENCES `guides` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `default_operations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `default_operations` (
    `guide_id` BIGINT NOT NULL,
    `route_id` VARCHAR(4) NOT NULL,
    `shift_code` ENUM('541', '542') NOT NULL,
    `is_primary_on_route` TINYINT NOT NULL,
    PRIMARY KEY (`guide_id`, `route_id`, `shift_code`),
    INDEX `fk_default_operation_guides1_idx` (`guide_id` ASC),
    INDEX `fk_default_operation_routes1_idx` (`route_id` ASC, `shift_code` ASC),
    CONSTRAINT `fk_default_operation_guides1`
    FOREIGN KEY (`guide_id`)
    REFERENCES `guides` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_default_operation_routes1`
    FOREIGN KEY (`route_id` , `shift_code`)
    REFERENCES `routes` (`route_id` , `shift_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `attendances`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `attendances` (
    `is_present` TINYINT NOT NULL,
    `roll_Call` TIMESTAMP NOT NULL,
    `trip_id` BIGINT NOT NULL,
    `student_id` BIGINT NOT NULL,
    PRIMARY KEY (`trip_id`, `student_id`),
    INDEX `fk_attendance_student1_idx` (`student_id` ASC),
    CONSTRAINT `fk_attendance_trips1`
    FOREIGN KEY (`trip_id`)
    REFERENCES `trips` (`trip_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_attendance_student1`
    FOREIGN KEY (`student_id`)
    REFERENCES `students` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `action_codes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS action_codes (
    code_id INT NOT NULL,
    description VARCHAR(150) NOT NULL,
    PRIMARY KEY (code_id))
    ENGINE = InnoDB;
-- Table guide_check_ins
CREATE TABLE IF NOT EXISTS guide_check_ins (
    check_in_id BIGINT NOT NULL AUTO_INCREMENT,
    check_in_time TIMESTAMP NOT NULL,
    address VARCHAR(200) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    route_id VARCHAR(4) NOT NULL,
    shift_code ENUM('541', '542') NOT NULL,
    guide_id BIGINT NOT NULL,
    is_valid TINYINT NOT NULL,
    PRIMARY KEY (check_in_id),
    INDEX fk_guide_check_ins_routes1_idx (route_id ASC, shift_code ASC),
    INDEX fk_guide_check_ins_guides1_idx (guide_id ASC),
    CONSTRAINT fk_guide_check_ins_routes1
    FOREIGN KEY (route_id , shift_code)
    REFERENCES routes (route_id , shift_code)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_guide_check_ins_guides1
    FOREIGN KEY (guide_id)
    REFERENCES guides (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;
-- Table trip_events
CREATE TABLE IF NOT EXISTS trip_events (
    event_id BIGINT NOT NULL AUTO_INCREMENT,
    event_time TIMESTAMP NOT NULL,
    latitude DOUBLE NULL,
    longitude DOUBLE NULL,
    observations TEXT NULL,
    trip_id BIGINT NOT NULL,
    guide_id BIGINT NOT NULL,
    code_id INT NOT NULL,
    PRIMARY KEY (event_id),
    INDEX fk_trip_events_trips1_idx (trip_id ASC),
    INDEX fk_trip_events_guides1_idx (guide_id ASC),
    INDEX fk_trip_events_action_codes1_idx (code_id ASC),
    CONSTRAINT fk_trip_events_trips1
    FOREIGN KEY (trip_id)
    REFERENCES trips (trip_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_trip_events_guides1
    FOREIGN KEY (guide_id)
    REFERENCES guides (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_trip_events_action_codes1
    FOREIGN KEY (code_id)
    REFERENCES action_codes (code_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;
-- Data Seed / Inyección Inicial de Códigos de Acción
INSERT INTO action_codes (code_id, description) VALUES
(58, 'DISPONIBLE PARA SERVICIO'),
(512, 'INFORME NOVEDADES'),
(520, 'UBICACIÓN ACTUAL'),
(523, 'HE LLEGADO AL SITIO'),
(524, 'MISIÓN CUMPLIDA'),
(537, 'APOYE POLICÍA'),
(541, 'RUTA CONFIANZA HACIA EL COLEGIO'),
(542, 'RUTA CONFIANZA SALIDA DEL COLEGIO'),
(550, 'ACCIDENTE GUIA EN BICICLETA')
ON DUPLICATE KEY UPDATE description = VALUES(description);
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;