delete from person;
ALTER table person ALTER column id restart with 1;

INSERT INTO `person`(`full_name`, `year_of_birth`, `username`, `password`) VALUES ('test', 12, 'test','$2a$10$s.ajh/jGEcRwZCaZznlASOfiuAF1QaXET2Op2jaSYjHtEi1hNCWim'),
('ad', 100, 'ad','$2a$10$RbI7AJVYKrHzcXWXhfRUbOA4..Il4Pdj9UFrP7V8C1JUPme1Ywo0q');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('1','10');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','10');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','1');