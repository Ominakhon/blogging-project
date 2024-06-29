package uz.smartup.academy.bloggingplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.LikeDAO;
import uz.smartup.academy.bloggingplatform.dao.PostDao;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.dto.LikeDTO;
import uz.smartup.academy.bloggingplatform.dto.LikeDTOUtil;
import uz.smartup.academy.bloggingplatform.entity.Like;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.User;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeDAO likeDAO;
    private final PostDao postDao;
    private final LikeDTOUtil likeDTOUtil;
    private final UserDao userDao;

    @Autowired
    public LikeServiceImpl(LikeDAO likeDAO, PostDao postDao, LikeDTOUtil likeDTOUtil, UserDao userDao) {
        this.likeDAO = likeDAO;
        this.postDao = postDao;
        this.likeDTOUtil = likeDTOUtil;
        this.userDao = userDao;
    }

    @Override
    public LikeDTO findByUserAndPost(int userId, int postId) {
        return likeDTOUtil.toDto(likeDAO.findByUserAndPost(userId, postId));
    }

    @Override
    public void addLike(int userId, int postId) {
        User user = userDao.getUserById(userId);
        Post post = postDao.getById(postId);

        Like like1 = likeDAO.findByUserAndPost(userId, postId);

        if (like1 != null) {
            likeDAO.delete(like1);
            // throw new IllegalStateException("Post already liked");
        }
        else {
            Like like = new Like();
            like.setAuthor(user);
            like.setPost(post);
            likeDAO.save(like);
        }


    }

    @Override
    public void removeLike(int userId, int postId) {
        User user = userDao.getUserById(userId);
        Post post = postDao.getById(postId);

        Like like = likeDAO.findByUserAndPost(userId, postId);

        if (like != null) {
            likeDAO.delete(like);
        }
    }

    @Override
    public long countLikesByPostId(int postId) {
        return likeDAO.countByPostId(postId);
    }
}
