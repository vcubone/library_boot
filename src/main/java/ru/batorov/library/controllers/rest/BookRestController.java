package ru.batorov.library.controllers.rest;

import static ru.batorov.library.util.AuthenticationHelper.*;
import static ru.batorov.library.util.DTOConvert.*;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.batorov.library.dto.book.BookAdminDTO;
import ru.batorov.library.dto.book.BookCreationDTO;
import ru.batorov.library.dto.book.BookOwnerDTO;
import ru.batorov.library.dto.book.BookUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.BookService;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.exceptions.ErrorsGetter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "The Books API. Contains all the operations that can be performed with a book.")
public class BookRestController {
    private final BookService bookService;
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    public BookRestController(BookService bookService, PeopleService peopleService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    @Operation(summary = "Gets all books", description = "You can sort books by year or get required page with various amount of books in one page", tags = "Books")
    public Collection<BookUserDTO> all(
            @RequestParam(value = "sortByYear", required = false) boolean sortByYear,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
        System.out.println("test\n");
        List<Book> books = page == null || itemsPerPage == null ? bookService.all(sortByYear)
                : bookService.all(sortByYear, page, itemsPerPage);
        return convertToBookUserDTOCollection(books, modelMapper);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for books that contains request string", tags = "Books")
    public Collection<BookUserDTO> search(@RequestParam(value = "findRequest") String findRequest) {
        if (findRequest != null && !findRequest.equals(""))
            return convertToBookUserDTOCollection(bookService.findBooksByTitleContaining(findRequest), modelMapper);
        return null;
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Gets book with required id", description = "Owners field depends on his existence and your role", tags = "Books")
    public BookOwnerDTO show(@PathVariable("bookId") int bookId, @ApiIgnore Authentication authentif) {
        Book book = bookService.getBookByIdWithOwner(bookId);
        if (book.getOwner() != null) {
            if (authentif != null && authentif.isAuthenticated())
            {
                // not admin or owner -> empty person
                if(!hasRoleByAuthentication(authentif, "ROLE_ADMIN") && getUsernameByAuthentication(authentif) != book.getOwner().getUsername())
                    book.setOwner(new Person());
            } else
            book.setOwner(new Person());
        }
        BookOwnerDTO bookOwnerDTO = convertToBookOwnerDTO(book, modelMapper);
        return bookOwnerDTO;
    }

    @PostMapping("/new")
    @Operation(summary = "Creates a new book", description = "Admins only", tags = "Books", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "400", description = "Bad input values")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid BookCreationDTO bookCreationDTO,
            @ApiIgnore BindingResult bindingResult) {
        Book book = converToBook(bookCreationDTO, modelMapper);
        if (bindingResult.hasErrors())
            throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));
        bookService.create(book);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{bookId}/edit")
    @Operation(summary = "Updates book with required id", description = "Admins only", tags = "Books", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "400", description = "Bad input values")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid BookAdminDTO bookAdminDTO,
            @PathVariable("bookId") int bookId,
            @ApiIgnore BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));
        bookService.update(bookId, converToBook(bookAdminDTO, modelMapper));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    @Operation(summary = "Deletes book with required id", description = "Admins only", tags = "Books", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<HttpStatus> delete(@PathVariable("bookId") int bookId) {
        bookService.delete(bookId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{bookId}/addowner")
    @Operation(summary = "Adds owner to books with required id", description = "ownersId is admins parameter", tags = "Books", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<HttpStatus> addOwner(
            @RequestParam(value = "ownersId", required = false) Integer ownersId,
            @PathVariable("bookId") int bookId,
            @ApiIgnore Authentication authentif) {
        Book book = bookService.getBookById(bookId);
        if (book.getOwner() == null) {
            int personId = getUserIdByAuthentication(authentif);
            if (ownersId != null) {
                if (hasRoleByAuthentication(authentif, "ROLE_ADMIN") || personId == ownersId)
                    personId = ownersId;
                else
                    throw new AccessDeniedException("Only admins can give books to other persons");
            }
            bookService.addOwner(bookId, peopleService.getPersonById(personId));
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{bookId}/release")
    @Operation(summary = "Deletes Owner from book with required id", tags = "Books", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<HttpStatus> deleteOwner(@PathVariable("bookId") int bookId,
            @ApiIgnore Authentication authentif) {
        // если админ это делает или id, совпадающим с владельцем книги
        if (hasRoleByAuthentication(authentif, "ROLE_ADMIN") ||
                (getUserIdByAuthentication(authentif) == bookService.getBooksOwner(bookId).getId()))
            bookService.deleteOwner(bookId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
