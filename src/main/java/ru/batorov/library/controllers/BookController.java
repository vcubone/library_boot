package ru.batorov.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.BookService;
import ru.batorov.library.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PeopleService peopleService;
    
    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String all(Model model,
        @RequestParam(value = "sortByYear", required = false)		boolean sortByYear,
        @RequestParam(value = "page", required = false)				Integer page,
        @RequestParam(value = "itemsPerPage", required = false)		Integer itemsPerPage)
    {
        System.out.println("test\n");
        if (page != null && itemsPerPage != null)
            model.addAttribute("books", bookService.all(sortByYear, page, itemsPerPage));
        else
            model.addAttribute("books", bookService.all(sortByYear));
        return "books/all";
    }
    
    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "findRequest", required = false) String findRequest)
    {
        if (findRequest != null && !findRequest.equals(""))
            model.addAttribute("books", bookService.findByTitleStartingWith(findRequest));
        return "books/search";
    }
    
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{bookId}/edit")
    public String edit(@PathVariable("bookId") int bookId, Model model) {
        model.addAttribute("book", bookService.show(bookId));
        return "books/edit";
    }
    
    @PatchMapping("/{bookId}/edit")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,@PathVariable("bookId") int bookId) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.update(bookId, book);
        return "redirect:/books";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int bookId, Model model, @ModelAttribute("person") Person person) {
        Book book = bookService.show(bookId);
        model.addAttribute("book", book);
        
        if (book.getOwner() == null)
            model.addAttribute("people", peopleService.all());
            else
            model.addAttribute("owner", book.getOwner());
        return "books/show";
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id)
    {
        bookService.delete(id);
        return "redirect:/books";
    }
    
    @PatchMapping("/{bookId}/addowner")
    public String addowner(@PathVariable("bookId") int bookId, @ModelAttribute("person") Person person) {
        if (person.getPersonId() == 0)
            return "redirect:/books/" + bookId;
        bookService.addOwner(bookId, person);
        return "redirect:/books/" + bookId;
    }
    
    @PatchMapping("/{id}/release")
    public String deleteOwner(@PathVariable("id") int id)
    {
        bookService.deleteOwner(id);
        return "redirect:/books/" + id;
    }
}
