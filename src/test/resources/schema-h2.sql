create table IF NOT EXISTS person(
    id int GENERATED always AS IDENTITY, PRIMARY KEY(id),
    username varchar(100) not null UNIQUE,
    password varchar(70) not null,
    full_name varchar(100) not null,
    year_of_birth int not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP DEFAULT ON NULL,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT ON NULL,
    version int not null DEFAULT 1
);;
CREATE TABLE if NOT EXISTS role(
    id int, PRIMARY KEY(id),
    name varchar(20) UNIQUE not null
);;
CREATE TABLE if NOT EXISTS person_role(
    person_id int, FOREIGN KEY(person_id) REFERENCES person(id) ON DELETE CASCADE,
    role_id int, FOREIGN KEY(role_id) REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY(person_id, role_id)
);;
create table IF NOT EXISTS book(
    id int GENERATED always AS IDENTITY, PRIMARY KEY(id),
    person_id int, FOREIGN KEY(person_id) REFERENCES person(id) on DELETE set null,
    title varchar(100) not null,
    author varchar(100) not null,
    release_year int not null,
	take_time timestamp DEFAULT CURRENT_TIMESTAMP DEFAULT ON NULL,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP DEFAULT ON NULL,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	DEFAULT ON NULL
);;