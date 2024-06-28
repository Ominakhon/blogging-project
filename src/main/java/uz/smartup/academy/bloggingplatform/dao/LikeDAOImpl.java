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
//        Like like = entityManager.createQuery(
//                "SELECT l FROM Like l WHERE l.author = :userId AND l.post = :userId", Like.class)
//                .setParameter("author", )
//                .setParameter("post", postId)
//                .getSingleResult();
        return null;
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
}
