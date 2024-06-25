package uz.smartup.academy.bloggingplatform.dao;


import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

public interface UserDao {
    void save(User user);
    User getUserByUsername(String username);
    User getUserById(int id);
    void update(User user);
    void delete(User user);
    List<User> getALlUsers();


}
