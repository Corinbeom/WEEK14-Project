package jungle.week13project.controller;

import jungle.week13project.config.JwtUtils;
import jungle.week13project.model.dto.BoardRequest;
import jungle.week13project.model.dto.BoardResponse;
import jungle.week13project.service.BoardService;
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

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@ModelAttribute BoardRequest request, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userName = jwtUtils.extractUserName(authHeader);
        request.setAuthor(userName);

        return ResponseEntity.ok(boardService.createBoard(request));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
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
            @RequestBody BoardRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!jwtUtils.validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 수정할 때도 author 검증을 한다면 여기에 추가
        String userName = jwtUtils.extractUserName(authHeader);

        BoardResponse existingBoard = boardService.getBoardById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!existingBoard.getAuthor().equals(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // author는 request로부터 덮어씌우지 않고 유지
        request.setAuthor(userName);
        return ResponseEntity.ok(boardService.updateBoard(id, request));
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
        boardService.toggleLike(id, userId);
        return ResponseEntity.ok().build();
    }

}
