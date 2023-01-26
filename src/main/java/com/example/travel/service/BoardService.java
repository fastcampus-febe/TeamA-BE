package com.example.travel.service;

import com.example.travel.dto.BoardRequestDto;
import com.example.travel.dto.BoardResponseDto;
import com.example.travel.entity.Board;
import com.example.travel.exception.CustomException;
import com.example.travel.exception.ErrorCode;
import com.example.travel.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public Long save(final BoardRequestDto params) {
        Board entity = boardRepository.save(params.toEntity());
        return entity.getId();
    }

    /**
     * 게시글 리스트 조회
     */
    public List<BoardResponseDto> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
        List<Board> list = boardRepository.findAll(sort);
        return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
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
    public BoardResponseDto findByBoardId(final Long id){
        Board result = boardRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if(result != null) return new BoardResponseDto(result);

        return null;
    }

    /**
     * 게시글 삭제하기
     */
    @Transactional
    public void deleteById(final Long id){
        boardRepository.deleteById(id);
    }
    /**
     * 게시글 조회수 가져오기
     */

}
