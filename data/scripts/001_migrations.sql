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

CREATE TABLE confirmation_codes
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id INT                            NOT NULL,
    code       VARCHAR(6)                     NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY fk_user_confirmation_code (fk_user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE reset_codes
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id INT                            NOT NULL,
    code       VARCHAR(6)                     NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY fk_user_reset_code (fk_user_id) REFERENCES user (id) ON DELETE CASCADE
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
    CONSTRAINT FOREIGN KEY fk_user_user_role (fk_user_id) REFERENCES user (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY fk_role_user_role (fk_role_id) REFERENCES role (id) ON DELETE CASCADE
);

CREATE TABLE email_settings
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name       VARCHAR(255)                   NOT NULL,
    deleted_at DATETIME
);

CREATE TABLE user_email_settings
(
    id                   INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_email_settings_id INT                            NOT NULL,
    fk_user_id           INT                            NOT NULL,
    created_at           DATETIME                       NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY fk_email_settings_user_email_settings (fk_email_settings_id) REFERENCES email_settings (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY fk_user_user_email_settings (fk_user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE session
(
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id    INT,
    last_activity DATETIME                       NOT NULL,
    payload       VARCHAR(255)                   NOT NULL,
    CONSTRAINT FOREIGN KEY fk_user_session (fk_user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE tip_card
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_user_id INT                            NOT NULL,
    created_at DATETIME                       NOT NULL DEFAULT NOW(),
    updated_at DATETIME                       NOT NULL DEFAULT NOW(),
    deleted_at DATETIME,
    CONSTRAINT FOREIGN KEY fk_user_tip_card (fk_user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE `group`
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name       VARCHAR(255)                   NOT NULL,
    knockout   BOOLEAN                        NOT NULL DEFAULT FALSE,
    deleted_at DATETIME
);

CREATE TABLE team
(
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name        VARCHAR(255)                   NOT NULL,
    fk_group_id INT                            NOT NULL,
    points      INT                            NOT NULL DEFAULT 0,
    deleted_at  DATETIME,
    CONSTRAINT FOREIGN KEY fk_group_team (fk_group_id) REFERENCES `group` (id) ON DELETE CASCADE
);

CREATE TABLE game
(
    id               INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_home_team_id  INT                            NOT NULL,
    fk_guest_team_id INT                            NOT NULL,
    fk_group_id      INT                            NOT NULL,
    timestamp        DATETIME                       NOT NULL,
    created_at       DATETIME                       NOT NULL DEFAULT NOW(),
    updated_at       DATETIME                       NOT NULL DEFAULT NOW(),
    deleted_at       DATETIME,
    goals_home       INT,
    goals_guest      INT,
    CONSTRAINT FOREIGN KEY fk_home_team_game (fk_home_team_id) REFERENCES `team` (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY fk_guest_team_game (fk_guest_team_id) REFERENCES `team` (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY fk_group_game (fk_group_id) REFERENCES `group` (id) ON DELETE CASCADE
);

CREATE TABLE tip
(
    id             INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fk_game_id     INT                            NOT NULL,
    fk_tip_card_id INT                            NOT NULL,
    goals_home     INT                            NOT NULL,
    goals_guest    INT                            NOT NULL,
    created_at     DATETIME                       NOT NULL DEFAULT NOW(),
    updated_at     DATETIME                       NOT NULL DEFAULT NOW(),
    joker          INT                            NOT NULL DEFAULT 1,
    deleted_at     DATETIME,
    CONSTRAINT FOREIGN KEY fk_game_tip (fk_game_id) REFERENCES `game` (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY fk_tip_card_tip (fk_tip_card_id) REFERENCES `tip_card` (id) ON DELETE CASCADE
);
