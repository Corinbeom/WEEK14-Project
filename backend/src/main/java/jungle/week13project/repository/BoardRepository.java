package jungle.week13project.repository;

import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.User;
import jungle.week13project.model.enums.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUser(User user);
    List<Board> findByType(BoardType type);
}
