package ru.batorov.library.services;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.repositories.BookRepository;
import ru.batorov.library.util.CopyHelper;
import ru.batorov.library.util.exceptions.BookNotFoundException;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> all() {
        return all(false);
    }

    // sort
    public List<Book> all(Boolean sortByYear) {
        return sortByYear ? bookRepository.findAll(Sort.by("releaseYear")) : bookRepository.findAll();
    }

    // sort + pageable
    public List<Book> all(Boolean sortByYear, int page, int itemsPerPage) {
        return sortByYear
                ? bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("releaseYear"))).getContent()
                : bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    public Book findBookById(int bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public Book getBookById(int bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }
    
    public Book getBookByIdWithOwner(int bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        Hibernate.initialize(book.getOwner());
        return book;
    }

    @Transactional
    public void update(int bookId, Book updatedBook) {
        Book bookToBeUpdated = getBookById(bookId);
        if (bookToBeUpdated == null)
            return;
        CopyHelper.copyNotNullProperties(updatedBook, bookToBeUpdated);
        bookRepository.save(bookToBeUpdated);
    }

    @Transactional
    public void delete(int bookId) {
        bookRepository.deleteById(bookId);
    }

    @Transactional
    public void addOwner(int bookId, Person person) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setOwner(person);
            book.setTakeTime(new Date());
        });
    }

    @Transactional
    public void deleteOwner(int bookId) {
        bookRepository.findById(bookId).ifPresent(book -> book.setOwner(null));
    }

    public List<Book> findBooksByTitleContaining(String findRequest) {
        return bookRepository.findByTitleContaining(findRequest);
    }

    public Person findBooksOwner(int bookId) {
        Book book = getBookById(bookId);
        Hibernate.initialize(book.getOwner());
        return book.getOwner() == null ? null : book.getOwner();
    }

    public Person getBooksOwner(int bookId) {
        Book book = getBookById(bookId);
        Hibernate.initialize(book.getOwner());
        return book.getOwner();
    }
}