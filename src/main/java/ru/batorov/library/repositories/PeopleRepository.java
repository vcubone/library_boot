package ru.batorov.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.batorov.library.models.Person;

public interface PeopleRepository extends JpaRepository<Person, Integer>{
    List<Person> findByFullName(String name);
    Optional<Person> findByUsername(String username);
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roles")
    List<Person> findAllWithRoles();
    
    @Query(
        value = "SELECT distinct p.id, p.username, r.id, r.name from person p left join person_role pr on p.id = pr.person_id LEFT JOIN role r on r.id = pr.role_id where p.id = :personId",
        nativeQuery = true
    )
    @Deprecated
    Person getAuthenticationsInformation(Integer personId);
}
