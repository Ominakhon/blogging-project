package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

public interface PostService {
    void createPost(Post post);

    void update(PostDto postDto);

    void delete(int postId);

    PostDto getById(int id);

    List<PostDto> getAllPosts();

    List<PostDto> getPostsByTag(int tagId);

    List<PostDto> getPostsByCategory(int categoryId);

    User getAuthorById(int id);

    List<PostDto> getPostsByAuthor(int authorId);

    List<CommentDTO> getPostComments(int id);

    List<PostDto> getDraftPost();

    List<PostDto> getPublishedPost();

    List<PostDto> getDraftPostsByAuthorId(int authorId);

    List<PostDto> getPublishedPostsByAuthorId(int authorId);

    Post getPostWithLikeCount(int postId);

    void switchPostDraftToPublished(int id);

    void switchPublishedToDraft(int id);

}
