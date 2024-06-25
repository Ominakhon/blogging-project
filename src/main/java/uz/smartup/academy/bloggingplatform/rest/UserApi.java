package uz.smartup.academy.bloggingplatform.rest;


import org.springframework.web.bind.annotation.*;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.util.List;

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
        service.registerUser(userDTO);
    }

//    @PutMapping("/update")
//    public void updateUser( @RequestBody UserDTO userDTO){
//        service.updateUser(userDTO);
//    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id){
        service.deleteUser(id);

    }

}
