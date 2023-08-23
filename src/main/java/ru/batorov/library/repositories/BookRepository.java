package ru.batorov.library.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.batorov.library.models.Book;


public interface BookRepository extends JpaRepository<Book, Integer>{
    List<Book> findByTitleContaining(String findRequest);
}
