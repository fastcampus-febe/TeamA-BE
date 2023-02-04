package com.example.travel.service;

import com.example.travel.dto.CommentRequestDto;
import com.example.travel.dto.CommentResponseDto;
import com.example.travel.entity.Board;
import com.example.travel.entity.Comment;
import com.example.travel.entity.Member;
import com.example.travel.repository.BoardRepository;
import com.example.travel.repository.CommentRepository;
import com.example.travel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 생성
     */
    @Transactional
    public Long save(Long boardId, final CommentRequestDto dto, String memberId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 게시글 id가 존재하지 않습니다. => " + boardId));
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 Member id가 존재하지 않습니다. => " + memberId));
        dto.setWriter(member.getNickname());
        dto.setBoard(board);
        dto.setMember(member);

        Comment comment = commentRepository.save(dto.toEntity());
        return comment.getId();
    }

    /**
     * 댓글 리스트 조회
     */
    public List<CommentResponseDto> findAll(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("댓글 작성 실패 : 해당 게시글 id가 존재하지 않습니다. => " + boardId));
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
        List<Comment> list = commentRepository.findCommentsByBoardId(boardId, sort);
        return list.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Long update(final Long commentId, final CommentRequestDto params) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글 작성 실패 : 해당 댓글 id가 존재하지 않습니다. => " + commentId));
        comment.update(params.getContent());
        return commentId;
    }

    /**
     * 댓글 삭제하기
     */
    @Transactional
    public void deleteById(final Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
