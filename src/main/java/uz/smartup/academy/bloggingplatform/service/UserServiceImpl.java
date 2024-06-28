package uz.smartup.academy.bloggingplatform.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.dto.UserDtoUtil;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.repository.PostRepository;
import uz.smartup.academy.bloggingplatform.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserDtoUtil dtoUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public UserServiceImpl(UserDao userDao, UserDtoUtil dtoUtil) {
        this.userDao = userDao;
        this.dtoUtil = dtoUtil;
    }

    @Transactional
    @Override
    public void publishPostByAuthor(int userId, int postId) {
        userDao.addPostToAuthor(userId, postId);

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
    public void registerUser(UserDTO userDTO, Set<Role> roles) {
        User user = dtoUtil.toEntity(userDTO);

        for (Role role : roles) {
            role.setUsername(user.getUsername());
        }
//        String hashedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hashedPassword);
        user.setRoles(roles);

        userDao.save(user);
    }

    @Override
    public List<PostDao> getAllPostsOfUser(int id) {
        return List.of();
    }



}
