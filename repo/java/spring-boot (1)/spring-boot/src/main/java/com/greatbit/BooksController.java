package com.greatbit;

import com.greatbit.models.Book;
import com.greatbit.models.BooksStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class BooksController {

    @GetMapping("/")
    public String booksList(Model model) {
        model.addAttribute("books", BooksStorage.getBooks());
        return "books-list";
    }

    @GetMapping("/create-form")
    public String createForm() {
        return "create-form";
    }

    @PostMapping("/create")
    public String create(Book book) {
        book.setId(UUID.randomUUID().toString());
        BooksStorage.getBooks().add(book);
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        Book bookToDelete=BooksStorage.getBooks().stream().
                filter(b -> b.getId().equals(id)).
                findFirst().
                orElseThrow(RuntimeException::new);
        BooksStorage.getBooks().remove(bookToDelete);
        return "redirect:/";
    }
    @GetMapping("/edit-form/{id}")
    public String editForm(@PathVariable("id") String id,
                           Model model) {
        Book bookToEdit=BooksStorage.getBooks().stream().
                filter(b -> b.getId().equals(id)).
                findFirst().
                orElseThrow(RuntimeException::new);
        model.addAttribute("book", bookToEdit);
        return "edit-form";
    }
//    @PostMapping("/update")
//    public String update(Book book) {
//        delete(book.getId());
//        BooksStorage.getBooks().add(book);
//        return "redirect:/";
//    }
@PostMapping("/update")
public String update(@ModelAttribute Book book) {
    BooksStorage.getBooks().removeIf(b -> b.getId().equals(book.getId()));
    BooksStorage.getBooks().add(book);
    return "redirect:/";
}

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        BooksStorage.getBooks().removeIf(book -> book.getId().equals(id));
        return "redirect:/";
    }

}