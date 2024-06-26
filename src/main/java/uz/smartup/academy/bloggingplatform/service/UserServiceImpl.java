package uz.smartup.academy.bloggingplatform.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.dto.PostDtoUtil;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.dto.UserDtoUtil;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.beans.Transient;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserDtoUtil dtoUtil;
 //   private PostDtoUtil postDtoUtil;

    public UserServiceImpl(UserDao userDao, UserDtoUtil dtoUtil) {
        this.userDao = userDao;
        this.dtoUtil = dtoUtil;
    //    this.postDtoUtil = postDtoUtil;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userDao.getALlUsers();
        return dtoUtil.toEntities(users);
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = userDao.getUserById(id);
        return dtoUtil.toDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userDao.getUserByUsername(username);
        return dtoUtil.toDTO(user);
    }

    @Transactional
    @Override
    public void updateUser(UserDTO userDTO) {
        User user = dtoUtil.toEntity(userDTO);
        userDao.update(user);

    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        User user = userDao.getUserById(id);
        userDao.delete(user);

    }

    @Transactional
    @Override
    public void registerUser(UserDTO userDTO) {
        User user = dtoUtil.toEntity(userDTO);
        userDao.save(user);

    }

    @Override
    public List<PostDao> getAllPostsOfUser(int id) {
        return List.of();
    }

    @Transactional
    @Override
    public List<PostDao> addPostToAuthor(int id, UserDTO userDTO) {
        return List.of();
    }



}
