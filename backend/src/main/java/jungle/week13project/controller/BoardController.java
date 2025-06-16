package jungle.week13project.controller;

import jungle.week13project.config.JwtUtils;
import jungle.week13project.model.dto.BoardRequest;
import jungle.week13project.model.dto.BoardResponse;
import jungle.week13project.model.entity.User;
import jungle.week13project.model.enums.BoardType;
import jungle.week13project.service.BoardService;
import jungle.week13project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @ModelAttribute BoardRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtUtils.extractUserId(authHeader);
        User user = userService.findByUserId(userId);  // 🔥 핵심 추가: User 조회

        return ResponseEntity.ok(boardService.createBoard(request, user));
    }


    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards(@RequestParam(required = false) BoardType type) {
        List<BoardResponse> boards = (type != null)
                ? boardService.getBoardsByType(type)
                : boardService.getAllBoards();
        return ResponseEntity.ok(boards);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return boardService.getBoardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long id,
            @ModelAttribute BoardRequest request,  // 이미지 포함이면 @ModelAttribute 유지
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtUtils.extractUserId(authHeader);
        User user = userService.findByUserId(userId);  // 🔥 핵심 추가

        BoardResponse existingBoard = boardService.getBoardById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!existingBoard.getAuthor().equals(user.getUserName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(boardService.updateBoard(id, request, user));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<?> increaseViewCount(@PathVariable Long id,
                                               @RequestHeader("Authorization") String authHeader) {
        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = jwtUtils.extractUserId(authHeader);
        boardService.increaseViewCount(id, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id,
                                        @RequestHeader("Authorization") String authHeader) {
        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtUtils.extractUserId(authHeader);
        User user = userService.findByUserId(userId);  // 🔥 User 엔티티 조회 (핵심 추가)

        boardService.toggleLike(id, user);
        return ResponseEntity.ok().build();
    }

}
