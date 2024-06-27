package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.entity.Like;

@Repository
public class LikeDAOImpl implements LikeDAO {

    private final EntityManager entityManager;

    public LikeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Like findByUserAndPost(int userId, int postId) {
        Like like = entityManager.createQuery(
                "SELECT l FROM Like l WHERE l.userId = :userId AND l.postId = :userId", Like.class)
                .setParameter("userId", userId)
                .setParameter("userId", postId)
                .getSingleResult();
        return like;
    }

    @Override
    @Transactional
    public void save(Like like) {
        entityManager.persist(like);
    }

    @Override
    @Transactional
    public void delete(Like like) {
        entityManager.remove(like);
    }

    @Override
    public int countByPostId(int postId) {
        String query = "SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId";
        return entityManager.createQuery(query, Integer.class)
                .setParameter("postId", postId)
                .getSingleResult();
    }
}
