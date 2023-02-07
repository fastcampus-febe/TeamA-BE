package com.example.travel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "board")
public class Board {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 1000)
    private String title;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "writer")
    private String writer;

    @Column(name = "thumb", nullable = true)
    private int thumb; // 좋아요 수

    @Column(name = "hit")
    private int hit; // 조회수

    @Column(name = "createdDate")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modifiedDate")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc") // 댓글 정렬
    private List<Comment> comments;

    @Builder
    public Board(Long id, String title, String content, String writer, int thumb, int hit, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.thumb = thumb;
        this.hit = hit;
        this.member = member;
    }

    /**
     * 게시글 수정
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }

    /**
     * 조회 수 증가
     */
    public void increaseHit() {
        this.hit++;
    }
}
