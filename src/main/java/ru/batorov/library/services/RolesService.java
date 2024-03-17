package ru.batorov.library.services;


import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Role;
import ru.batorov.library.repositories.RolesRepository;
import ru.batorov.library.util.exceptions.PersonNotFoundException;
import ru.batorov.library.util.exceptions.RoleNotFoundException;

@Service
@Transactional(readOnly = true)
public class RolesService {
	private final RolesRepository rolesRepository;

	public RolesService(RolesRepository rolesRepository) {
		this.rolesRepository = rolesRepository;
	}
	
	/**
	 * Return list of all roles.
	 * 
	 * @return list of all roles.
	 */
	public List<Role> all(){
		return rolesRepository.findAll();
	}
	
	/**
	 * Return role by its name if present, otherwise throw
	 * RoleNotFoundException.
	 * 
	 * @param name must not be bull
	 * @return role.
	 * @throws PersonNotFoundException  if no role is present.
     * @throws IllegalArgumentException - if {@literal name} is
     *                                  {@literal null}.
	 */
	public Role getRoleByName(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("Name must not be null!");
		return rolesRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException(name));
	}
	
	/**
	 * Return person's roles.
	 * 
	 * @param person_id must not be null.
	 * @return person's roles.
	 * 
	 */
	public List<Role> getPersonsRoles(Integer person_id)
	{
		if (person_id == null) 
			throw new IllegalArgumentException("person_id must not be null!");
		return rolesRepository.findRolesByPersonId(person_id);
	}
}
