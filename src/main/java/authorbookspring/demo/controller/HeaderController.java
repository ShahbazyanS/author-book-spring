package authorbookspring.demo.controller;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class HeaderController {

    private final AuthorService authorService;

    @ModelAttribute("authorName")
    public String authorName(@AuthenticationPrincipal Principal principal){
        String authorName = null;
        if (principal != null) {
            authorName = principal.getName();
        }
        return authorName;
    }

    @ModelAttribute("authors")
    public List<Author> authors(){
        List<Author> authors = authorService.findAll();
        return authors;
    }
}
