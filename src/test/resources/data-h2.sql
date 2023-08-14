INSERT INTO `role`(`id`, `name`) VALUES (1, 'ROLE_ADMIN');
INSERT INTO `role`(`id`, `name`) VALUES (10, 'ROLE_USER');

INSERT INTO `person`(`full_name`, `year_of_birth`, `username`, `password`) VALUES ('test', 12, 'test','$2a$10$B98M1CPBFchhFzgDUO8dLuw/C.rgUl6xNtPzFAMP6aEVW76EZT07q');

INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('1','10');

INSERT INTO `book`(`title`, `author`, `release_year`) VALUES ('title','author', 500);