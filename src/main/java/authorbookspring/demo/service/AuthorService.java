package authorbookspring.demo.service;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    
    public void save(Author author){
        authorRepository.save(author);
    }
    
    public Author getOne(int id){
        return authorRepository.getOne(id);
    }

    public List<Author> findAll(){
        return authorRepository.findAll();
    }
     public void deleteById(int id){
        authorRepository.deleteById(id);
     }

     public Optional<Author> findByUsername(String username){
      return authorRepository.findByUsername(username);
     }
}
