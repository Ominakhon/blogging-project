package uz.smartup.academy.bloggingplatform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.CommentDao;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.dto.CommentDtoUtil;
import uz.smartup.academy.bloggingplatform.entity.Comment;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
   private final CommentDtoUtil dtoUtil;
   private final CommentDao commentDao;

    public CommentServiceImpl(CommentDtoUtil dtoUtil, CommentDao commentDao) {
        this.dtoUtil = dtoUtil;
        this.commentDao = commentDao;
    }

    @Transactional
    @Override
    public void save(CommentDTO commentDTO) {
        Comment comment = dtoUtil.toEntity(commentDTO);
        commentDao.save(comment);
    }

    @Override
    public Comment getComment(int id) {
        return commentDao.getComment(id);
    }

    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentDao.getComments();
        return dtoUtil.toEntities(comments);
    }
    @Transactional
    @Override
    public void deleteComment(int id) {
        Comment comment = commentDao.getComment(id);
        commentDao.delete(comment);
    }

    @Transactional
    @Override
    public void updateComment(CommentDTO comment) {
        Comment comment1 = dtoUtil.toEntity(comment);
        commentDao.update(comment1);
    }
}
