package ru.batorov.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.batorov.library.models.Role;

public interface RolesRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(String name);
	//@Query("SELECT p.roles FROM Person p LEFT JOIN p.roles where p.id = :personId")
	@Query(
		value = "select r.* from role r left join person_role pr on r.id = pr.role_id where pr.person_id = :personId",
		nativeQuery = true
	)
	List<Role> findRolesByPersonId(Integer personId);
}
