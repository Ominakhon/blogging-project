package uz.smartup.academy.bloggingplatform.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.dto.*;
import uz.smartup.academy.bloggingplatform.entity.Comment;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserDtoUtil dtoUtil;
    private final PostDao postDao;
    private final PostDtoUtil postDtoUtil;
    private final PostService postService;


    public UserServiceImpl(UserDao userDao, UserDtoUtil dtoUtil, PostDao postDao, PostDtoUtil postDtoUtil, PostService postService) {
        this.userDao = userDao;
        this.dtoUtil = dtoUtil;
        this.postDao = postDao;
        this.postDtoUtil = postDtoUtil;
        this.postService = postService;
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

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        User user = userDao.getUserById(userDTO.getId());
        System.out.println(userDao.userFindByRoles(userDTO.getUsername()));
        System.out.println(user);
        userDao.update(dtoUtil.userMergeEntity(user, userDTO));
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        User user = userDao.getUserById(id);
        userDao.delete(user);
    }

    @Transactional
    @Override
    public void registerUser(UserDTO userDTO, List<Role> roles) {
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


    @Transactional
    @Override
    public void addDraftPostByUserId(int userId, PostDto postDto) {
        User user = userDao.getUserById(userId);

        Post post = postDtoUtil.toEntity(postDto);

        post.setStatus(Post.Status.DRAFT);
        post.setAuthor(user);
        post.setCreatedAt(LocalDate.now());
        postDao.save(post);
        userDao.update(user);
    }

    @Override
    @Transactional
    public void addPublishedPostByUserId(int userId, PostDto postDto) {
        User user = userDao.getUserById(userId);

        Post post = postDtoUtil.toEntity(postDto);

        post.setStatus(Post.Status.PUBLISHED);
        post.setAuthor(user);
        post.setCreatedAt(LocalDate.now());
        postDao.save(post);
        userDao.update(user);
    }

    @Override
    public List<PostDto> userPublishedPosts(int userId) {
        return postService.getPublishedPostsByAuthorId(userId);
    }

    @Override
    public List<PostDto> userDraftPosts(int userId) {
        return postService.getDraftPostsByAuthorId(userId);
    }

    @Override
    @Transactional
    public void updateUserComment(int userId, int postId, Comment comment) {
        userDao.updateUserComment(userId, postId, comment);
    }

}
