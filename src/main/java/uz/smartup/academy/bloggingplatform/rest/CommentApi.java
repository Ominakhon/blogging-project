package uz.smartup.academy.bloggingplatform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.entity.Comment;
import uz.smartup.academy.bloggingplatform.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentApi {

    private final CommentService service;

    public CommentApi(CommentService commentService) {
        this.service = commentService;
    }

    @GetMapping({"", "/"})
    public List<CommentDTO> getComments() {
       return service.getAllComments();
    }

    @PostMapping({"", "/"})
    public void addComment(@RequestBody CommentDTO comment) {
        service.save(comment);
    }
}
