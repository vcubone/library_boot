package ru.batorov.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.batorov.library.models.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {
	Optional<Credentials> findByUsername(String username);
    List<Credentials> findByRole(String role);
}
