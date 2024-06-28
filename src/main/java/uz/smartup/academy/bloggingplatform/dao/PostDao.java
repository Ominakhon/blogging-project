package uz.smartup.academy.bloggingplatform.dao;

import uz.smartup.academy.bloggingplatform.entity.Category;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Tag;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

public interface PostDao {
    void save(Post post);

    void update(Post post);

    void delete(Post post);

    Post getById(int id);

    List<Post> getPostsByTag(int tagId);

    List<Post> getPostsByCategory(int categoryId);

    User getAuthorById(int id);

    List<Post> getPostsByAuthor(int authorId);


}

/*
post:
    save
    update
    delete
    getById
    getPostsByTag
    getByPostsByCategory
    getAuthorById
    getPostsByAuthor
 */
