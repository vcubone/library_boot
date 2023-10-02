package ru.batorov.library.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.repositories.BookRepository;
import ru.batorov.library.util.CopyHelper;
import ru.batorov.library.util.exceptions.BookNotFoundException;
import ru.batorov.library.util.exceptions.PersonNotFoundException;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Return list of all books.
     * 
     * @return list of all books.
     */
    public List<Book> all() {
        return all(false);
    }

    /**
     * Return list of all books sorted by the given options.
     * 
     * @param sortByYear if true then sort in ascending order.
     * @return list of all books sorted by the given options.
     */
    public List<Book> all(boolean sortByYear) {
        return sortByYear ? bookRepository.findAll(Sort.by("releaseYear")) : bookRepository.findAll();
    }

    /**
     * Returns a list of books on the given page sorted by the given options.
     * 
     * @param sortByYear   if true then sort in ascending order.
     * @param page         zero-based page index.
     * @param itemsPerPage the size of the page to be returned.
     * @return list of books.
     */
    public List<Book> all(boolean sortByYear, Integer page, Integer itemsPerPage) {
        return sortByYear
                ? bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("releaseYear"))).getContent()
                : bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    /**
     * Save a given book.
     * 
     * @param book must not be {@literal null}.
     * @return the saved book; will never be {@literal null}.
     * @throws IllegalArgumentException          in case the given {@literal book}
     *                                           is {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    @Transactional
    public Book create(Book book) {
        book.setCreated_at(LocalDateTime.now());
        book.setUpdated_at(book.getCreated_at());
        bookRepository.save(book);
        return book;
    }

    /**
     * Return book by its bookId if present, otherwise return null.
     * 
     * @param bookId must not be {@literal null}.
     * @return book if present, otherwise {@literal null}.
     * @throws IllegalArgumentException - if {@literal bookId} is
     *                                  {@literal null}.
     */
    public Book findBookById(Integer bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    /**
     * Return book by its bookId if present, otherwise throw
     * BookNotFoundException.
     * 
     * @param bookId must not be {@literal null}.
     * @return book.
     * @throws BookNotFoundException    if no book is present.
     * @throws IllegalArgumentException - if {@literal bookId} is
     *                                  {@literal null}.
     */
    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }

    /**
     * Return book by its bookId if present, otherwise throw
     * BookNotFoundException. Contains its owner if it exists.
     * 
     * @param bookId must not be {@literal null}.
     * @return book that contain its owner.
     * @throws BookNotFoundException    if no book is present.
     * @throws IllegalArgumentException - if {@literal bookId} is
     *                                  {@literal null}.
     */
    public Book getBookByIdWithOwner(Integer bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        Hibernate.initialize(book.getOwner());
        return book;
    }

    /**
     * Update existing book with fields of incoming book.
     * 
     * @param bookId      of existing book, must not be {@literal null}.
     * @param updatedBook with changed fields, must not be {@literal null}.
     * @throws IllegalArgumentException          - if {@literal bookId} or
     *                                           {@literal updatedBook} is
     *                                           {@literal null}.
     * @throws OptimisticLockingFailureException when the entity uses optimistic
     *                                           locking and has a version attribute
     *                                           with
     *                                           a different value from that found
     *                                           in the persistence store. Also
     *                                           thrown if the entity is assumed to
     *                                           be
     *                                           present but does not exist in the
     *                                           database.
     */
    @Transactional
    public void update(Integer bookId, Book updatedBook) {
        Book bookToBeUpdated = getBookById(bookId);
        CopyHelper.copyNotNullProperties(updatedBook, bookToBeUpdated);
        bookRepository.save(bookToBeUpdated);
    }

    /**
     * Deletes the book with the given book_id.
     * <p>
     * If the book is not found it is silently ignored.
     * 
     * @param book_id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal book_id} is
     *                                  {@literal null}
     */
    @Transactional
    public void delete(Integer book_id) {
        bookRepository.deleteById(book_id);
    }

    /**
     * Add owner to the book with required book_id.
     * 
     * @param book_id must not be {@literal null}
     * @param person  must not be {@literal null}
     * @throws IllegalArgumentException - if {@literal book_id}, {@literal person} or {@literal person.id}
     *                                  is {@literal null}.
     */
    @Transactional
    public void addOwner(Integer book_id, Person person) {
        bookRepository.findById(book_id).ifPresent(book -> {
            book.setOwner(person);
            book.setTakeTime(new Date());
        });
    }

    /**
     * Delete owner of the book
     * <p>
     * If the person don't have given role it is silently ignored.
     * 
     * @param bookId must not be {@literal null}
     */
    @Transactional
    public void deleteOwner(Integer bookId) {
        bookRepository.findById(bookId).ifPresent(book -> book.setOwner(null));
    }

    /**
     * Return books which title contains reqired string
     * 
     * @param findRequest 
     * @return books with findRequest in title
     */
    public List<Book> findBooksByTitleContaining(String findRequest) {
        return bookRepository.findByTitleContaining(findRequest);
    }

    /**
     * Returns book owner if exists, otherwise null.
     * 
     * @param bookId must not be null.
     * @return person who borrows the book.
     * @throws BookNotFoundException    if no book is present.
     * @throws IllegalArgumentException - if {@literal bookId} is
     *                                  {@literal null}.
     */
    public Person findBooksOwner(Integer bookId) {
        Book book = getBookById(bookId);
        Hibernate.initialize(book.getOwner());
        return book.getOwner();
    }

    /**
     * Returns book owner if exists, otherwise throw PersonNotFoundException.
     * 
     * @param bookId must not be null.
     * @return person who borrows the book.
     * @throws BookNotFoundException    if no book is present.
     * @throws PersonNotFoundException  if no owner is present.
     * @throws IllegalArgumentException - if {@literal bookId} is
     *                                  {@literal null}.
     */
    public Person getBooksOwner(Integer bookId) {
        Book book = getBookById(bookId);
        Hibernate.initialize(book.getOwner());
        if (book.getOwner() == null) throw new PersonNotFoundException("Book dont have the owner");
        return book.getOwner();
    }
}