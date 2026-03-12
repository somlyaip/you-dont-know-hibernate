CREATE TABLE project
(
    id                  BIGINT PRIMARY KEY,
    project_name        VARCHAR(255),
    status              VARCHAR(30)
);

CREATE TABLE estimation
(
    id                  BIGINT PRIMARY KEY,
    original_man_days   INTEGER
);

CREATE TABLE issue
(
    id                  BIGINT PRIMARY KEY,
    title               VARCHAR(255),
    description         VARCHAR(1000),
    status              VARCHAR(30),
    issue_level         VARCHAR(30),
    project_id          BIGINT,
    estimation_id       BIGINT
);

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_project_id FOREIGN KEY (project_id) REFERENCES project (id);
ALTER TABLE issue
    ADD CONSTRAINT fk_issue_estimation_id FOREIGN KEY (estimation_id) REFERENCES estimation (id);

CREATE TABLE feature
(
    id                  BIGINT PRIMARY KEY,
    title               VARCHAR(255),
    description         VARCHAR(1000),
    status              VARCHAR(30),
    project_id          BIGINT,
    estimation_id       BIGINT
);

ALTER TABLE feature
    ADD CONSTRAINT fk_feature_project_id FOREIGN KEY (project_id) REFERENCES project (id);
ALTER TABLE feature
    ADD CONSTRAINT fk_feature_estimation_id FOREIGN KEY (estimation_id) REFERENCES estimation (id);

CREATE TABLE project_architect
(
    id                  BIGINT PRIMARY KEY,
    name                VARCHAR(50),
    email               VARCHAR(50),
    position            VARCHAR(50),
    feature_id          BIGINT
);

ALTER TABLE project_architect
    ADD CONSTRAINT fk_project_architect_feature_id FOREIGN KEY (feature_id) REFERENCES feature (id);

CREATE TABLE developer
(
    id                  BIGINT PRIMARY KEY,
    name                VARCHAR(50),
    email               VARCHAR(50),
    feature_id          BIGINT
);

ALTER TABLE developer
    ADD CONSTRAINT fk_developer_feature_id FOREIGN KEY (feature_id) REFERENCES feature (id);

CREATE TABLE task
(
    id                  BIGINT PRIMARY KEY,
    title               VARCHAR(50),
    description         VARCHAR(50),
    feature_id          BIGINT
);

ALTER TABLE task
    ADD CONSTRAINT fk_task_feature_id FOREIGN KEY (feature_id) REFERENCES feature (id);

CREATE TABLE comment
(
    id                  BIGINT PRIMARY KEY,
    title               VARCHAR(50),
    description         VARCHAR(50),
    task_id             BIGINT
);

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_task_id FOREIGN KEY (task_id) REFERENCES comment (id);

CREATE TABLE sub_task
(
    id                  BIGINT PRIMARY KEY,
    title               VARCHAR(50),
    description         VARCHAR(50),
    task_id             BIGINT
);

ALTER TABLE sub_task
    ADD CONSTRAINT fk_sub_task_task_id FOREIGN KEY (task_id) REFERENCES sub_task (id);

INSERT INTO project(id, project_name, status)
    VALUES (1, 'Project 1', 'PLANNING'),
     (2, 'Project 2', 'PLANNING'),
     (3, 'Project 3', 'PLANNING');

INSERT INTO estimation(id, original_man_days)
    VALUES (1, 1),
     (2, 2),
     (3, 4);

INSERT INTO issue(id, title, description, status, issue_level, project_id, estimation_id)
    VALUES (1, 'Bug 1', 'This is the first bug', 'IN_PROGRESS', 'EASY', 1, 1),
     (2, 'Bug 2', 'This is the second bug', 'NEW', 'MEDIUM', 2, 2),
     (3, 'Bug 3', 'This is the third bug', 'IN_PROGRESS', 'BLOCKING', 2, 3),
     (4, 'Bug 4', 'This is the fourth bug', 'NEW', 'EASY', 2, 1),
     (5, 'Bug 5', 'This is the fifth bug', 'NEW', 'MEDIUM', 2, 2),
     (6, 'Bug 6', 'This is the sixth bug', 'IN_PROGRESS', 'BLOCKING', 3, 3);

