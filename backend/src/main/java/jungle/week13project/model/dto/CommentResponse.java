package jungle.week13project.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdTime;
    private Long postId;
}
