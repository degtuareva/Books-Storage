package com.greatbit;

import com.greatbit.models.Book;
import com.greatbit.models.BooksStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
