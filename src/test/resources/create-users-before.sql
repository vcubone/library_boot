delete from person;
ALTER table person ALTER column id restart with 1;

INSERT INTO `person`(`full_name`, `year_of_birth`, `username`, `password`) VALUES ('test', 12, 'test','$2a$10$B98M1CPBFchhFzgDUO8dLuw/C.rgUl6xNtPzFAMP6aEVW76EZT07q'),
('ad', 100, 'ad','$2a$10$ZPlJBtwTbgGcsUcZ4CeOGOEuc3fGqHYJuR8B4Ba/55IdnjkPKRgOC');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('1','10');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','10');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','1');