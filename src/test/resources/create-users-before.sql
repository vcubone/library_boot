delete from person;
ALTER table person ALTER column id restart with 1;

INSERT INTO `person`(`full_name`, `year_of_birth`, `username`, `password`) VALUES ('testing', 12, 'testing','$2a$10$.bLKvVOOkjEk74DjekWaPu.ifgu.golwZHgb/nVi0RSpPE/GZlIwq'),
('ad', 100, 'ad','$2a$10$.bLKvVOOkjEk74DjekWaPu.ifgu.golwZHgb/nVi0RSpPE/GZlIwq');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('1','10');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','10');
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','1');