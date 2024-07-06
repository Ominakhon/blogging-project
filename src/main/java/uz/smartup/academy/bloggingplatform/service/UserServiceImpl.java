package uz.smartup.academy.bloggingplatform.service;



import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import uz.smartup.academy.bloggingplatform.dao.CategoryDao;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dao.TagDao;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.dto.*;
import java.util.Base64;
import uz.smartup.academy.bloggingplatform.entity.*;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Value("classpath:static/css/photos/userPhoto.jpg")
    private Resource defaultPhotoResource;

    private byte[] defaultPhoto;

    private final UserDao userDao;
    private final UserDtoUtil dtoUtil;
    private final PostDao postDao;
    private final PostDtoUtil postDtoUtil;
    private final PostService postService;
    private final CategoryDtoUtil categoryDtoUtil;
    private final CategoryDao categoryDao;
    private final CommentDtoUtil commentDtoUtil;
    private final TagDao tagDao;
    private final TagDtoUtil tagDtoUtil;


    public UserServiceImpl(UserDao userDao, UserDtoUtil dtoUtil, PostDao postDao, PostDtoUtil postDtoUtil, PostService postService, CategoryDtoUtil categoryDtoUtil, CategoryDao categoryDao, CommentDtoUtil commentDtoUtil, TagDao tagDao, TagDtoUtil tagDtoUtil) {
        this.userDao = userDao;
        this.dtoUtil = dtoUtil;
        this.postDao = postDao;
        this.postDtoUtil = postDtoUtil;
        this.postService = postService;
        this.categoryDtoUtil = categoryDtoUtil;
        this.categoryDao = categoryDao;
        this.commentDtoUtil = commentDtoUtil;
        this.tagDao = tagDao;
        this.tagDtoUtil = tagDtoUtil;
    }

    @PostConstruct
    public void init() throws IOException {
        defaultPhoto = StreamUtils.copyToByteArray(defaultPhotoResource.getInputStream());
//        System.out.println("Default photo size: " + defaultPhoto.length); // Debugging line
    }

    @Override
    public String encodePhotoToBase64(byte[] photo) {
        if (photo == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(photo);
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
//        System.out.println(userDao.userFindByRoles(userDTO.getUsername()));
//        System.out.println(user);
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

        if (user.getPhoto() == null || user.getPhoto().length == 0) {
            user.setPhoto(defaultPhoto);
        }

//        System.out.println("User photo size: " + user.getPhoto().length); // Debugging line

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
        post.setCreatedAt(LocalDateTime.now());
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
        post.setCreatedAt(LocalDateTime.now());
        postDao.save(post);
        userDao.update(user);
    }

    @Override
    @Transactional
    public void addExistCategoriesToPost(int categoryId, int postId) {
        Post post = postDao.getById(postId);
        Category category = categoryDao.findCategoryById(categoryId);

        post.addCategories(category);

        postDao.update(post);
    }

    @Override
    @Transactional
    public void addNewCategoryToPost(CategoryDto categoryDto, int postId) {
        Post post = postDao.getById(postId);

        post.addCategories(categoryDtoUtil.toEntity(categoryDto));

        postDao.update(post);
    }

    @Override
    @Transactional
    public void addExistTagToPost(int tagId, int postId) {
        Post post = postDao.getById(postId);
        Tag tag = tagDao.findTagById(tagId);

        post.addTag(tag);

        postDao.update(post);
    }

    @Override
    @Transactional
    public void addNewTagToPost(TagDto tagDto, int postId) {
        Post post = postDao.getById(postId);

        post.addTag(tagDtoUtil.toEntity(tagDto));

        postDao.update(post);
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
    public void updateUserComment(int userId, int postId, CommentDTO comment) {
        Comment comment1 = commentDtoUtil.toEntity(comment);

        userDao.updateUserComment(userId, postId, comment1);
    }
}
