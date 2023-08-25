package ru.batorov.library.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.JwtBlackList;
import ru.batorov.library.repositories.JwtBlackListRepository;

@Service
@Transactional(readOnly = true)
public class JwtBlackListService {
	private final JwtBlackListRepository jwtBlackListRepository;

	public JwtBlackListService(JwtBlackListRepository jwtBlackListRepository) {
		this.jwtBlackListRepository = jwtBlackListRepository;
	}
	
	public List<JwtBlackList> all(){
		return jwtBlackListRepository.findAll();
	}
	
	// @Transactional
	// public void save()
}
