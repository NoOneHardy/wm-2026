USE wm;

CREATE TABLE user
(
    id                 INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username           VARCHAR(255)                   NOT NULL,
    password           VARCHAR(255)                   NOT NULL,
    email              VARCHAR(255)                   NOT NULL,
    firstname          VARCHAR(255)                   NOT NULL,
    lastname           VARCHAR(255)                   NOT NULL,
    created_at         DATETIME                       NOT NULL DEFAULT NOW(),
    is_active          BOOLEAN                        NOT NULL DEFAULT FALSE,
    points             INT                            NOT NULL DEFAULT 0,
    updated_at         DATETIME                       NOT NULL DEFAULT NOW(),
    last_reviewed_rank INT,
    avatar             VARCHAR(255),
    confirmed_at       DATETIME,
    deleted_at         DATETIME
);

use wm;

CREATE TABLE confirmation_codes
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id INT                            NOT NULL,
    code       VARCHAR(6)                     NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY fk_user_confirmation_code (fk_user_id) REFERENCES user (id)
);

CREATE TABLE reset_codes
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id INT                            NOT NULL,
    code       VARCHAR(6)                     NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY fk_user_reset_code (fk_user_id) REFERENCES user (id)
);

CREATE TABLE role
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name       VARCHAR(255)                   NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    updated_at DATETIME                       NOT NULL DEFAULT NOW(),
    deleted_at DATETIME
);

CREATE TABLE user_role
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id INT                            NOT NULL,
    fk_role_id INT                            NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY fk_user_user_role (fk_user_id) REFERENCES user (id),
    CONSTRAINT FOREIGN KEY fk_role_user_role (fk_role_id) REFERENCES role (id)
);
