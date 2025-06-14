package jungle.week13project.model.dto;

import jungle.week13project.model.enums.BoardType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardRequest {
    private BoardType type;
    private String title;
    private String content;
    private String author;
    // 이 부분은 파일 자체를 받기 위해 MultipartFile 추가
    private MultipartFile imageFile;
}
