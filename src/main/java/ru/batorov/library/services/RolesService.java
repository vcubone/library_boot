package ru.batorov.library.services;


import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Role;
import ru.batorov.library.repositories.RolesRepository;
import ru.batorov.library.util.exceptions.RoleNotFoundException;

@Service
@Transactional(readOnly = true)
public class RolesService {
	private final RolesRepository rolesRepository;

	public RolesService(RolesRepository rolesRepository) {
		this.rolesRepository = rolesRepository;
	}
	
	public List<Role> all(){
		return rolesRepository.findAll();
	}
	Role getRoleByName(String name)
	{
		return rolesRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException(name));
	}
}