INSERT INTO feature(id, title, description, status, project_id, estimation_id)
    VALUES(1, 'Feature 1', 'Feature description 1', 'ACCEPTED', 1, 2),
    (2, 'Feature 2', 'Feature description 2', 'ACCEPTED', 1, 2),
    (3, 'Feature 3', 'Feature description 3', 'PLANNED', 1, 2),
    (4, 'Feature 4', 'Feature description 4', 'IN_DEVELOPMENT', 2, 1),
    (5, 'Feature 5', 'Feature description 5', 'ACCEPTED', 2, 3),
    (6, 'Feature 6', 'Feature description 6', 'COMPLETED', 2, 1),
    (7, 'Feature 7', 'Feature description 7', 'REJECTED', 2, 3),
    (8, 'Feature 8', 'Feature description 8', 'IN_DEVELOPMENT', 3, 3),
    (9, 'Feature 9', 'Feature description 9', 'PLANNED', 3, 2),
    (10, 'Feature 10', 'Feature description 10', 'ACCEPTED', 3, 1);

INSERT INTO project_architect(id, name, email, position, feature_id)
    VALUES (1, 'Architect 1', '1@mail.com', 'backend', 1),
     (2, 'Architect 2', '2@mail.com', 'backend', 2),
     (3, 'Architect 3', '3@mail.com', 'backend', 3),
     (4, 'Architect 4', '4@mail.com', 'backend', 4),
     (5, 'Architect 5', '5@mail.com', 'backend', 5),
     (6, 'Architect 6', '6@mail.com', 'backend', 6);

INSERT INTO developer(id, name, email, feature_id)
    VALUES (1, 'Developer 1', '1@mail.com', 1),
     (2, 'Developer 2', '2@mail.com', 2),
     (3, 'Developer 3', '3@mail.com', 4),
     (4, 'Developer 4', '4@mail.com', 5),
     (5, 'Developer 5', '5@mail.com', 6),
     (6, 'Developer 6', '6@mail.com', 4),
     (7, 'Developer 7', '7@mail.com', 5);

INSERT INTO task(id, title, description, feature_id)
    VALUES (1, 'Title 1', 'Description 1', 1),
    (2, 'Title 2', 'Description 2', 1),
    (3, 'Title 3', 'Description 3', 1),
    (4, 'Title 4', 'Description 4', 2),
    (5, 'Title 5', 'Description 5', 2),
    (6, 'Title 6', 'Description 6', 2),
    (7, 'Title 7', 'Description 7', 3),
    (8, 'Title 8', 'Description 8', 3),
    (9, 'Title 9', 'Description 9', 3),
    (10, 'Title 10', 'Description 10', 4),
    (11, 'Title 11', 'Description 11', 4),
    (12, 'Title 12', 'Description 12', 4),
    (13, 'Title 13', 'Description 13', 5),
    (14, 'Title 14', 'Description 14', 5),
    (15, 'Title 15', 'Description 15', 5),
    (16, 'Title 16', 'Description 16', 6),
    (17, 'Title 17', 'Description 17', 6),
    (18, 'Title 18', 'Description 18', 6),
    (19, 'Title 19', 'Description 19', 7),
    (20, 'Title 20', 'Description 20', 7),
    (21, 'Title 21', 'Description 21', 7),
    (22, 'Title 22', 'Description 22', 8),
    (23, 'Title 23', 'Description 23', 8),
    (24, 'Title 24', 'Description 24', 8),
    (25, 'Title 25', 'Description 25', 9),
    (26, 'Title 26', 'Description 26', 9),
    (27, 'Title 27', 'Description 27', 9),
    (28, 'Title 28', 'Description 28', 10),
    (29, 'Title 29', 'Description 29', 10),
    (30, 'Title 30', 'Description 30', 10);

