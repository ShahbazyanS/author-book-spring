package authorbookspring.demo.service;

import authorbookspring.demo.model.Book;
import authorbookspring.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void save(Book book){
        bookRepository.save(book);
    }

    public Book getOne(int id){
        return  bookRepository.getOne(id);
    }

    public void deletById(int id){
        bookRepository.deleteById(id);
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }
}
