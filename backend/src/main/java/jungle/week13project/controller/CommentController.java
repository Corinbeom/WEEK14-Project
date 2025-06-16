package jungle.week13project.controller;

import jungle.week13project.config.JwtUtils;
import jungle.week13project.model.dto.CommentRequest;
import jungle.week13project.model.dto.CommentResponse;
import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.Comment;
import jungle.week13project.model.entity.User;
import jungle.week13project.repository.BoardRepository;
import jungle.week13project.service.CommentService;
import jungle.week13project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/{postId}")
    public CommentResponse createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String authHeader) {

        if (!jwtUtils.validateToken(authHeader)) {
            throw new RuntimeException("UNAUTHORIZED");
        }

        String userId = jwtUtils.extractUserId(authHeader);
        User user = userService.findByUserId(userId);

        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다"));

        Comment savedComment = commentService.saveComment(post, request, user);
        return commentService.convertToResponse(savedComment);
    }

    @GetMapping("/{postId}")
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다"));

        return commentService.getCommentsByPost(post).stream()
                .map(commentService::convertToResponse)
                .toList();
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
