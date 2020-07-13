package authorbookspring.demo.controller;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.model.Book;
import authorbookspring.demo.repository.AuthorRepository;
import authorbookspring.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {


    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @GetMapping("/authors")
    public String allAuthors(ModelMap map){
        List<Author> authors = authorRepository.findAll();
        map.addAttribute("authors", authors);
        return "home";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book){
        bookRepository.save(book);
        return "redirect:/";
    }
    @GetMapping("/allBooks")
    public String authors(ModelMap map){
        List<Book> books = bookRepository.findAll();
        map.addAttribute("books", books);
        return "books";
    }
    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") int id){
        bookRepository.deleteById(id);
        return "redirect:/allBooks";
    }
    @GetMapping("/getBookById")
    public String getBook(@RequestParam("id") int id, ModelMap map){
        Book book = bookRepository.getOne(id);
        map.addAttribute("book", book);
        return "updateBook";
    }
    @GetMapping("/aut")
    public String aAuthors(ModelMap map){
        List<Author> authors = authorRepository.findAll();
        map.addAttribute("authors", authors);
        return "updateBook";
    }
}
