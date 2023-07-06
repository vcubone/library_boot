package ru.batorov.library.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.repositories.BookRepository;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    public List<Book> all() {
        return all(false);
    }
    //sort
    public List<Book> all(Boolean sortByYear) {
        return sortByYear? bookRepository.findAll(Sort.by("releaseYear")) :bookRepository.findAll();
    }
    //sort + pageable
    public List<Book> all(Boolean sortByYear, int page, int itemsPerPage) {
        return sortByYear? bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("releaseYear"))).getContent() 
            :bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }
    
    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }
    
    public Book show(int bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }
    
    @Transactional
    public void update(int bookId, Book updatedBook)
    {
        Book bookToBeUpdated = bookRepository.findById(bookId).orElse(null);
        if (bookToBeUpdated == null)
            return;
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        updatedBook.setBookId(bookId);
        updatedBook.setTakeTime(bookToBeUpdated.getTakeTime());
        bookRepository.save(updatedBook);
    }
    
    @Transactional
    public void delete(int bookId)
    {
        bookRepository.deleteById(bookId);;
    }
    
    @Transactional
    public void addOwner(int bookId, Person person)
    {
        bookRepository.findById(bookId).ifPresent(book->{
            book.setOwner(person);
            book.setTakeTime(new Date());
        });
    }
    
    @Transactional
    public void deleteOwner(int bookId)
    {
        bookRepository.findById(bookId).ifPresent(book-> 
            book.setOwner(null)
        );
    }
    
    public List<Book> findByTitleStartingWith(String findRequest)
    {
        return bookRepository.findByTitleStartingWith(findRequest);
    }
}