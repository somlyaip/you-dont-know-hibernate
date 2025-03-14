CREATE SEQUENCE IF NOT EXISTS project_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS project_architect_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS issue_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS feature_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS estimation_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE project
(
    id                      BIGINT PRIMARY KEY,
    project_name            VARCHAR(255),
    status                  VARCHAR(30) NOT NULL,
    created_at              TIMESTAMP NOT NULL,
    project_architect_id    BIGINT
);

CREATE TABLE project_architect
(
    id                      BIGINT PRIMARY KEY,
    name                    VARCHAR(255),
    email                   VARCHAR(30),
    position                VARCHAR(30)
);

ALTER TABLE IF EXISTS project
    ADD CONSTRAINT FK_project_to_project_architect FOREIGN KEY (project_architect_id) REFERENCES project_architect;

CREATE TABLE issue
(
    id                      BIGINT PRIMARY KEY,
    title                   VARCHAR(255),
    description             VARCHAR(1000),
    status                  VARCHAR(30) NOT NULL,
    issue_level             VARCHAR(30) NOT NULL,
    created_at              TIMESTAMP NOT NULL,
    project_id              BIGINT
);

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_project_id FOREIGN KEY (project_id) REFERENCES project (id);

CREATE TABLE feature
(
    id                      BIGINT PRIMARY KEY,
    title                   VARCHAR(255),
    description             VARCHAR(1000),
    status                  VARCHAR(30) NOT NULL,
    created_at              TIMESTAMP NOT NULL,
    project_id              BIGINT
);

ALTER TABLE feature
    ADD CONSTRAINT fk_feature_project_id FOREIGN KEY (project_id) REFERENCES project (id);

CREATE TABLE estimation
(
    id                      BIGINT PRIMARY KEY,
    original_man_days       INTEGER NOT NULL
);

ALTER TABLE issue
    ADD COLUMN estimation_id    BIGINT;

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_estimation_id FOREIGN KEY (estimation_id) REFERENCES estimation (id);
