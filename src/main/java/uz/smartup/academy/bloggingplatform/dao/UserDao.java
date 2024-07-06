package uz.smartup.academy.bloggingplatform.dao;

import uz.smartup.academy.bloggingplatform.entity.Post;

import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.entity.Comment;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;
import java.util.Set;


public interface UserDao {

    void save(User user);

    List<User> getALlUsers();

    User getUserByUsername(String username);

    User getUserById(int id);

    void update(User user);

    void delete(User user);
  
    List<Post> getUserAllPosts(int userId);

    List<Role> userFindByRoles(String userName);

    Set<Role> getUserRoles(int userId);

    void updateUserComment(int userId, int postId, Comment comment);


}

/*
userDao:
    update
    delete
    getUserById
    register = save
    getUsers
    getUserByUsername



like:
    save
    delete
    getLikesByPostId

Category:
    save
    update
    delete
    getAllCategories
    getCategoryByTitle

tag:
    save
    update
    delete
    getAllTags
    getTagByTitle

 */