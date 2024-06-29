package uz.smartup.academy.bloggingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.smartup.academy.bloggingplatform.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

}
