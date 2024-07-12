package uz.smartup.academy.bloggingplatform.mvc;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.CategoryService;
import uz.smartup.academy.bloggingplatform.service.PostService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.util.List;
import java.util.Objects;

@Controller
public class EditorMVC {
    private final UserService userService;

    private final PostService postService;

    private final CategoryService categoryService;

    public EditorMVC(UserService userService, PostService postService, CategoryService categoryService) {
        this.userService = userService;
        this.postService = postService;
        this.categoryService = categoryService;
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

    private UserDetails getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return (UserDetails) principal;

        return null;
    }

}
