package jungle.week13project.repository;

import jungle.week13project.model.entity.BoardViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BoardViewHistoryRepository extends JpaRepository<BoardViewHistory, Long> {
    boolean existsByBoardIdAndUserIdAndViewDate(Long boardId, String userId, LocalDate viewDate);

}
