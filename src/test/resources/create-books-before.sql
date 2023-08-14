delete from book;
ALTER table book ALTER column id restart with 1;

INSERT INTO `book`(`title`, `author`, `releaseYear`) VALUES ('title3','author3', 300);
INSERT INTO `book`(`title`, `author`, `releaseYear`) VALUES ('title1','author1', 100);
INSERT INTO `book`(`title`, `author`, `releaseYear`) VALUES ('title2','author2', 200);