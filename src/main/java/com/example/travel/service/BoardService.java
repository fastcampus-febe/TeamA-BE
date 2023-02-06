package com.example.travel.service;

import com.example.travel.dto.BoardRequestDto;
import com.example.travel.dto.BoardResponseDto;
import com.example.travel.entity.Board;
import com.example.travel.entity.Member;
import com.example.travel.entity.Thumb;
import com.example.travel.exception.CustomException;
import com.example.travel.exception.ErrorCode;
import com.example.travel.repository.BoardRepository;
import com.example.travel.repository.MemberRepository;
import com.example.travel.repository.ThumbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ThumbRepository thumbRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public Long save(String nickname, final BoardRequestDto dto) {
        Member member = memberRepository.findByNickname(nickname);
        dto.setWriter(member.getNickname());
        dto.setMember(member);

        Board board = boardRepository.save(dto.toEntity());
        return board.getId();
    }

    /**
     * 게시글 리스트 10개씩 조회
     */
    public List<BoardResponseDto> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Board> result = boardRepository.findAll(pageable);
        List<Board> boardList = result.getContent();
        return boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Long update(final Long id, final BoardRequestDto params) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
            entity.update(params.getTitle(), params.getContent());
        return id;
    }

    /**
     * 상세게시글 내용 가져오기
     */
    @Transactional
    public BoardResponseDto findByBoardId(final Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.increaseHit(); // 조회수 증가
        return new BoardResponseDto(entity);
    }

    /**
     * 게시글 삭제하기
     */
    @Transactional
    public void deleteById(final Long id) {
        boardRepository.deleteById(id);
    }

    /**
     * 좋아요
     */
    @Transactional
    public String thumbsUp(Long id, String memberId) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        if (thumbRepository.findByBoardAndMember(board, member) == null) {
            // 좋아요를 누른적 없다면 LikeBoard 생성 후, 좋아요 처리
            board.setThumb(board.getThumb() + 1);
            Thumb thumb = new Thumb(board, member); // true 처리
            thumbRepository.save(thumb);
            return "1"; // 좋아요 처리 완료
        } else {
            // 좋아요를 누른적 있다면 취소 처리 후 테이블 삭제
            Thumb thumb = thumbRepository.findByBoardAndMember(board, member);
            thumb.thumbsDown(board);
            thumbRepository.delete(thumb);
            return "0"; // 좋아요 취소
        }
    }


    /**
     * 게시글 최신 순으로 가져오기
     * ?order = -createdDate : 내림차순 desc created_date
     */
    public List<BoardResponseDto> findByCreatedDateDesc(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());
        Page<Board> result = boardRepository.findAll(pageable);
        List<Board> boardList = result.getContent();
        return boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }


    /**
     * 게시글 오래된 순으로 가져오기
     * ?order = createdDate : 오름차순 asc create_date
     */
    public List<BoardResponseDto> findByCreatedDateAsc(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate").ascending());
        Page<Board> result = boardRepository.findAll(pageable);
        List<Board> boardList = result.getContent();
        return boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 게시글 좋아요 순으로 가져오기
     * ?order = thumb : 내림차순 desc thumb
     */
    public List<BoardResponseDto> findByThumbsUp(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("thumb").descending());
        Page<Board> result = boardRepository.findAll(pageable);
        List<Board> boardList = result.getContent();
        return boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 게시글 검색
     */

    public List<BoardResponseDto> boardSearchByKey(String searchKeyword, Pageable pageable) {
        Page<Board> result = boardRepository.findAllByTitleContaining(searchKeyword, pageable);
        List<Board> boardList = result.getContent();
        List<BoardResponseDto> boardResponseDtoList= boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
        return boardResponseDtoList;
    }

    public List<BoardResponseDto> boardList(Pageable pageable) {
        Page<Board> result = boardRepository.findAll(pageable);
        List<Board> boardList = result.getContent();
        List<BoardResponseDto> boardResponseDtoList= boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
        return boardResponseDtoList;
    }

    public List<BoardResponseDto> boardSearchByWriter(String writer, Pageable pageable){
        Page<Board> result = boardRepository.findAllByWriterContaining(writer, pageable);
        List<Board> boardList = result.getContent();
        List<BoardResponseDto> boardResponseDtoList= boardList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
        return boardResponseDtoList;
    }
}
