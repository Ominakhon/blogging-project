package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.dao.CommentDao;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.entity.Comment;

import java.util.List;

public interface CommentService {
    public void save(CommentDTO comment);
    public Comment getComment(int id);
    public List<CommentDTO> getAllComments();
    public void deleteComment(int id);
    public void updateComment(CommentDTO comment);


}
