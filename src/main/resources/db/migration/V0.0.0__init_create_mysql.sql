CREATE TABLE activity (
    activity_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(5000),
    status VARCHAR(100) NOT NULL,
    priority_id INTEGER,

    start_date DATETIME NOT NULL,
    end_date DATETIME,

    created_by VARCHAR(255),
    creation_date DATETIME,
    modified_by VARCHAR(255),
    modification_date DATETIME
);
CREATE UNIQUE INDEX unique_key_activity_name ON activity(name(255));

CREATE TABLE priority (
    priority_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX unique_key_priority_identifier ON priority(identifier(255));