UPDATE book SET person_id = null;

UPDATE `book` SET `person_id` = '2' WHERE `book`.`id` = 1;
UPDATE `book` SET `person_id` = '2' WHERE `book`.`id` = 2;