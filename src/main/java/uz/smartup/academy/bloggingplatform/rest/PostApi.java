package uz.smartup.academy.bloggingplatform.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostApi {

    private final PostService postService;

    public PostApi(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable("id") int id) {
        PostDto post = postService.getById(id);

        if(post != null) return post;
        else throw new RuntimeException("Post not found by id :" + id);
    }

    @GetMapping
    public List<PostDto> getPosts() {
        List<PostDto> posts = postService.getAllPosts();

        if(!posts.isEmpty()) return posts;
        else throw new RuntimeException("Any post doesn't exist yet");
    }

    @GetMapping("/{id}/comments")
    public List<CommentDTO> getPostComments(@PathVariable("id") int id) {
        List<CommentDTO> comments = postService.getPostComments(id);

        if(!comments.isEmpty()) return comments;
        else throw new RuntimeException("Any comment doesn't exist yet");
    }

    @GetMapping("/drafts")
    public List<PostDto> getDraftPosts() {
        List<PostDto> posts = postService.getDraftPost();

        if(!posts.isEmpty()) return posts;
        else throw new RuntimeException("Any post doesn't exist yet");
    }

    @GetMapping("/published")
    public List<PostDto> getPublishedPosts() {
        List<PostDto> posts = postService.getPublishedPost();

        if(!posts.isEmpty()) return posts;
        else throw new RuntimeException("Any post doesn't exist yet");
    }

    @GetMapping("/{postId}/like-count")
    public ResponseEntity<Post> getPostWithLikeCount(@PathVariable int postId) {
        Post post = postService.getPostWithLikeCount(postId);
        return ResponseEntity.ok(post);
    }
}
