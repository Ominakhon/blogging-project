package uz.smartup.academy.bloggingplatform.dto;

import org.springframework.stereotype.Component;
import uz.smartup.academy.bloggingplatform.entity.Comment;

import java.util.List;

@Component
public class CommentDtoUtil {
    public Comment toEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        return comment;
    }

    public CommentDTO toDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        return commentDTO;
    }

    public List<CommentDTO> toDTOs(List<Comment> comments) {
        return comments.stream().map(this::toDto).toList();
    }

    public List<CommentDTO> toEntities(List<Comment> users) {
        return users.stream().map(this::toDto).toList();
    }
}
