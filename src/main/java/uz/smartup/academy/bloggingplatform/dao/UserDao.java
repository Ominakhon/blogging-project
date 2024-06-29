package uz.smartup.academy.bloggingplatform.dao;


import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;


public interface UserDao {
    void save(User user);

    List<User> getALlUsers();
    User getUserByUsername(String username);
    User getUserById(int id);
    void update(User user);
    void delete(User user);
    void addPostToAuthor(int postId, int authorId);
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