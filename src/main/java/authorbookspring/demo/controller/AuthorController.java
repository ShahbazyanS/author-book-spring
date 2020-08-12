package authorbookspring.demo.controller;

import authorbookspring.demo.dto.AuthorRequestDto;
import authorbookspring.demo.model.Author;
import authorbookspring.demo.service.AuthorService;
import authorbookspring.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthorController {


    @Value("${file.upload.dir}")
    private String uploadDir;
    private final AuthorService authorService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String homePage(ModelMap map, @RequestParam(name = "msg", required = false) String msg) {
        map.addAttribute("msg", msg);
        return "home";
    }

    @PostMapping("/addAuthor")
    public String addAuthor(@Valid @ModelAttribute("a")  AuthorRequestDto authorRequest, BindingResult errors, @RequestParam("image") MultipartFile file, Model model, Locale locale) throws IOException, MessagingException {
        if (errors.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            return "home";
        }

        if (!authorRequest.getPassword().equals(authorRequest.getConfirmPassword())) {
            return "redirect:/?msg=password and confirm password does not match!";
        }
        Optional<Author> byUsername = authorService.findByUsername(authorRequest.getUsername());
        if (byUsername.isPresent()) {
            return "redirect:2/?msg=User already exist";
        }
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir, name);
        Author author = Author.builder()
                .name(authorRequest.getName())
                .surname(authorRequest.getSurname())
                .username(authorRequest.getUsername())
                .password(passwordEncoder.encode(authorRequest.getPassword()))
                .active(false)
                .token(UUID.randomUUID().toString())
                .role(authorRequest.getRole())
                .gender(authorRequest.getGender())
                .bio(authorRequest.getBio())
                .profilePic(name)
                .build();
        authorService.save(author);
        String link = "http://localhost:8080/activate?email=" + authorRequest.getUsername() + "&token=" + author.getToken();
        emailService.sendHtmlEmail(authorRequest.getUsername(), "Welcome", author,link,"email/authorWelcomeMail.html",locale);
        return "redirect:/?msg= Author was addid";
    }


    @GetMapping("/activate")
    public String activate(@RequestParam("email") String email, @RequestParam("token") String token) {
        Optional<Author> byUsername = authorService.findByUsername(email);
        if (byUsername.isPresent()) {
            Author author = byUsername.get();
            if (author.getToken().equals(token)) {
                author.setActive(true);
                author.setToken("");
                authorService.save(author);
                return "redirect:/?msg= Author was activate ";
            }
        }
        return "redirect:/?msg= Something went wrong, please try again";
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("name") String imageName) throws IOException {
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/allAuthors")
    public String authors() {
        return "authors";
    }

    @GetMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam("id") int id) {
        authorService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/getAuthorById")
    public String getAuthor(@RequestParam("id") int id, ModelMap map) {
        Author author = authorService.getOne(id);
        map.addAttribute("author", author);
        return "updateAuthor";
    }

    @PostMapping("/update")
    public String updateAuthor(@ModelAttribute Author author) {
        authorService.save(author);
        return "redirect:/authors";
    }
}
