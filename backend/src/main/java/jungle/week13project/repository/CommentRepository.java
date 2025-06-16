package jungle.week13project.repository;

import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.Comment;
import jungle.week13project.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Board post);
    List<Comment> findByUser(User user);
}
