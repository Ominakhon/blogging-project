package uz.smartup.academy.bloggingplatform.dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.smartup.academy.bloggingplatform.entity.Comment;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.repository.PostRepository;
import uz.smartup.academy.bloggingplatform.repository.UserRepository;

import java.util.List;

@Component
public class CommentDtoUtil {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentDtoUtil(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Comment toEntity(CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());;
        comment.setAuthor(comment.getAuthor());
        comment.setPost(comment.getPost());
        return comment;
    }

    public CommentDTO toDto(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        return commentDTO;
    }

    public List<CommentDTO> toDTOs(List<Comment> comments){
        return comments.stream().map(this::toDto).toList();
    }
    public List<CommentDTO> toEntities(List<Comment> users) {
        return users.stream().map(this::toDto).toList();
    }
}

