TRUNCATE TABLE flyway_schema_history;

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE activity AUTO_INCREMENT = 1;
TRUNCATE TABLE activity;
ALTER TABLE activity_assignee AUTO_INCREMENT = 1;
TRUNCATE TABLE activity_assignee;
ALTER TABLE activity_notification AUTO_INCREMENT = 1;
TRUNCATE TABLE activity_notification;
ALTER TABLE activity_content_resource AUTO_INCREMENT = 1;
TRUNCATE TABLE activity_content_resource;
ALTER TABLE activity_entity_link AUTO_INCREMENT = 1;
TRUNCATE TABLE activity_entity_link;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO activity (name, description, status, priority_id, start_date, created_by, modification_date)
VALUES ('test 1', 'test activity 1', 'DRAFT', 1, '2100-04-06T16:27:00', '65189df4-a121-4557-8baa-c7d5b79dd607', now());

COMMIT;