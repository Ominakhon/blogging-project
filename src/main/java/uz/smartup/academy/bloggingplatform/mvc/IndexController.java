package uz.smartup.academy.bloggingplatform.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.stylesheets.LinkStyle;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.service.CategoryService;
import uz.smartup.academy.bloggingplatform.service.PostService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"", "/"})
public class IndexController {
    private final PostService postService;
    private final CategoryService categoryService;
    private final UserService userService;

    public IndexController(PostService postService, CategoryService categoryService, UserService userService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        List<PostDto> posts = postService.getPublishedPost()
                .stream()
                .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()))
                .toList();


        if(posts.size() > 20)
                posts = posts.stream()
                        .limit(20)
                        .toList();
//                .reversed();

        List<CategoryDto> categories = categoryService.getAllCategories();


        model.addAttribute("topPost", posts.getFirst());
        model.addAttribute("posts", posts);
        model.addAttribute("categories", categories);

        return "index";

    }

}
