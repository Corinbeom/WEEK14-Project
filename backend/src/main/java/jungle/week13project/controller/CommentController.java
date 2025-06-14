package jungle.week13project.controller;

import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.Comment;
import jungle.week13project.repository.BoardRepository;
import jungle.week13project.service.CommentService;
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

    @PostMapping("/{postId}")
    public Comment createComment(@PathVariable Long postId, @RequestBody Comment comment) {
        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다"));
        comment.setPost(post);
        return commentService.saveComment(comment);
    }

    @GetMapping("/{postId}")
    public List<Comment> getComments(@PathVariable Long postId) {
        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다"));
        return commentService.getCommentsByPost(post);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
