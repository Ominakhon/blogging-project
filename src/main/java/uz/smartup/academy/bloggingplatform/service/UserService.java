package uz.smartup.academy.bloggingplatform.service;

import jakarta.transaction.Transactional;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(int id);
    UserDTO getUserByUsername(String username);
    void updateUser(UserDTO userDTO);
    void deleteUser(int id);
//    void registerUser(UserDTO userDTO);
    void registerUser(UserDTO userDTO, Set<Role> roles);
    List<PostDao> getAllPostsOfUser(int id);
    void publishPostByAuthor(int id, int postId);




}
