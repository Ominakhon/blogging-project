package uz.smartup.academy.bloggingplatform.dto;


import lombok.Getter;
import lombok.Setter;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.time.LocalDate;

@Getter
@Setter
public class CommentDTO {
    private int id;
    private int authorId;
    private int postId;
    private String content;
    private LocalDate createdAt;
}
