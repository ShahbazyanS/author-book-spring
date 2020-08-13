package authorbookspring.demo.service;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    
    public void save(Author author){
        authorRepository.save(author);
    }
    
    public Author getOne(int id){
        try {
            return authorRepository.getOne(id);
        } catch (EntityNotFoundException e){
            log.error("author with {} id does not exist ", id);
            return null;
        }
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
