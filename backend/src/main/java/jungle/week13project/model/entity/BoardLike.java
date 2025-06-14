package jungle.week13project.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;
    private String userId;

    public BoardLike() {}

    public BoardLike(Long boardId, String userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}

