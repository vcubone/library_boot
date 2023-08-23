delete from book;
ALTER table book ALTER column id restart with 1;

INSERT INTO `book`(`title`, `author`, `release_year`) VALUES ('The Hound of the Baskervilles','	Arthur Conan Doyle', 1902);
INSERT INTO `book`(`title`, `author`, `release_year`) VALUES ('War and Peace','	Leo Tolstoy', 1865);
INSERT INTO `book`(`title`, `author`, `release_year`) VALUES ('Effective Java','Joshua Bloch', 2001);