package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(int id);
    UserDTO getUserByUsername(String username);
    void updateUser(UserDTO userDTO);
    void deleteUser(int id);
    void registerUser(UserDTO userDTO);
    List<PostDao> getAllPostsOfUser(int id);
    List<PostDao> addPostToAuthor(int id, UserDTO userDTO);




}
