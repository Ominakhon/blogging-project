package uz.smartup.academy.bloggingplatform.rest;


import org.springframework.web.bind.annotation.*;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Role;
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
    public void updateUser(@RequestBody UserDTO userDTO) {

        service.updateUser(userDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id){
        service.deleteUser(id);
    }

    @PostMapping("/{id}/addDraftPost")
    public void addDraftPostToUser(@PathVariable("id") int id, @RequestBody PostDto postDto) {
        if(postDto != null) service.addDraftPostByUserId(id, postDto);
        else throw new RuntimeException("Cannot create post!");
    }

    @PostMapping("/{id}/addPublishedPost")
    public void addPublishedPostToUser(@PathVariable("id") int id, @RequestBody PostDto postDto) {
        if(postDto != null) service.addPublishedPostByUserId(id, postDto);
        else throw new RuntimeException("Cannot create post!");
    }
}
