package authorbookspring.demo.controller;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.service.AuthorService;
import authorbookspring.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public String homePage( ModelMap map, @RequestParam(name = "msg", required = false) String msg) {
        map.addAttribute("msg", msg);
        return "home";
    }

    @PostMapping("/addAuthor")
    public String addAuthor(@ModelAttribute Author author, @RequestParam("image") MultipartFile file) throws IOException {
        if (!author.getPassword().equals(author.getConfirmPassword())) {
            return "redirect:/?msg=password and confirm password does not match!";
        }

        Optional<Author> byUsername = authorService.findByUsername(author.getUsername());
        if (byUsername.isPresent()) {
            return "redirect:/?msg=User already exist";
        }
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir, name);
        file.transferTo(image);
        author.setProfilePic(name);
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        author.setActive(false);
        author.setToken(UUID.randomUUID().toString());
        authorService.save(author);
        String link = "http://localhost:8080/activate?email=" + author.getUsername() + "&token=" + author.getToken();
        emailService.send(author.getUsername(), "Welcome", "Dear" + author.getName() + " " + "you have successfully registered. Please activate your account by cliking on:" + link);
        return "redirect:/?msg= Author was addid";
    }

    @GetMapping("/activate")
    public String activate(@RequestParam("email") String email, @RequestParam("token") String token){
        Optional<Author> byUsername = authorService.findByUsername(email);
        if (byUsername.isPresent()){
            Author author = byUsername.get();
           if(author.getToken().equals(token)){
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
