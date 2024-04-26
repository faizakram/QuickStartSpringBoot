CREATE TABLE `permission`
(
    `id`          INT         NOT NULL AUTO_INCREMENT,
    `code`        VARCHAR(50) NOT NULL,
    `name`        VARCHAR(50) NOT NULL,
    `active`      BIT(1)      NOT NULL,
    `created_at`  DATETIME(6) NOT NULL,
    `modified_at` DATETIME(6) NOT NULL,
    `created_by`  INT         NOT NULL,
    `modified_by` INT         NOT NULL,
    `group_name`  VARCHAR(30) NOT NULL,
    PRIMARY KEY (`id`)
);
