package jungle.week13project.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jungle.week13project.model.enums.BoardType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType type;

    private String title;

    @Lob
    private String content;

    private String imageUrl;

    private String author;

    private Integer viewCount = 0;

    private Integer likeCount = 0;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    // 생성 시점 자동 설정
    @PrePersist
    public void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.modifiedTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedTime = LocalDateTime.now();
    }

}
