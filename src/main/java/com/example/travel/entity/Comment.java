package com.example.travel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "comment")
@ToString
public class Comment {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "writer")
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @Column(name = "createdDate")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modifiedDate")
    private LocalDateTime modifiedDate;

    @Builder
    public Comment(Long id, String content, String writer, Member member, Board board) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.member = member;
        this.board = board;
    }
//    @Builder
//    public Comment(Member member, Board board) {
//        this.member = member;
//        this.board = board;
//    }

    /**
     * 댓글 수정
     */
    public void update(String content) {
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }
}
