-- Mon Oct 12 01:01:42 2026
-- Model: New Model    Version: 1.0
-- Purified for Flyway Migrations (ACB_admin_lo)

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

-- -----------------------------------------------------
-- Table `guides_login`
-- -----------------------------------------------------

CREATE TABLE guides_login (
    guide_id BIGINT PRIMARY KEY,
    username VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('GUIDE', 'LEADER', 'MANAGER') NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
)
    ENGINE = InnoDB;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;