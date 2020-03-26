CREATE TABLE activity (
    activity_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(5000),
    status VARCHAR(100) NOT NULL,
    priority_id INTEGER,

    start_date DATETIME NOT NULL,
    end_date DATETIME,
    activity_notification_id INTEGER,

    created_by VARCHAR(255),
    creation_date DATETIME,
    modified_by VARCHAR(255),
    modification_date DATETIME
);

CREATE TABLE activity_notification (
    activity_notification_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    activity_id INTEGER,
    email VARCHAR(255),
    sms_id VARCHAR(255),
    notify_datetime DATETIME NOT NULL,
    snooze_repeat SMALLINT,
    snooze_repeat_interval SMALLINT,
    FOREIGN KEY (activity_id)
        REFERENCES activity(activity_id)
        ON DELETE CASCADE
);

CREATE TABLE priority (
    priority_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX unique_key_priority_identifier ON priority(identifier(255));

