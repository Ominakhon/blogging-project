package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
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

    List<PostDto> getAllPostsOfUser(int id);

    void addDraftPostByUserId(int userId, PostDto postDto);

    public List<PostDto> userPublishedPosts(int userId);
  
    public List<PostDto> userDraftPosts(int userId);

    void registerUser(UserDTO userDTO, List<Role> roles);

    List<PostDao> getAllPostsOfUser(int id);

    void addDraftPostByUserId(int userId, PostDto postDto);

    void addPublishedPostByUserId(int userId, PostDto postDto);

    void addExistCategoriesToPost(int categoryId, int postId);

    void addNewCategoryToPost(CategoryDto categoryDto, int postId);

    List<PostDto> userPublishedPosts(int userId);

    List<PostDto> userDraftPosts(int userId);

    void addPublishedPostByUserId(int userId, PostDto postDto);

}
