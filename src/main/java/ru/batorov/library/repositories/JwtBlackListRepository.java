package ru.batorov.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.batorov.library.models.Book;
import ru.batorov.library.models.JwtBlackList;

public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Integer> {
	
}
