package uz.smartup.academy.bloggingplatform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.dto.PostDtoUtil;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostDao dao;

    private final PostDtoUtil dtoUtil;

    public PostServiceImpl(PostDao dao, PostDtoUtil dtoUtil) {
        this.dao = dao;
        this.dtoUtil = dtoUtil;
    }

    @Override
    @Transactional
    public void createPost(Post post) {
        dao.save(post);
    }

    @Override
    @Transactional
    public void update(Post post) {
        dao.update(post);
    }

    @Override
    @Transactional
    public void delete(int postId) {
        dao.delete(dao.getById(postId));
    }

    @Override
    public PostDto getById(int id) {
        return dtoUtil.toDto(dao.getById(id));
    }

    @Override
    public List<PostDto> getPostsByTag(int tagId) {
        return dtoUtil.toDTOs(dao.getPostsByTag(tagId));
    }

    @Override
    public List<PostDto> getPostsByCategory(int categoryId) {
        return dtoUtil.toDTOs(dao.getPostsByCategory(categoryId));
    }

    @Override
    public User getAuthorById(int id) {
        return dao.getAuthorById(id);
    }

    @Override
    public List<PostDto> getPostsByAuthor(int authorId) {
        return dtoUtil.toDTOs(dao.getPostsByAuthor(authorId));
    }
}
