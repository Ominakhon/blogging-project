package uz.smartup.academy.bloggingplatform.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.stylesheets.LinkStyle;
import uz.smartup.academy.bloggingplatform.config.CategoryConfiguration;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.service.CategoryService;
import uz.smartup.academy.bloggingplatform.service.LikeService;
import uz.smartup.academy.bloggingplatform.service.PostService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.io.IOException;
import java.util.Base64;
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
    private final CategoryConfiguration categoryConfiguration;

    public IndexController(PostService postService, CategoryService categoryService, UserService userService, LikeService likeService, CategoryConfiguration categoryConfiguration) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.likeService = likeService;
        this.categoryConfiguration = categoryConfiguration;
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

    @GetMapping("/categories/{categoryTitle}")
    public String categoryPost(@PathVariable("categoryTitle") String categoryTitle, Model model) {
        List<PostDto> posts = postService.getPostsByCategory(categoryTitle)
                .stream()
                .filter(postDto -> postDto.getStatus().equals(Post.Status.PUBLISHED))
                .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()))
                .toList();

        if(posts.size() > 20)
            posts = posts.stream()
                    .limit(20)
                    .toList();

        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("posts", posts);
        model.addAttribute("topPost", posts.getFirst());
        model.addAttribute("categories", categories);
        model.addAttribute("categoryTitle", categoryTitle);

        return "categoryPosts";

    }

    @GetMapping("profile/{userId}")
    public String profile(@PathVariable("userId") int userId, Model model) {
        UserDTO user = userService.getUserById(userId);
        List<CategoryDto> categories = categoryService.getAllCategories();

        String base64EncodedPhoto = userService.encodePhotoToBase64(user.getPhoto());
        model.addAttribute("base64EncodedPhoto", base64EncodedPhoto);
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/profile/{userId}/uploadPhoto")
    public String uploadPhoto(RedirectAttributes attributes,@RequestParam("file") MultipartFile file, Model model, @PathVariable("userId") int userId) {
        // Assume you have a method to get the user from the database
        UserDTO user = userService.getUserById(userId);

        try {
            byte[] bytes = file.getBytes();
            user.setPhoto(bytes);
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to upload the photo. Please try again.");
        }

        attributes.addAttribute("userId", userId);

        return "redirect:/profile/{userId}";
    }

    @GetMapping("profile/{userId}/edit")
    public String editProfile(Model model, @PathVariable("userId") int userId) {
        UserDTO user = userService.getUserById(userId);
        List<CategoryDto> categories = categoryService.getAllCategories();


        String base64EncodedPhoto = userService.encodePhotoToBase64(user.getPhoto());
        model.addAttribute("base64EncodedPhoto", base64EncodedPhoto);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);

        return "editUser";
    }

    @PostMapping("profile/{userId}/update")
    public String updateUser(@PathVariable("userId") int userId,Model model, @ModelAttribute("user") UserDTO userDTO, RedirectAttributes attributes, @RequestParam(value = "file", required = false) MultipartFile photo) throws IOException {
        try {
//            System.out.println(userDTO.getId());
            UserDTO user = userService.getUserById(userId);
            if (photo == null) {
                userDTO.setPhoto(user.getPhoto());
            }else {
                byte[] photoBytes = photo.getBytes();
                userDTO.setPhoto(photoBytes);
            }
            userDTO.setId(userId);
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setRegistered(user.getRegistered());
            userService.updateUser(userDTO);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to update the profile. Please try again.");
        }

        attributes.addAttribute("userId", userId);

        return "redirect:/profile/{userId}";
    }

    @PostMapping("/web/posts/create")
    public String CreatePostController(@ModelAttribute("post") PostDto postDto, Model model){
        model.addAttribute("categories", categoryConfiguration.getCategories());
        return "createPost";
    }

    @GetMapping("/web/posts/create")
    public String CreatePostController(Model model){
        model.addAttribute("categories", categoryConfiguration.getCategories());
        return "createPost";
    }

}
