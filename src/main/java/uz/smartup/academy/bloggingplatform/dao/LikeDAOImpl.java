package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.entity.Like;
import uz.smartup.academy.bloggingplatform.entity.Post;

import java.util.List;

@Repository
public class LikeDAOImpl implements LikeDAO {

    private final EntityManager entityManager;

    public LikeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Like findByUserAndPost(int userId, int postId) {
        List<Like> likes = entityManager.createQuery(
                        "SELECT l FROM Like l WHERE l.author.id = :userId AND l.post.id = :postId", Like.class)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .getResultList();
        return likes.isEmpty() ? null : likes.get(0);
    }

    @Override
    @Transactional
    public void save(Like like) {
        entityManager.persist(like);
    }

    @Override
    @Transactional
    public void delete(Like like) {
        if (entityManager.contains(like)) {
            entityManager.remove(like);
        } else {
            entityManager.remove(entityManager.merge(like));
        }
    }

    @Override
    public long countByPostId(int postId) {
        String query = "SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId";
        return entityManager.createQuery(query, Long.class)
                .setParameter("postId", postId)
                .getSingleResult();
    }

    @Override
    public List<Like> getAllLike() {
        TypedQuery<Like> query = entityManager.createQuery("FROM Like", Like.class);

        return query.getResultList();
    }
}
