package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{
    private final EntityManager entityManager;
    private final PostDao postDao;


    public UserDaoImpl(EntityManager entityManager, PostDao postDao) {
        this.entityManager = entityManager;
        this.postDao = postDao;

    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return entityManager.find(User.class, username);
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public void addPostToAuthor(int postId, int authorId) {
        Post post = postDao.getById(postId);
        User author = entityManager.find(User.class, authorId);
        author.addPostToAuthor(post);
        update(author);

    }

    @Override
    public List<User> getALlUsers() {
        TypedQuery<User> users = entityManager.createQuery("FROM User", User.class);
        return users.getResultList();
    }

}
