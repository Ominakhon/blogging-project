package uz.smartup.academy.bloggingplatform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.entity.Tag;

import java.util.List;

@Repository
public class TagDaoImpl implements TagDao{
    private final EntityManager entityManager;

    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Tag tag) {
        entityManager.persist(tag);
    }

    @Override
    public void update(Tag tag) {
        entityManager.merge(tag);
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        TypedQuery<Tag> query = entityManager.createQuery("FROM Tag",Tag.class);
        return query.getResultList();
    }

    @Override
    public Tag findTagById(int id) {
        return entityManager.find(Tag.class,id);
    }
}
