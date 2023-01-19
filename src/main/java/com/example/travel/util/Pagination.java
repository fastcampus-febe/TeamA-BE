package com.example.travel.util;

public class Pagination {

    private int totalRecordCount;   // 전체 데이터 수
    private int totalPageCount;     // 전체 페이지 수
    private int startPage;          // 첫 페이지 번호
    private int endPage;            // 끝 페이지 번호
    private int limitStart;         // LIMIT 시작 위치
    private boolean existPrevPage;  // 이전 페이지 존재 여부
    private boolean existNextPage;  // 다음 페이지 존재 여부

    public Pagination(int totalRecordCount, SearchDto params) {
        if (totalRecordCount > 0) {
            this.totalRecordCount = totalRecordCount;
            this.calculation(params);
        }
    }

    private void calculation(SearchDto params) {

        // 전체 페이지 수 계산
        totalPageCount = ((totalRecordCount - 1) / params.getRecordSize()) + 1;

        // 현재 페이지 번호가 전체 페이지 수보다 큰 경우, 현재 페이지 번호에 전체 페이지 수 저장
        if (params.getPage() > totalPageCount) {
            params.setPage(totalPageCount);
        }

        // 첫 페이지 번호 계산
        startPage = ((params.getPage() - 1) / params.getPageSize()) * params.getPageSize() + 1;

        // 끝 페이지 번호 계산
        endPage = startPage + params.getPageSize() - 1;

        // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 전체 페이지 수 저장
        if (endPage > totalPageCount) {
            endPage = totalPageCount;
        }

        // LIMIT 시작 위치 계산
        limitStart = (params.getPage() - 1) * params.getRecordSize();

        // 이전 페이지 존재 여부 확인
        existPrevPage = startPage != 1;

        // 다음 페이지 존재 여부 확인
        existNextPage = (endPage * params.getRecordSize()) < totalRecordCount;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public void setExistPrevPage(boolean existPrevPage) {
        this.existPrevPage = existPrevPage;
    }

    public void setExistNextPage(boolean existNextPage) {
        this.existNextPage = existNextPage;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public boolean isExistPrevPage() {
        return existPrevPage;
    }

    public boolean isExistNextPage() {
        return existNextPage;
    }
}
