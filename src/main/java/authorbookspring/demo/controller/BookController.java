package authorbookspring.demo.controller;

import authorbookspring.demo.model.Book;
import authorbookspring.demo.service.AuthorService;
import authorbookspring.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BookController {



    private final BookService bookService;
    private final AuthorService authorService;


    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book){
        bookService.save(book);
        return "redirect:/";
    }
    @GetMapping("/allBooks")
    public String authors(ModelMap map){
        map.addAttribute("books", bookService.findAll());
        return "books";
    }
    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") int id){
        bookService.deletById(id);
        return "redirect:/allBooks";
    }
    @GetMapping("/getBookById")
    public String getBook(@RequestParam("id") int id, ModelMap map){
        map.addAttribute("a",authorService.findAll());
        map.addAttribute("book", bookService.getOne(id));
        return "updateBook";
    }

}
