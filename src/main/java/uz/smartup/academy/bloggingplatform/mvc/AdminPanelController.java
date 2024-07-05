package uz.smartup.academy.bloggingplatform.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.smartup.academy.bloggingplatform.dto.*;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.service.*;

import java.util.List;

@Controller
public class AdminPanelController {

    private final UserService userService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TagService tagService;


    public AdminPanelController(CategoryService categoryService, UserService userService, PostService postService, CommentService commentService, LikeService likeService, TagService tagService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.tagService = tagService;
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        List<UserDTO> userDTOS = userService.getAllUsers();
        List<PostDto> postDtos = postService.getAllPosts();
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("userDTO",userDTOS);
        model.addAttribute("users",userDTOS.size());
        model.addAttribute("posts",postDtos.size());
        model.addAttribute("categories",categories);

        return "admin_zip/admin_panel";
    }

    @GetMapping("/admin/category/{title}")
    public String getPostsByCategory(@PathVariable("title") String title, Model model) {

        List<CategoryDto> categories = categoryService.getAllCategories();
        List<PostDto> posts = postService.getPostsByCategory(title);
        model.addAttribute("postDTO", posts);
        model.addAttribute("categoryTitle", title);
        model.addAttribute("postSize", posts.size());
        model.addAttribute("categories",categories);
        return "admin_zip/category_table";
    }

    @RequestMapping("/admin/user")
    public String getAllUsers(Model model) {
        List<UserDTO> userDTOS = userService.getAllUsers();

        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("userDTO",userDTOS);
        model.addAttribute("userSize",userDTOS.size());

        model.addAttribute("categories",categories);

        return "admin_zip/user_table";
    }

    @RequestMapping("/admin/post")
    public String getAllPost(Model model) {
        List<PostDto> postDtos = postService.getAllPosts();

        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("postDTO",postDtos);
        model.addAttribute("postSize",postDtos.size());

        model.addAttribute("categories",categories);

        return "admin_zip/post_table";
    }

    @RequestMapping("/admin/comment")
    public String getAllComment(Model model) {
        List<CommentDTO> commentDTOS = commentService.getAllComments();
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("commentDTO",commentDTOS);
        model.addAttribute("commentSize",commentDTOS.size());

        model.addAttribute("categories",categories);

        return "admin_zip/comment_table";
    }

    @RequestMapping("/admin/like")
    public String getAllLike(Model model) {
        List<LikeDTO> likeDTOS = likeService.getAllLikes();
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("likeDTO",likeDTOS);
        model.addAttribute("likeSize",likeDTOS.size());

        model.addAttribute("categories",categories);

        return "admin_zip/like_table";
    }

    @RequestMapping("/admin/category")
    public String getAllCategories(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("categoryDTO",categories);
        model.addAttribute("categorySize",categories.size());

        model.addAttribute("categories",categories);

        return "admin_zip/categories_table";
    }

    @RequestMapping("/admin/tag")
    public String getAllTag(Model model) {
        List<TagDto> tagDtos = tagService.getAllTags();
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("tagDTO",tagDtos);
        model.addAttribute("tagSize",tagDtos.size());

        model.addAttribute("categories",categories);

        return "admin_zip/tag_table";
    }





}