INSERT INTO comment(id, title, description, task_id)
    VALUES (1, 'Comment title 1', 'Comment description 1', 1),
    (2, 'Comment title 2', 'Comment description 2', 1),
    (3, 'Comment title 3', 'Comment description 3', 2),
    (4, 'Comment title 4', 'Comment description 4', 2),
    (5, 'Comment title 5', 'Comment description 5', 3),
    (6, 'Comment title 6', 'Comment description 6', 3),
    (7, 'Comment title 7', 'Comment description 7', 4),
    (8, 'Comment title 8', 'Comment description 8', 1),
    (9, 'Comment title 9', 'Comment description 9', 5),
    (10, 'Comment title 1§', 'Comment description 10', 5),
    (11, 'Comment title 11', 'Comment description 11', 6),
    (12, 'Comment title 12', 'Comment description 12', 6),
    (13, 'Comment title 13', 'Comment description 13', 7),
    (14, 'Comment title 14', 'Comment description 14', 7),
    (15, 'Comment title 15', 'Comment description 15', 8),
    (16, 'Comment title 16', 'Comment description 16', 8),
    (17, 'Comment title 17', 'Comment description 17', 9),
    (18, 'Comment title 18', 'Comment description 18', 9),
    (19, 'Comment title 19', 'Comment description 19', 10),
    (20, 'Comment title 20', 'Comment description 20', 10),
    (21, 'Comment title 21', 'Comment description 21', 11),
    (22, 'Comment title 22', 'Comment description 22', 11),
    (23, 'Comment title 23', 'Comment description 23', 12),
    (24, 'Comment title 24', 'Comment description 24', 12),
    (25, 'Comment title 25', 'Comment description 25', 13),
    (26, 'Comment title 26', 'Comment description 26', 13),
    (27, 'Comment title 27', 'Comment description 27', 14),
    (28, 'Comment title 28', 'Comment description 28', 14),
    (29, 'Comment title 29', 'Comment description 29', 15),
    (30, 'Comment title 30', 'Comment description 30', 15),
    (31, 'Comment title 31', 'Comment description 31', 16),
    (32, 'Comment title 32', 'Comment description 32', 16),
    (33, 'Comment title 33', 'Comment description 33', 17),
    (34, 'Comment title 34', 'Comment description 34', 17),
    (35, 'Comment title 35', 'Comment description 35', 18),
    (36, 'Comment title 36', 'Comment description 36', 18),
    (37, 'Comment title 37', 'Comment description 37', 19),
    (38, 'Comment title 38', 'Comment description 38', 19),
    (39, 'Comment title 39', 'Comment description 39', 20),
    (40, 'Comment title 40', 'Comment description 40', 20),
    (41, 'Comment title 41', 'Comment description 41', 21),
    (42, 'Comment title 42', 'Comment description 42', 21),
    (43, 'Comment title 43', 'Comment description 43', 22),
    (44, 'Comment title 44', 'Comment description 44', 22),
    (45, 'Comment title 45', 'Comment description 45', 23),
    (46, 'Comment title 46', 'Comment description 46', 23),
    (47, 'Comment title 47', 'Comment description 47', 24),
    (48, 'Comment title 48', 'Comment description 48', 24),
    (49, 'Comment title 49', 'Comment description 49', 25),
    (50, 'Comment title 50', 'Comment description 50', 25),
    (51, 'Comment title 51', 'Comment description 51', 26),
    (52, 'Comment title 52', 'Comment description 52', 26),
    (53, 'Comment title 53', 'Comment description 53', 27),
    (54, 'Comment title 54', 'Comment description 54', 27),
    (55, 'Comment title 55', 'Comment description 55', 28),
    (56, 'Comment title 56', 'Comment description 56', 28),
    (57, 'Comment title 57', 'Comment description 57', 29),
    (58, 'Comment title 58', 'Comment description 58', 29),
    (59, 'Comment title 59', 'Comment description 59', 30),
    (60, 'Comment title 60', 'Comment description 60', 30);

INSERT INTO sub_task(id, title, description, task_id)
    VALUES (1, '1', '1', 1),
     (2, '2', '2', 2),
     (3, '3', '3', 3),
     (4, '4', '4', 4),
     (5, '5', '5', 5),
     (6, '6', '6', 6),
     (7, '7', '7', 7),
     (8, '8', '8', 8),
     (9, '9', '9', 9),
     (10, '10', '10', 10),
     (11, '11', '11', 11),
     (12, '12', '12', 12),
     (13, '13', '13', 13),
     (14, '14', '14', 14),
     (15, '15', '15', 15),
     (16, '16', '16', 16),
     (17, '17', '17', 17),
     (18, '18', '18', 18),
     (19, '19', '19', 19),
     (20, '20', '20', 20),
     (21, '21', '21', 21),
     (22, '22', '22', 22),
     (23, '23', '23', 23),
     (24, '24', '24', 24),
     (25, '25', '25', 25),
     (26, '26', '26', 26),
     (27, '27', '27', 27),
     (28, '28', '28', 28),
     (29, '29', '29', 29),
     (30, '30', '30', 30);
