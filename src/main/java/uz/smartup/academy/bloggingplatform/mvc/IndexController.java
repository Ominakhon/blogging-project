package uz.smartup.academy.bloggingplatform.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.stylesheets.LinkStyle;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.service.CategoryService;
import uz.smartup.academy.bloggingplatform.service.LikeService;
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
    private final LikeService likeService;

    public IndexController(PostService postService, CategoryService categoryService, UserService userService, LikeService likeService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.likeService = likeService;
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

    @GetMapping("posts/{postId}")
    public String getPostById(@PathVariable("postId") int postId, Model model) {
        PostDto post = postService.getById(postId);
        List<CommentDTO> comments = postService.getPostComments(postId);
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("commentsSize", comments.size());
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("categories", categories);
        model.addAttribute("newComment", new CommentDTO());

        return "getPost";
    }

    @PostMapping("/{postId}/submitComment/{authorId}")
    public String createComment(RedirectAttributes attributes, @PathVariable("postId") int postId, @PathVariable("authorId") int authorId, @ModelAttribute("newComment") CommentDTO comment) {
        postService.addCommentToPost(authorId, postId, comment);

        attributes.addAttribute("id", postId);

        return "redirect:/post/{id}";
    }

    @PostMapping("/{postId}/likes/{userId}")
    public String createLike(@PathVariable("postId") int postId, @PathVariable("userId") int userId) {
        likeService.addLike(userId, postId);

        return "redirect:/";
    }

}
