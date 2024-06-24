package uz.smartup.academy.bloggingplatform.dao;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;


public interface UserDao {
    void save(User user);

    void delete(User user);

    void update(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    User getUserByUsername(String username);
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