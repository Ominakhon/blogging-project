package uz.smartup.academy.bloggingplatform.dao;

import uz.smartup.academy.bloggingplatform.entity.Like;

public interface LikeDAO {

    Like findByUserAndPost(int userId, int postId);

    void save(Like like);

    void delete(Like like);

    long countByPostId(int postId);
}



/*
    like:
        save
        delete
        getLikesByPostId
 */
