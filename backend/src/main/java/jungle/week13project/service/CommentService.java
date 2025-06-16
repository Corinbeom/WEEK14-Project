package jungle.week13project.service;

import jungle.week13project.model.dto.CommentRequest;
import jungle.week13project.model.dto.CommentResponse;
import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.Comment;
import jungle.week13project.model.entity.User;
import jungle.week13project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByPost(Board post) {
        return commentRepository.findByPost(post);
    }

    public Comment saveComment(Board post, CommentRequest request, User user) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public CommentResponse convertToResponse(Comment comment) {
        CommentResponse dto = new CommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getUser().getUserName());
        dto.setCreatedTime(comment.getCreatedTime());
        return dto;
    }
}
