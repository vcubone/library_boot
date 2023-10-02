INSERT INTO role(id, name) VALUES (1, 'ROLE_ADMIN') on conflict (id) do nothing;;
INSERT INTO role(id, name) VALUES (10, 'ROLE_USER') on conflict (id) do nothing;;

INSERT INTO person(id, username, password, full_name, year_of_birth) VALUES (1,'ad','$2a$10$ZPlJBtwTbgGcsUcZ4CeOGOEuc3fGqHYJuR8B4Ba/55IdnjkPKRgOC','ad',1) on conflict (id) do nothing;;
INSERT INTO person_role (person_id, role_id) VALUES ('1','1') on conflict (person_id, role_id) do nothing;;
INSERT INTO person_role(person_id, role_id) VALUES ('1','10') on conflict (person_id, role_id) do nothing;;
DO
$do$
BEGIN
   IF nextval('person_id_seq'::regclass) < 10 THEN
      ALTER SEQUENCE person_id_seq RESTART WITH 10;
   END IF;
END
$do$
