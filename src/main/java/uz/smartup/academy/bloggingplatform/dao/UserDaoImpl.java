package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.dto.PostDto;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Comment;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {
    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> getALlUsers() {
        TypedQuery<User> users = entityManager.createQuery("FROM User", User.class);
        return users.getResultList();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
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
        if (entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            User managedUser = entityManager.find(User.class, user.getId());
            if (managedUser != null) {
                entityManager.remove(managedUser);
            }
        }
    }

    @Override
    public List<Post> getUserAllPosts(int userId) {
        return entityManager.createQuery("SELECT p FROM Post p WHERE p.user.id = :userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Role> userFindByRoles(String userName) {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r WHERE r.id.username = :userName", Role.class);
        query.setParameter("userName", userName);
        return query.getResultList();
    }

    @Override
    public void updateUserComment(int userId, int postId, Comment comment) {
        User user = getUserById(userId);
        Post post = entityManager.find(Post.class, postId);
        comment.setPost(post);
        comment.setAuthor(user);
        entityManager.merge(comment);
    }

    @Override
    public Set<Role> getUserRoles(int userId) {
        User user = getUserById(userId);

        TypedQuery<Role> query = entityManager.createQuery("FROM Role r where r.id.username = :username", Role.class);
        query.setParameter("username", user.getUsername());

        return new HashSet<>(query.getResultList());

    }

    @Override
    public List<User> getALlUsers() {
        TypedQuery<User> users = entityManager.createQuery("FROM User", User.class);
        return users.getResultList();
    }


}
