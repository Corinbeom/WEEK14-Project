package jungle.week13project.model.dto;

import jungle.week13project.model.enums.BoardType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponse {
    private Long id;
    private BoardType type;
    private String title;
    private String content;
    private String imageUrl;
    private String author;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
