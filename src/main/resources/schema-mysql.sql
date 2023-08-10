create table IF NOT EXISTS person(
    personId int AUTO_INCREMENT, PRIMARY KEY(personId),
    fullName varchar(100) not null UNIQUE,
    age int not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	
);
create table IF NOT EXISTS book(
    bookId int AUTO_INCREMENT,
    PRIMARY KEY(bookId),
    personId int,
    FOREIGN KEY(personId) REFERENCES person(personId) on DELETE set null,
    title varchar(100) not null,
    author varchar(100) not null,
    releaseYear int not null,
	takeTime timestamp DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	
);
CREATE TABLE if NOT EXISTS credentials(
    personId int, PRIMARY KEY(personId), FOREIGN KEY(personId) REFERENCES person(personId) on DELETE cascade,
    username varchar(100) not null UNIQUE,
    password varchar(70) not null,
    role varchar(20) not null
)