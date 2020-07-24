package authorbookspring.demo.controller;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthorController {


    @Value("${file.upload.dir}")
    private String uploadDir;
    private final AuthorService authorService;
    private final PasswordEncoder passwordEncoder;




    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal Principal principal, ModelMap map, @RequestParam(name = "msg", required = false) String msg){
       String authorName = null;
        if (principal != null){
           authorName = principal.getName();
       }
        map.addAttribute("msg", msg);
        List<Author> all = authorService.findAll();
        map.addAttribute("au", all);
        map.addAttribute("authorName", authorName);
        return "home";
    }

    @PostMapping("/addAuthor")
    public String addAuthor(@ModelAttribute Author author, @RequestParam("image")MultipartFile file) throws IOException {
        Optional<Author> byUsername = authorService.findByUsername(author.getUsername());
        if (byUsername.isPresent()){
            return "redirect:/?msg=User already exist";
        }
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir,name);
        file.transferTo(image);
        author.setProfilePic(name);
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        authorService.save(author);
        return "redirect:/?msg= Author was addid";
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam ("name")String imageName) throws IOException{
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/loginPage")
    public String loginPage(){
        return "loginPage";
    }

    @GetMapping("/allAuthors")
    public String authors(ModelMap map){
        List<Author> all = authorService.findAll();
        map.addAttribute("all", all);
        return "authors";
    }

    @GetMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam("id") int id){
        authorService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/getAuthorById")
    public String getAuthor(@RequestParam("id") int id,ModelMap map){
        Author author = authorService.getOne(id);
        map.addAttribute("author", author);
        return "updateAuthor";
    }
    @PostMapping("/update")
    public String updateAuthor(@ModelAttribute Author author){
        authorService.save(author);
        return "redirect:/authors";
    }
}
