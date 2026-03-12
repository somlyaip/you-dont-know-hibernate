CREATE TABLE branch
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(255),
    development_attributes_id BIGINT
);

CREATE TABLE commit
(
    id   BIGINT PRIMARY KEY,
    author VARCHAR(255),
    message VARCHAR(255),
    development_attributes_id BIGINT
);

CREATE TABLE merge_request
(
    id   BIGINT PRIMARY KEY
);

CREATE TABLE development_attributes
(
    id   BIGINT PRIMARY KEY,
    merge_request_id BIGINT
);

ALTER TABLE commit
    ADD CONSTRAINT fk_commit_development_attributes_id FOREIGN KEY (development_attributes_id) REFERENCES development_attributes (id);

ALTER TABLE branch
    ADD CONSTRAINT fk_branch_development_attributes_id FOREIGN KEY (development_attributes_id) REFERENCES development_attributes (id);

ALTER TABLE development_attributes
    ADD CONSTRAINT fk_development_attributes_merge_request_id FOREIGN KEY (merge_request_id) REFERENCES merge_request (id);

CREATE TABLE issue
(
    id          BIGINT PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(1000),
    development_attributes_id BIGINT
);

ALTER TABLE issue
    ADD CONSTRAINT fk_issue_development_attributes_id FOREIGN KEY (development_attributes_id) REFERENCES development_attributes (id);

INSERT INTO merge_request(id)
VALUES (1);

INSERT INTO development_attributes(id, merge_request_id)
VALUES (1, 1);

INSERT INTO branch(id, name, development_attributes_id)
VALUES (1, 'feature/1-awesome-form', 1);

INSERT INTO commit(id, author, message, development_attributes_id)
VALUES (1, 'john.doe@mail.com', '#1 feat: add awesome form to register inquiry', 1);

INSERT INTO issue(id, title, description, development_attributes_id)
VALUES (1, 'Awesome form', 'This is an awesome form', 1);
