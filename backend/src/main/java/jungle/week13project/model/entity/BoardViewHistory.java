package jungle.week13project.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class BoardViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;
    private String userId;

    private LocalDate viewDate;  // 조회한 날짜만 기록 (LocalDate 사용)

    public BoardViewHistory() {}

    public BoardViewHistory(Long boardId, String userId, LocalDate viewDate) {
        this.boardId = boardId;
        this.userId = userId;
        this.viewDate = viewDate;
    }
}

