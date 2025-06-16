package jungle.week13project.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileResponse {
    private String userId;
    private String userName;
    private String userEmail;
    private String profileImage;

    private List<BoardResponse> myPosts;
    private List<CommentResponse> myComments;
    private List<BoardResponse> myLikes;
}
