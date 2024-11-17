CREATE TABLE project
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE version
(
    id   BIGINT PRIMARY KEY,
    minor INTEGER,
    major INTEGER,
    patch INTEGER
);

CREATE TABLE estimation
(
    id   BIGINT PRIMARY KEY,
    original_man_days INTEGER
);

CREATE TABLE issue
(
    id          BIGINT PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(1000),
    project_id  BIGINT,
    version_id  BIGINT,
    estimation_id  BIGINT
);

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_project_id FOREIGN KEY (project_id) REFERENCES project (id);
ALTER TABLE issue
    ADD CONSTRAINT fk_issue_version_id FOREIGN KEY (version_id) REFERENCES version (id);
ALTER TABLE issue
    ADD CONSTRAINT fk_issue_estimation_id FOREIGN KEY (estimation_id) REFERENCES estimation (id);

CREATE SEQUENCE issue_seq START WITH 1 INCREMENT BY 50;

INSERT INTO project(id, name)
VALUES ((SELECT nextval('issue_seq')), 'Project 1');

INSERT INTO version(id, major, minor, patch)
VALUES (1, 18, 1, 27);

INSERT INTO estimation(id, original_man_days)
VALUES (1, 4);

INSERT INTO issue(id, title, description, project_id, version_id, estimation_id)
VALUES (1, 'Bug 1', 'This is the first bug', 1, 1, 1);
