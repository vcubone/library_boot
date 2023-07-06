package ru.batorov.library.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.batorov.library.models.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
    List<Book> findByTitleStartingWith(String findRequest);
}
