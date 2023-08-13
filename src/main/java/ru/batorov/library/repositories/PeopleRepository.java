package ru.batorov.library.repositories;

import java.util.Collection;
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
}
