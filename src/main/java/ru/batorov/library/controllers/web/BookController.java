package ru.batorov.library.controllers.web;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import ru.batorov.library.dto.book.BookAdminDTO;
import ru.batorov.library.dto.person.PersonUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.BookService;
import ru.batorov.library.services.PeopleService;

import static ru.batorov.library.util.AuthenticationHelper.*;
import static ru.batorov.library.util.DTOConvert.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    public BookController(BookService bookService, PeopleService peopleService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public String all(Model model,
            @RequestParam(value = "sortByYear", required = false) Boolean sortByYear,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
        if (sortByYear == null)
            sortByYear = false;
        List<Book> books = page == null || itemsPerPage == null ? bookService.all(sortByYear)
                : bookService.all(sortByYear, page, itemsPerPage);
        model.addAttribute("bookUserDTOs", convertToBookUserDTOCollection(books, modelMapper));
        return "books/all";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "findRequest", required = false) String findRequest) {
        if (findRequest != null && !findRequest.equals(""))
            model.addAttribute("bookUserDTOs",
                    convertToBookUserDTOCollection(bookService.findBooksByTitleContaining(findRequest), modelMapper));
        return "books/search";
    }

    @GetMapping("/{bookId}")
    public String show(@PathVariable("bookId") int bookId, Model model,
            @ModelAttribute("personUserDTO") PersonUserDTO personUserDTO,
            Authentication authentif) {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("bookUsersInfoDTO", convertToBookUsersInfoDTO(book, modelMapper));

        if (book.getOwner() == null) {
            // admin logged in
            if (authentif != null && authentif.isAuthenticated() &&
                    hasRoleByAuthentication(authentif, "ROLE_ADMIN"))
                model.addAttribute("personUserDTOs",
                        convertToPersonUserDTOCollection(peopleService.all(), modelMapper));
        } else {
            model.addAttribute("owner", convertToPersonUserDTO(bookService.getBooksOwner(bookId), modelMapper));
            // user that logged in owns this book
            if (authentif != null && authentif.isAuthenticated() &&
                    getUserIdByAuthentication(authentif) == book.getOwner().getId())
                model.addAttribute("UserIsOwner", true);
        }
        return "books/show";
    }

    // all methods after that are for admins only
    @GetMapping("/new")
    public String newBook(@ModelAttribute("bookAdminDTO") BookAdminDTO bookAdminDTO) {
        return "books/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("bookAdminDTO") @Valid BookAdminDTO bookAdminDTO,
            BindingResult bindingResult) {
        Book book = converToBook(bookAdminDTO, modelMapper);
        if (bindingResult.hasErrors())
            return "books/new";
        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/{bookId}/edit")
    public String edit(@PathVariable("bookId") int bookId, Model model) {
        BookAdminDTO bookAdminDTO = convertToBookAdminDTO(bookService.getBookById(bookId), modelMapper);
        model.addAttribute("bookAdminDTO", bookAdminDTO);
        return "books/edit";
    }

    @PatchMapping("/{bookId}/edit")
    public String update(@ModelAttribute("bookAdminDTO") @Valid BookAdminDTO bookAdminDTO, BindingResult bindingResult,
            @PathVariable("bookId") int bookId) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.update(bookId, converToBook(bookAdminDTO, modelMapper));
        return "redirect:/books";
    }

    @DeleteMapping("/{bookId}")
    public String delete(@PathVariable("bookId") int bookId) {
        bookService.delete(bookId);
        return "redirect:/books";
    }

    @PatchMapping("/{bookId}/addowner")
    public String addowner(@PathVariable("bookId") int bookId,
            @ModelAttribute("personUserDTO") PersonUserDTO personUserDTO,
            Authentication authentif) {
        Person person = converToPerson(personUserDTO, modelMapper);
        if (person.getId() == null) {
            if (hasRoleByAuthentication(authentif, "ROLE_ADMIN"))// если админ,значит список людей ему пришел нулевой ->
                                                                 // назначать некому
                return "redirect:/books/" + bookId;
            person.setId(getUserIdByAuthentication(authentif));// если юзер, то узнаем его id
        }

        bookService.addOwner(bookId, person);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{bookId}/release")
    public String deleteOwner(@PathVariable("bookId") int bookId, Authentication authentif,
            HttpServletRequest request) {
        if (hasRoleByAuthentication(authentif, "ROLE_ADMIN") || // если админ это делает или
                (hasRoleByAuthentication(authentif, "ROLE_USER") && // юзер с
                        getUserIdByAuthentication(authentif) == bookService
                                .findBooksOwner(bookId).getId())// id, совпадающим с владельцем книги
        )
            bookService.deleteOwner(bookId);
        return "redirect:" + request.getHeader("referer");
    }

}
