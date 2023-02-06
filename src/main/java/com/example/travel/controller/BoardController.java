package com.example.travel.controller;

import com.example.travel.dto.BoardRequestDto;
import com.example.travel.dto.BoardResponseDto;
import com.example.travel.dto.MemberLoginRequest;
import com.example.travel.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"게시판 서비스"}, description = "게시글 서비스를 담당합니다.")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 생성
     */
    @PostMapping("/board/insert")
    @ApiOperation(value = "게시글 생성", notes = "새 게시글을 생성합니다.")
    public Long save(@RequestBody final BoardRequestDto params, Authentication authentication) {
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        String strMemberNickname = memberLoginRequest.getNickname();
        return boardService.save(strMemberNickname, params);
    }

    /**
     * 게시글 리스트 10개씩 조회
     */
    @GetMapping("/board/selectAll")
    @ApiOperation(value = "게시글 전체 조회", notes = "전체 게시글 내용을 10개씩 가져옵니다.")
    public List<BoardResponseDto> findAll(@RequestParam(required = false, defaultValue = "1") int page) {
        return boardService.findAll(page - 1);
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/board/{id}")
    @ApiOperation(value = "게시글 수정", notes = "상세 게시글 내용을 수정합니다.")
    public Long save(@PathVariable final Long id, @RequestBody final BoardRequestDto params) {
        return boardService.update(id, params);
    }

    /**
     * 상세 게시글 내용 가져오기
     */
    @GetMapping("/board/{id}")
    @ApiOperation(value = "게시글 조회", notes = "상세 게시글 내용을 가져옵니다.")
    public BoardResponseDto findByBoardId(@PathVariable final Long id) {
        return boardService.findByBoardId(id);
    }

    /**
     * 게시글 삭제
     */
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/board/{id}")
    public void deleteBoardById(@PathVariable final Long id) {
        boardService.deleteById(id);
    }

    /**
     * 게시글 좋아요
     */
    @PostMapping("/board/thumb/{id}")
    @ApiOperation(value = "게시글 좋아요", notes = "사용자가 게시글 좋아요를 누릅니다.")
    public String thumbsUp(@PathVariable Long id, Authentication authentication) {
        MemberLoginRequest memberLoginRequest = (MemberLoginRequest) authentication.getPrincipal();
        String strMemberId = memberLoginRequest.getId();
        return boardService.thumbsUp(id, strMemberId);
    }

    /**
     * 게시글 최신 순으로 가져오기
     * ?order = -createdDate : 내림차순 desc created_date
     * <p>
     * 게시글 오래된 순으로 가져오기
     * ?order = createdDate : 오름차순 asc create_date
     */
    @GetMapping("/boards")
    @ApiOperation(value = "게시글 정렬", notes = "default 는 최신 순으로 게시글을 가져옵니다. order = createdDate 인 경우 오래된 순으로 가져옵니다.")
    public List<BoardResponseDto> findByCreatedDate(@RequestParam(required = false, defaultValue = "-createdDate") @Nullable String order,
                                                    @RequestParam(required = false, defaultValue = "1") int page) {
        if (order.equals("-createdDate") || order.equals("")) {
            return boardService.findByCreatedDateDesc(page - 1);
        }
        return boardService.findByCreatedDateAsc(page - 1);
    }

    /**
     * 게시글 좋아요 순으로 가져오기
     * ?order = thumb : 내림차순 desc thumb
     */
    @GetMapping("/boards/thumb")
    @ApiOperation(value = "게시글 정렬(좋아요 순)", notes = "모든 게시글을 좋아요 순으로 가져오는 api")
    public List<BoardResponseDto> findByThumbsUp(@RequestParam(required = false, defaultValue = "1") int page) {
        return boardService.findByThumbsUp(page - 1);
    }

    @ApiOperation(value = "게시글 검색", notes = "닉네임 또는 제목으로 게시물을 검색합니다.")
    @GetMapping("/board/list")
    public List<BoardResponseDto> boardList(Model model,
                            @PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(required = false, value="searchKeyword") String searchKeyword, @RequestParam(required = false, value="writer") String writer){

        if (searchKeyword ==null && writer == null){
            return boardService.boardList(pageable);
        } else if (searchKeyword != null && writer == null) {
            return boardService.boardSearchByKey(searchKeyword, pageable);
        } else {
            return boardService.boardSearchByWriter(writer, pageable);
        }
    }
}
