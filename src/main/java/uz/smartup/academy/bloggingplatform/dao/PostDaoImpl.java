package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import uz.smartup.academy.bloggingplatform.entity.Category;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Tag;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

public class PostDaoImpl implements PostDao{

    private final EntityManager entityManager;

    public PostDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Post post) {
        entityManager.persist(post);
    }

    @Override
    public void update(Post post) {
        entityManager.merge(post);
    }

    @Override
    public void delete(Post post) {
        entityManager.remove(post);
    }

    @Override
    public Post getById(int id) {
        Post post = entityManager.find(Post.class, id);

        return post;
    }

    @Override
    public List<Post> getPostsByTag(int tagId) {
        Tag tag = entityManager.find(Tag.class, tagId);

        TypedQuery<Post> posts = entityManager.createQuery("FROM Post WHERE :tag MEMBER OF tags", Post.class);
        posts.setParameter("tag", tag);

        return posts.getResultList();
    }

    @Override
    public List<Post> getPostsByCategory(int categoryId) {
        Category category = entityManager.find(Category.class, categoryId);

        TypedQuery<Post> posts = entityManager.createQuery("FROM Post WHERE :category MEMBER OF categories", Post.class);
        posts.setParameter("category", category);

        return posts.getResultList();
    }

    @Override
    public User getAuthorById(int id) {
        Post post = entityManager.find(Post.class, id);

        return post.getAuthor();
    }

    @Override
    public List<Post> getPostsByAuthor(int authorId) {
        TypedQuery<Post> query = entityManager.createQuery("FROM Post WHERE author.id = :authorId", Post.class);
        query.setParameter("authorId", authorId);

        return query.getResultList();
    }
}
