package authorbookspring.demo.controller;

import authorbookspring.demo.model.Author;
import authorbookspring.demo.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    @Value("${file.upload.dir}")
    private String uploadDir;
    private final AuthorRepository authorRepository;

    @GetMapping("/")
    public String homePage(){
        return "home";
    }

    @PostMapping("/addAuthor")
    public String addAuthor(@ModelAttribute Author author, @RequestParam("image")MultipartFile file) throws IOException {
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir,name);
        file.transferTo(image);
        author.setProfilePic(name);
        authorRepository.save(author);
        return "redirect:/";
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam ("name")String imageName) throws IOException{
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/allAuthors")
    public String authors(ModelMap map){
        List<Author> all = authorRepository.findAll();
        map.addAttribute("all", all);
        return "authors";
    }

    @GetMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam("id") int id){
        authorRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/getAuthorById")
    public String getAuthor(@RequestParam("id") int id,ModelMap map){
        Author author = authorRepository.getOne(id);
        map.addAttribute("author", author);
        return "updateAuthor";
    }
    @PostMapping("/update")
    public String updateAuthor(@ModelAttribute Author author){
        authorRepository.save(author);
        return "redirect:/authors";
    }
}
