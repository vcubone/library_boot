create table IF NOT EXISTS person(
    id serial PRIMARY KEY,
    username varchar(100) not null UNIQUE,
    password varchar(70) not null,
    full_name varchar(100) not null,
    year_of_birth int not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    version int not null DEFAULT 1
);;
CREATE TABLE if NOT EXISTS role(
    id int PRIMARY KEY,
    name varchar(20) UNIQUE not null
);;
CREATE TABLE if NOT EXISTS person_role(
    person_id int REFERENCES person(id) ON DELETE CASCADE,
    role_id int REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY(person_id, role_id)
);;
create table IF NOT EXISTS book(
    id serial PRIMARY KEY,
    person_id int REFERENCES person(id) on DELETE set null,
    title varchar(100) not null,
    author varchar(100) not null,
    release_year int not null,
	take_time timestamp DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP	
);;
CREATE OR REPLACE FUNCTION update_changetimestamp_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = now(); 
   RETURN NEW;
END;
$$ language 'plpgsql';;
CREATE or replace TRIGGER update_ab_changetimestamp BEFORE UPDATE
    ON person FOR EACH ROW EXECUTE PROCEDURE 
    update_changetimestamp_column();;
CREATE or replace TRIGGER update_ab_changetimestamp BEFORE UPDATE
    ON book FOR EACH ROW EXECUTE PROCEDURE 
    update_changetimestamp_column();;