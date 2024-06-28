package uz.smartup.academy.bloggingplatform.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.PostService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/users")
public class UserApi {

    private final UserService service;

    public UserApi(UserService service) {
        this.service = service;
    }


    @GetMapping({"", "/"})
    public List<UserDTO> getAllUsers(){
        return service.getAllUsers();
    }

    @PostMapping({"", "/"})
    public void registerUser(@RequestBody UserDTO userDTO){
        Set<Role> roles = new HashSet<>();

        Role role = new Role();
        role.setRole("ROLE_VIEWER");
        role.setUsername(userDTO.getUsername());
        roles.add(role);

        service.registerUser(userDTO, roles);
    }

    @PutMapping("/update")
    public void updateUser( @RequestBody UserDTO userDTO){
        service.updateUser(userDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id){
        service.deleteUser(id);

    }

    @PostMapping("addPost/{authorId}/posts/{postId}")
    public ResponseEntity<String> addPostToAuthor(
            @PathVariable int authorId,
            @PathVariable int postId) {
                try {
                    service.publishPostByAuthor(postId, authorId);
                    return new ResponseEntity<>("Post added to author successfully", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Failed to add post to author: " + e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }
     @GetMapping("getPosts/{userId}")
    public List<PostDao> getAllPostOfAuthor(@PathVariable int userId){
         return service.getAllPostsOfUser(userId);

     }
    @GetMapping("getById/{userId}")
    public UserDTO getUserById(@PathVariable int userId){
        return service.getUserById(userId);
    }
    @GetMapping("getByUsername/{username}")
    public UserDTO getUserByUsername(@PathVariable String username){
        return service.getUserByUsername(username);
    }


}
