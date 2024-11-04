CREATE TABLE project
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(255)
);


CREATE TABLE issue
(
    id          BIGINT PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(1000),
    project_id  BIGINT
);

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_project_id FOREIGN KEY (project_id) REFERENCES project (id);

INSERT INTO project(id, name)
VALUES (1, 'Project 1');

INSERT INTO issue(id, title, description, project_id)
VALUES (1, 'Bug 1', 'This is the first bug', 1);