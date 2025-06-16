package jungle.week13project.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String userId;      // 업데이트 대상 유저
    private String imageUrl;    // 새 이미지 URL
}
