package uz.smartup.academy.bloggingplatform.mvc;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.CategoryService;
import uz.smartup.academy.bloggingplatform.service.PostService;
import uz.smartup.academy.bloggingplatform.service.TagService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class EditorMVC {
    private final UserService userService;

    private final PostService postService;

    private final CategoryService categoryService;

    private final TagService tagService;

    public EditorMVC(UserService userService, PostService postService, CategoryService categoryService, TagService tagService) {
        this.userService = userService;
        this.postService = postService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping("editor/{username}/posts")
    public String editorPost(@PathVariable("username") String username, Model model) {
        List<PostDto> posts = postService.getPostsByAuthor(userService.getUserByUsername(username).getId());
        List<CategoryDto> categories = categoryService.getAllCategories();

        String photo = "";
        UserDTO userDTO = getLoggedUser() == null ? null : userService.getUserByUsername(getLoggedUser().getUsername());
        if(userDTO != null){
            photo = userService.encodePhotoToBase64(userDTO.getPhoto());
        }

//        System.out.println(Objects.requireNonNull(getLoggedUser()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EDITOR")));

        model.addAttribute("posts", posts);
        model.addAttribute("categories", categories);
        model.addAttribute("loggedIn", getLoggedUser());
        model.addAttribute("photo", photo);

        return "editorPosts";
    }

    @GetMapping("/editor/{username}/posts/new")
    public String createPostForm(Model model, @PathVariable("username") String username) {
        String photo = "";
        UserDTO userDTO = getLoggedUser() == null ? null : userService.getUserByUsername(getLoggedUser().getUsername());

        if(userDTO != null){
            photo = userService.encodePhotoToBase64(userDTO.getPhoto());
        }

        model.addAttribute("photo", photo);
        model.addAttribute("loggedIn", getLoggedUser());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("post", new PostDto());


        return "createPost";
    }

    @RequestMapping("/editor/{username}/posts/next")
    public String nextCreatePost(@PathVariable("username") String username,@ModelAttribute("post") PostDto postDto, @RequestParam("file") MultipartFile photo, Model model) throws IOException {
        byte[] photoBytes = photo.getBytes();
        postDto.setPhoto(photoBytes);


        UserDTO userDTO = userService.getUserByUsername(username);
        userService.addDraftPostByUserId(userDTO.getId(), postDto);

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("username", username);
        model.addAttribute("post", postDto);

        return "nextCreatePost";
    }

    @RequestMapping("/editor/{username}/posts/save")
    public String savePost(@PathVariable("username") String username,
                           @ModelAttribute("post") PostDto postDto,
                           @RequestParam("action") String action) {
        UserDTO userDTO = userService.getUserByUsername(username);

        PostDto post = postService.getDraftPostsByAuthorId(userDTO.getId())
                .stream()
                .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()))
                .toList()
                .getFirst();


        if ("Publish".equals(action)) {
            postService.switchPostDraftToPublished(post.getId());
        }


        // Add selected categories and tags to the post
        if (postDto.getCategories() != null) {
            for (int categoryId : postDto.getCategories()) {
                userService.addExistCategoriesToPost(categoryId, post.getId());
            }
        }

        if (postDto.getTags() != null) {
            for (int tagId : postDto.getTags()) {
                userService.addExistTagToPost(tagId, post.getId());
            }
        }

        return "redirect:/editor/" + username + "/posts";
    }





    private UserDetails getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return (UserDetails) principal;

        return null;
    }

}
