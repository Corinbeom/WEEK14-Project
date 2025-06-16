package jungle.week13project.service;

import jungle.week13project.model.dto.BoardRequest;
import jungle.week13project.model.dto.BoardResponse;
import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.BoardLike;
import jungle.week13project.model.entity.BoardViewHistory;
import jungle.week13project.model.entity.User;
import jungle.week13project.model.enums.BoardType;
import jungle.week13project.repository.BoardLikeRepository;
import jungle.week13project.repository.BoardRepository;
import jungle.week13project.repository.BoardViewHistoryRepository;
import jungle.week13project.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardViewHistoryRepository boardViewHistoryRepository;

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    // 게시글 작성
    public BoardResponse createBoard(BoardRequest boardRequest, User user) {
        Board board = new Board();
        board.setType(boardRequest.getType());
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        board.setUser(user);
        board.setViewCount(0);
        board.setLikeCount(0);

        if (boardRequest.getImageFile() != null && !boardRequest.getImageFile().isEmpty()) {
            String imagePath = FileUploadUtil.saveImageFile(boardRequest.getImageFile(), uploadDir, "BoardImage");
            board.setImageUrl("/uploads/" + imagePath);
        }

        Board saved = boardRepository.save(board);
        return convertToResponse(saved);
    }

    // 전체 게시글 조회
    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<BoardResponse> getBoardsByType(BoardType type) {
        List<Board> boards = boardRepository.findByType(type);
        return boards.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 게시글 상세 조회
    public Optional<BoardResponse> getBoardById(Long id) {
        return boardRepository.findById(id).map(this::convertToResponse);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    // 게시글 수정
    public BoardResponse updateBoard(Long id, BoardRequest dto, User user) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 작성자 검증
        if (!board.getUser().getId().equals(user.getId())) {
            throw new SecurityException("작성자만 수정할 수 있습니다.");
        }

        // 수정 처리
        board.setType(dto.getType());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());

        // 이미지 수정 처리
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imagePath = FileUploadUtil.saveImageFile(dto.getImageFile(), uploadDir, "BoardImage");
            board.setImageUrl("/uploads/" + imagePath);
        }

        Board updatedBoard = boardRepository.save(board);
        return convertToResponse(updatedBoard);
    }


    // 조회수 증가 로직
    @Transactional
    public void increaseViewCount(Long boardId, String userId) {
        LocalDate today = LocalDate.now();

        boolean alreadyViewed = boardViewHistoryRepository.existsByBoardIdAndUserIdAndViewDate(boardId, userId, today);
        if (!alreadyViewed) {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

            board.setViewCount(board.getViewCount() + 1);
            boardRepository.save(board);

            BoardViewHistory history = new BoardViewHistory(boardId, userId, today);
            boardViewHistoryRepository.save(history);
        }
    }

    // 좋아요 증가 로직
    @Transactional
    public void toggleLike(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Optional<BoardLike> existingLike = boardLikeRepository.findByBoardAndUser(board, user);

        if (existingLike.isPresent()) {
            boardLikeRepository.delete(existingLike.get());
            board.setLikeCount(board.getLikeCount() - 1);
        } else {
            BoardLike like = new BoardLike(board, user);
            boardLikeRepository.save(like);
            board.setLikeCount(board.getLikeCount() + 1);
        }

        boardRepository.save(board);
    }


    private BoardResponse convertToResponse(Board board) {


        BoardResponse dto = new BoardResponse();
        dto.setId(board.getId());
        dto.setType(board.getType());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setImageUrl(board.getImageUrl());

        dto.setAuthor(board.getUser() != null ? board.getUser().getUserName() : "알 수 없음");  // null-safe (이전 로직에서 저장했던 Author 필드가 오류를 발생시켰음)

        dto.setViewCount(board.getViewCount());
        dto.setLikeCount(board.getLikeCount());
        dto.setCreatedTime(board.getCreatedTime());
        dto.setModifiedTime(board.getModifiedTime());
        return dto;
    }
}
