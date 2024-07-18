package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jdk.jfr.Registered;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uz.smartup.academy.bloggingplatform.dto.CommentDTO;
import uz.smartup.academy.bloggingplatform.entity.*;

import java.util.List;

@Repository
public class PostDaoImpl implements PostDao{

    private final EntityManager entityManager;

    private final SessionFactory sessionFactory;

    public PostDaoImpl(EntityManager entityManager, SessionFactory sessionFactory) {
        this.entityManager = entityManager;
        this.sessionFactory = sessionFactory;
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
        return entityManager.find(Post.class, id);
    }

    @Override
    public List<Post> getAllPosts() {
        TypedQuery<Post> query = entityManager.createQuery("FROM Post", Post.class);

        return query.getResultList();
    }


    @Override
    public List<Post> searchPosts(String keyword) {
        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t WHERE " +
                        "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))", Post.class
        );
        query.setParameter("keyword", keyword);

        return query.getResultList();
    }

    @Override
    public List<Post> getPostsByTag(Tag tag) {
        TypedQuery<Post> posts = entityManager.createQuery("FROM Post WHERE :tag MEMBER OF tags", Post.class);
        posts.setParameter("tag", tag);

        return posts.getResultList();
    }

    @Override
    public List<Post> getPostsByCategory(Category category) {
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

    @Override
    public List<Comment> getPostComments(int id) {
        TypedQuery<Comment> query = entityManager.createQuery("FROM Comment WHERE post.id = :id", Comment.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

    @Override
    public List<Post> findPostsByStatus(Post.Status status) {
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post p WHERE p.status = :status", Post.class);

        query.setParameter("status", status);

        return query.getResultList();
    }


    @Override
    public List<Post> findPostByStatusAndAuthorId(Post.Status status, int authorId) {
        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT p FROM Post p WHERE p.status = :status AND p.author.id = :authorId", Post.class);

        query.setParameter("status", status);
        query.setParameter("authorId", authorId);

        return query.getResultList();
    }

    @Override
    public Post.Status findPostStatusById(int postId) {
        Post post = getById(postId);
        return post.getStatus();
    }



}
