package jungle.week13project.service;

import jungle.week13project.model.dto.BoardResponse;
import jungle.week13project.model.dto.CommentResponse;
import jungle.week13project.model.dto.ProfileResponse;
import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.Comment;
import jungle.week13project.model.entity.User;
import jungle.week13project.repository.BoardLikeRepository;
import jungle.week13project.repository.BoardRepository;
import jungle.week13project.repository.CommentRepository;
import jungle.week13project.repository.UserRepository;
import jungle.week13project.util.FileUploadUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public User saveUser(User user) {

        // 비밀번호를 암호화
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encodedPassword);
        user.setProfileImage("/images/default_profile.png");

        return userRepository.save(user);
    }

    public void validatePassword(String password) {
        if (password.length() < 7) {
            throw new IllegalArgumentException("비밀 번호는 최소 8자 이상 이여야 합니다.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("비밀 번호는 적어도 하나의 대문자를 포함해야 합니다");
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("비밀 번호는 적어도 하나의 특수 문자를 포함 해야 합니다");
        }

    }

    public User findByUserId(String userId) {
        Optional<User> userInfo = userRepository.findByUserId(userId);
        return userInfo.orElse(null);
    }

    public User updateProfileImage(String userId, MultipartFile file) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        String imagePath = FileUploadUtil.saveImageFile(file, uploadDir, "ProfileImage");
        user.setProfileImage("/uploads/" + imagePath);

        return userRepository.save(user);
    }

    public ProfileResponse getProfile(User user) {
        ProfileResponse response = new ProfileResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setUserEmail(user.getUserEmail());
        response.setProfileImage(user.getProfileImage());

        // 내가 작성한 글
        List<BoardResponse> myPosts = boardRepository.findByUser(user).stream()
                .map(this::convertToBoardResponse)
                .toList();
        response.setMyPosts(myPosts);

        // 내가 작성한 댓글
        List<CommentResponse> myComments = commentRepository.findByUser(user).stream()
                .map(this::convertToCommentResponse)
                .toList();
        response.setMyComments(myComments);

        // 내가 좋아요 누른 게시글
        List<BoardResponse> myLikes = boardLikeRepository.findByUser(user).stream()
                .map(boardLike -> convertToBoardResponse(boardLike.getBoard()))
                .toList();
        response.setMyLikes(myLikes);

        return response;
    }

    // BoardResponse 변환
    private BoardResponse convertToBoardResponse(Board board) {
        BoardResponse dto = new BoardResponse();
        dto.setId(board.getId());
        dto.setType(board.getType());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setImageUrl(board.getImageUrl());
        dto.setAuthor(board.getUser().getUserName());
        dto.setViewCount(board.getViewCount());
        dto.setLikeCount(board.getLikeCount());
        dto.setCreatedTime(board.getCreatedTime());
        dto.setModifiedTime(board.getModifiedTime());
        return dto;
    }

    // CommentResponse 변환
    private CommentResponse convertToCommentResponse(Comment comment) {
        CommentResponse dto = new CommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getUser().getUserName());
        dto.setCreatedTime(comment.getCreatedTime());
        dto.setPostId(comment.getPost().getId());
        return dto;
    }

}
