INSERT IGNORE INTO `role`(`id`, `name`) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO `role`(`id`, `name`) VALUES (10, 'ROLE_USER');

INSERT IGNORE INTO `person`(`id`, `username`, `password`, `full_name`, `year_of_birth`) VALUES (1,'ad','$2a$10$ZPlJBtwTbgGcsUcZ4CeOGOEuc3fGqHYJuR8B4Ba/55IdnjkPKRgOC','ad',1);
INSERT IGNORE INTO `person_role`(`person_id`, `role_id`) VALUES ('1','1');
INSERT IGNORE INTO `person_role`(`person_id`, `role_id`) VALUES ('1','10');