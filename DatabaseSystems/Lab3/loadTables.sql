CREATE TABLE `Video`.`Video_Recordings` (
  `recording_id` INT NOT NULL,
  `director` VARCHAR(45) NULL,
  `title` VARCHAR(45) NULL,
  `category` VARCHAR(45) NULL,
  `image_name` VARCHAR(45) NULL,
  `duration` INT NULL,
  `rating` VARCHAR(5) NULL,
  `year_released` INT NULL,
  `price` FLOAT NULL,
  `stock_count` INT NULL,
  PRIMARY KEY (`recording_id`),
  UNIQUE INDEX `recording_id_UNIQUE` (`recording_id` ASC) VISIBLE);

CREATE TABLE `Video`.`Video_Categories` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);
  
CREATE TABLE `Video`.`Video_Actors` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `recording_id` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);