CREATE TABLE IF NOT EXISTS `ds_datasource` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `datasource_name` VARCHAR(128) NOT NULL,
    `datasource_type` VARCHAR(32) NOT NULL,
    `connection_params` JSON NULL,
    `secret_ref` VARCHAR(256) NULL,
    `network_policy` JSON NULL,
    `status` TINYINT DEFAULT 1,
    `version` INT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `form_form` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `form_name` VARCHAR(128) NOT NULL,
    `form_code` VARCHAR(64) UNIQUE NOT NULL,
    `form_config` JSON NULL,
    `schema_version` VARCHAR(32) DEFAULT '1.0.0',
    `status` TINYINT DEFAULT 1,
    `version` INT DEFAULT 0,
    `deleted` TINYINT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `form_field` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `form_id` BIGINT NOT NULL,
    `field_name` VARCHAR(128) NOT NULL,
    `field_code` VARCHAR(64) NOT NULL,
    `field_type` VARCHAR(32) NOT NULL,
    `field_config` JSON NULL,
    `sort_order` INT DEFAULT 0,
    `required` TINYINT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_form_field_form` FOREIGN KEY (`form_id`) REFERENCES `form_form` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `page_page` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `page_name` VARCHAR(128) NOT NULL,
    `page_code` VARCHAR(64) UNIQUE NOT NULL,
    `page_config` JSON NULL,
    `schema_version` VARCHAR(32) DEFAULT '1.0.0',
    `status` TINYINT DEFAULT 1,
    `version` INT DEFAULT 0,
    `deleted` TINYINT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `dm_data_model` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `model_name` VARCHAR(128) NOT NULL,
    `model_code` VARCHAR(64) UNIQUE NOT NULL,
    `model_config` JSON NULL,
    `schema_version` VARCHAR(32) DEFAULT '1.0.0',
    `status` TINYINT DEFAULT 1,
    `version` INT DEFAULT 0,
    `deleted` TINYINT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `dm_model_field` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `model_id` BIGINT NOT NULL,
    `field_name` VARCHAR(128) NOT NULL,
    `field_code` VARCHAR(64) NOT NULL,
    `field_type` VARCHAR(32) NOT NULL,
    `field_config` JSON NULL,
    `sort_order` INT DEFAULT 0,
    `required` TINYINT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_dm_field_model` FOREIGN KEY (`model_id`) REFERENCES `dm_data_model` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `config_project_config` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `project_id` BIGINT UNIQUE NOT NULL,
    `config_json` JSON NOT NULL,
    `schema_version` VARCHAR(32) DEFAULT '1.0.0',
    `version` INT DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_by` BIGINT NULL,
    `updated_time` DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX IF NOT EXISTS `idx_form_field_form` ON `form_field` (`form_id`, `field_code`);
CREATE INDEX IF NOT EXISTS `idx_dm_field_model` ON `dm_model_field` (`model_id`, `field_code`);
CREATE INDEX IF NOT EXISTS `idx_form_form_deleted` ON `form_form` (`deleted`);
CREATE INDEX IF NOT EXISTS `idx_page_page_deleted` ON `page_page` (`deleted`);
CREATE INDEX IF NOT EXISTS `idx_dm_model_deleted` ON `dm_data_model` (`deleted`);
