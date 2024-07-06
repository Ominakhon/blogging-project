package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Comment;
import uz.smartup.academy.bloggingplatform.entity.Role;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(int id);

    UserDTO getUserByUsername(String username);

    void updateUser(UserDTO userDTO);

    void deleteUser(int id);

    void registerUser(UserDTO userDTO, List<Role> roles);

    List<PostDao> getAllPostsOfUser(int id);

    public void addDraftPostByUserId(int userId, PostDto postDto);

    public void addPublishedPostByUserId(int userId, PostDto postDto);

    public List<PostDto> userPublishedPosts(int userId);

    public List<PostDto> userDraftPosts(int userId);

    void updateUserComment(int userId, int postId, Comment comment);



}
