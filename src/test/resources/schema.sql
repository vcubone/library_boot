


create table IF NOT EXISTS person(
    personId int AUTO_INCREMENT,
    PRIMARY KEY(personId),
    fullName varchar(100) not null UNIQUE,
    age int not null,
    created_at timestamp,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_who	varchar(100)
);
create table IF NOT EXISTS book(
    bookId int AUTO_INCREMENT,
    PRIMARY KEY(bookId),
    personId int,
    FOREIGN KEY(personId) REFERENCES person(personId) on DELETE set null,
    title varchar(100) not null,
    author varchar(100) not null,
    releaseYear int not null,
	takeTime timestamp
);