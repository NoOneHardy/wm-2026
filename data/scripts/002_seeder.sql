use wm;

INSERT INTO user (username, password, firstname, lastname, email, confirmed_at)
VALUES ('NoOneHardy', '1234', 'Silas', 'Hardegger', 'silas.hardegger@outlook.com', NOW());

INSERT INTO role (name)
VALUES ('admin'),
       ('player');

INSERT INTO user_role (fk_user_id, fk_role_id)
VALUES (1, 1),
       (1, 2);

INSERT INTO email_settings (name)
VALUES ('general'),
       ('open_tips'),
       ('daily_review'),
       ('rank_up'),
       ('rank_down');

INSERT INTO `group` (name, knockout)
VALUES ('Gruppe A', FALSE),
       ('Gruppe B', FALSE),
       ('Gruppe C', FALSE),
       ('Gruppe D', FALSE),
       ('Gruppe E', FALSE),
       ('Gruppe F', FALSE),
       ('Achtelfinale', TRUE),
       ('Viertelfinale', TRUE),
       ('Halbfinale', TRUE),
       ('Spiel um Platz 3', TRUE),
       ('Finale', TRUE);

INSERT INTO team (name, fk_group_id)
VALUES ('Schweiz', 1),
       ('Deutschland', 1),
       ('Ungarn', 1),
       ('Schottland', 1),
       ('Kroatien', 2),
       ('Italien', 2),
       ('Albanien', 2),
       ('Spanien', 2),
       ('England', 3),
       ('Dänemark', 3),
       ('Serbien', 3),
       ('Slowenien', 3),
       ('Frankreich', 4),
       ('Niederlande', 4),
       ('Polen', 4),
       ('Österreich', 4),
       ('Belgien', 5),
       ('Ukraine', 5),
       ('Rumänien', 5),
       ('Slowakei', 5),
       ('Portugal', 6),
       ('Tschechien', 6),
       ('Türkei', 6),
       ('Georgien', 6);

INSERT INTO game (fk_home_team_id, fk_guest_team_id, fk_group_id, timestamp)
VALUES (2, 4, 1, '2025-06-13 21:00:00'),
       (3, 1, 1, '2025-06-15 15:00:00'),
       (2, 3, 1, '2025-06-19 18:00:00'),
       (4, 1, 1, '2025-06-19 21:00:00'),
       (4, 3, 1, '2025-06-23 21:00:00'),
       (1, 2, 1, '2025-06-23 21:00:00'),
       (8, 5, 2, '2025-06-15 18:00:00'),
       (6, 7, 2, '2025-06-15 21:00:00'),
       (5, 7, 2, '2025-06-19 15:00:00'),
       (8, 6, 2, '2025-06-20 21:00:00'),
       (7, 8, 2, '2025-06-24 21:00:00'),
       (5, 6, 2, '2025-06-24 21:00:00'),
       (12, 10, 3, '2025-06-16 18:00:00'),
       (11, 9, 3, '2025-06-16 21:00:00'),
       (12, 11, 3, '2025-06-20 15:00:00'),
       (10, 9, 3, '2025-06-20 18:00:00'),
       (9, 12, 3, '2025-06-25 21:00:00'),
       (10, 11, 3, '2025-06-25 21:00:00'),
       (15, 14, 4, '2025-06-16 15:00:00'),
       (16, 13, 4, '2025-06-17 21:00:00'),
       (15, 16, 4, '2025-06-21 18:00:00'),
       (14, 13, 4, '2025-06-21 21:00:00'),
       (14, 16, 4, '2025-06-25 18:00:00'),
       (13, 15, 4, '2025-06-25 18:00:00'),
       (19, 18, 5, '2025-06-17 15:00:00'),
       (17, 20, 5, '2025-06-17 18:00:00'),
       (20, 18, 5, '2025-06-21 15:00:00'),
       (17, 19, 5, '2025-06-22 21:00:00'),
       (20, 19, 5, '2025-06-26 18:00:00'),
       (18, 17, 5, '2025-06-26 18:00:00'),
       (23, 24, 6, '2025-06-18 18:00:00'),
       (21, 22, 6, '2025-06-18 21:00:00'),
       (24, 22, 6, '2025-06-22 15:00:00'),
       (23, 21, 6, '2025-06-22 18:00:00'),
       (24, 21, 6, '2025-06-26 21:00:00'),
       (22, 23, 6, '2025-06-26 21:00:00'),
       (NULL, NULL, 7, '2025-06-29 18:00:00'),
       (NULL, NULL, 7, '2025-06-29 21:00:00'),
       (NULL, NULL, 7, '2025-06-30 18:00:00'),
       (NULL, NULL, 7, '2025-06-30 21:00:00'),
       (NULL, NULL, 7, '2025-07-01 18:00:00'),
       (NULL, NULL, 7, '2025-07-01 21:00:00'),
       (NULL, NULL, 7, '2025-07-02 18:00:00'),
       (NULL, NULL, 7, '2025-07-02 21:00:00'),
       (NULL, NULL, 8, '2025-07-05 18:00:00'),
       (NULL, NULL, 8, '2025-07-05 21:00:00'),
       (NULL, NULL, 8, '2025-07-06 18:00:00'),
       (NULL, NULL, 8, '2025-07-06 21:00:00'),
       (NULL, NULL, 9, '2025-07-09 21:00:00'),
       (NULL, NULL, 9, '2025-07-10 21:00:00'),
       (NULL, NULL, 10, '2025-07-14 21:00:00'),
       (NULL, NULL, 11, '2025-07-18 21:00:00');
