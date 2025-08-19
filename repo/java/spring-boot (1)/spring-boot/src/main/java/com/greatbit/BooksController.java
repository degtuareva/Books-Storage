package com.greatbit;

import com.greatbit.models.BooksStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BooksController {

    @GetMapping("/")
    public String booksList(Model model) {
        model.addAttribute("books", BooksStorage.getBooks());
        return "books-list";
    }
}
