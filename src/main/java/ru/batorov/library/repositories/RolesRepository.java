package ru.batorov.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.batorov.library.models.Role;

public interface RolesRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(String name);
}
