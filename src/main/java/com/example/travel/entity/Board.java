package com.example.travel.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "board")
@ToString
public class Board {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 1000)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "writer", nullable = false, unique = true)
    private String writer;

    @Column(name = "good")
    private int good;

    @Column(name = "bad")
    private int bad;

    @Column(name = "hit")
    private int hit;

    @Column(name = "createDate", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "modifiedDate", nullable = false)
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(Long id, String title, String content, String writer, int good, int bad, int hit) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.good = good;
        this.bad = bad;
        this.hit = hit;
    }
}
