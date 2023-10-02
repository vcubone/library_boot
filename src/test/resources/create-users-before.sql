delete from person;;
ALTER table person ALTER column id restart with 1;;

INSERT INTO `person`(`full_name`, `year_of_birth`, `username`, `password`) VALUES 
('ad', 100, 'ad','$2a$10$RbI7AJVYKrHzcXWXhfRUbOA4..Il4Pdj9UFrP7V8C1JUPme1Ywo0q'),
('user1', 2000, 'user1','$2a$10$MyMhmcssHNYFRXzN6xgsr.GgmwyPS.jUgUdXvN2lEO7W2s9PX79FS'),
('user2', 2005, 'user2','$2a$10$5wOUcUYoInIKgFoRyN.E5OyGkACeevc57lhzVViskOgjBeriFP.nu');;
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('1','10');;
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('1','1');;
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('2','10');;
INSERT INTO `person_role`(`person_id`, `role_id`) VALUES ('3','10');;