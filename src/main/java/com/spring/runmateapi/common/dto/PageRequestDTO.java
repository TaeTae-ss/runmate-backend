package com.spring.runmateapi.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PageRequestDTO {
    private String searchType = "";
    private String keyword = "";

    private int page = 1;
    private int size = 10;

    //검색필터
    private String location; //지역
    private LocalDate runDate; //러닝 날짜
    private Integer maxDistance; //거리(이하 검색)

    private String sort = "latest"; //

    public PageRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public Pageable getPageable(String sortField) {
        Sort.Direction direction = "oldest".equals(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, size, Sort.Direction.DESC, sortField);
    }
}
