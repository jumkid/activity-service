CREATE TABLE IF NOT EXISTS activity (
    activity_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(5000),
    status VARCHAR(100) NOT NULL,
    priority_id INTEGER,

    start_date DATETIME NOT NULL,
    end_date DATETIME,

    created_by VARCHAR(255),
    creation_date DATETIME(3),
    modified_by VARCHAR(255),
    modification_date DATETIME(3)
);

CREATE TABLE IF NOT EXISTS activity_assignee (
    activity_assignee_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    activity_id INTEGER,
    assignee_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (activity_id)
        REFERENCES activity(activity_id)
        ON DELETE CASCADE
);
CREATE UNIQUE INDEX unique_key_activity_assignee_id ON activity_assignee(activity_id, assignee_id);

CREATE TABLE IF NOT EXISTS activity_notification (
    activity_notification_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    activity_id INTEGER,
    notify_before INTEGER,
    notify_before_unit VARCHAR(10),
    trigger_datetime DATETIME NOT NULL,
    expired BOOL NOT NULL DEFAULT true,
    FOREIGN KEY (activity_id)
        REFERENCES activity(activity_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS activity_content_resource (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    activity_id INTEGER,
    content_resource_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (activity_id)
        REFERENCES activity(activity_id)
        ON DELETE CASCADE
);
CREATE UNIQUE INDEX unique_key_activity_assignee_id ON activity_content_resource(activity_id, content_resource_id);

CREATE TABLE IF NOT EXISTS activity_entity_link (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    activity_id INTEGER,
    entity_name VARCHAR(10) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    FOREIGN KEY (activity_id)
        REFERENCES activity(activity_id)
        ON DELETE CASCADE
);
CREATE UNIQUE INDEX unique_key_activity_entity_link ON activity_entity_link(activity_id, entity_name, entity_id);

CREATE TABLE IF NOT EXISTS priority (
    priority_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX unique_key_priority_identifier ON priority(identifier(255));
