package uz.smartup.academy.bloggingplatform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dao.TagDao;
import uz.smartup.academy.bloggingplatform.dto.TagDto;
import uz.smartup.academy.bloggingplatform.dto.TagDtoUtil;

import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Tag;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final PostDao postDao;
    private final TagDtoUtil tagDtoUtil;
    private final TagDao tagDao;

    public TagServiceImpl(TagDtoUtil tagDtoUtil, TagDao tagDao,PostDao postDao) {
        this.tagDtoUtil = tagDtoUtil;
        this.tagDao = tagDao;
        this.postDao = postDao;
    }

    @Transactional
    @Override
    public void createTag(TagDto tagDto) {
        Tag tag=tagDtoUtil.toEntity(tagDto);
        tagDao.save(tag);

    }

    @Transactional
    @Override
    public void update(TagDto tagDto) {
        Tag tag=tagDtoUtil.toEntity(tagDto);
        List<Post> posts =postDao.getPostsByTag(tag.getId());
        tag.setPosts(posts);
        tagDao.update(tag);
    }

    @Transactional
    @Override
    public void delete(int id) {
        tagDao.delete(tagDao.findTagById(id));
    }

    @Override
    public List<TagDto> getAllTags() {
        return tagDtoUtil.toDTOs(tagDao.getAllTags());
    }

    @Override
    public TagDto findTagById(int id) {
        return tagDtoUtil.toDto(tagDao.findTagById(id));
    }
}
