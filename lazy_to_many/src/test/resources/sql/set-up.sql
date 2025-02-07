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
    project_id  BIGINT,
    version_id  BIGINT,
    estimation_id  BIGINT
);

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_project_id FOREIGN KEY (project_id) REFERENCES project (id);

CREATE SEQUENCE project_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE issue_seq START WITH 1 INCREMENT BY 50;
