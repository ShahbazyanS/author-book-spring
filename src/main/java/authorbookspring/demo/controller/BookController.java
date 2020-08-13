package authorbookspring.demo.controller;

import authorbookspring.demo.model.Book;
import authorbookspring.demo.repository.BookRepository;
import authorbookspring.demo.service.AuthorService;
import authorbookspring.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {


    private final BookService bookService;
    private final BookRepository bookRepository;



    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/";
    }

    @GetMapping("/allBooks")
    public String authors(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          @RequestParam(value = "orderBy", defaultValue = "title") String orderBy, ModelMap map,
                          @RequestParam(value = "order", defaultValue = "ASC") String order ){
        Sort sort = order.equals("ASK") ? Sort.by(Sort.Order.asc(orderBy)):Sort.by(Sort.Order.desc(orderBy));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<Book> all = bookRepository.findAll(pageRequest);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers", pageNumbers);
        }
        map.addAttribute("books", all);
        return "books";
    }

    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") int id) {
        bookService.deletById(id);
        return "redirect:/allBooks";
    }

    @GetMapping("/getBookById")
    public String getBook(@RequestParam("id") int id, ModelMap map) {
        map.addAttribute("book", bookService.getOne(id));
        return "updateBook";
    }

}
