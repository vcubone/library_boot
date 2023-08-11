INSERT INTO `person`(`fullName`, `age`) VALUES ('test', 12);
INSERT INTO `credentials`(`personId`, `username`, `password`, `role`) VALUES (1 ,'test','$2a$10$B98M1CPBFchhFzgDUO8dLuw/C.rgUl6xNtPzFAMP6aEVW76EZT07q','ROLE_USER');

INSERT INTO `book`(`title`, `author`, `releaseYear`) VALUES ('title','author', 500);
select * from book;