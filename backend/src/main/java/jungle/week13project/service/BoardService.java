package jungle.week13project.service;

import jungle.week13project.model.dto.BoardRequest;
import jungle.week13project.model.dto.BoardResponse;
import jungle.week13project.model.entity.Board;
import jungle.week13project.model.entity.BoardLike;
import jungle.week13project.model.entity.BoardViewHistory;
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
    public BoardResponse createBoard(BoardRequest boardRequest) {
        Board board = new Board();
        board.setType(boardRequest.getType());
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContent());
        board.setAuthor(boardRequest.getAuthor());
        board.setViewCount(0);
        board.setLikeCount(0);

        String savedFilePath = FileUploadUtil.saveImageFile(boardRequest.getImageFile(), uploadDir);
        board.setImageUrl(savedFilePath);

        Board saved = boardRepository.save(board);
        return convertToResponse(saved);
    }

    // 전체 게시글 조회
    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public Optional<BoardResponse> getBoardById(Long id) {
        return boardRepository.findById(id).map(this::convertToResponse);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    // 게시글 수정 로직
    public BoardResponse updateBoard(Long id, BoardRequest dto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 작성자(author)는 수정 불가 (고정)
        board.setType(dto.getType());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());

        // 이미지 저장 처리
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String savedFilePath = FileUploadUtil.saveImageFile(dto.getImageFile(), uploadDir);
            board.setImageUrl(savedFilePath);
        }

        // 수정시 modifiedTime은 @PreUpdate가 자동으로 처리
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
    public void toggleLike(Long boardId, String userId) {
        Optional<BoardLike> existingLike = boardLikeRepository.findByBoardIdAndUserId(boardId, userId);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (existingLike.isPresent()) {
            boardLikeRepository.delete(existingLike.get());
            board.setLikeCount(board.getLikeCount() - 1);
        } else {
            BoardLike like = new BoardLike(boardId, userId);
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
        dto.setAuthor(board.getAuthor());
        dto.setViewCount(board.getViewCount());
        dto.setLikeCount(board.getLikeCount());
        dto.setCreatedTime(board.getCreatedTime());
        dto.setModifiedTime(board.getModifiedTime());
        return dto;
    }
}
