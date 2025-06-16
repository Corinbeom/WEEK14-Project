package jungle.week13project.repository;

import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.BoardLike;
import jungle.week13project.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByBoardAndUser(Board board, User user);
    List<BoardLike> findByUser(User user);
}
