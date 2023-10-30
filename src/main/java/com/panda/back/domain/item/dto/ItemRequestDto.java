package com.panda.back.domain.item.dto;

import lombok.Getter;

@Getter
public class ItemRequestDto {
    private String title;
    private String content;
    private Long startPrice;
    private Long minBidPrice;
    private Integer deadline;
    private String category;
}
/**
  {
      "title" : "사첼백",
      "content" : "일상에서 편리하게 사용 가능",
      "tartPrice" : 50000,
      "inBidPrice" : 1000,
      "deadline" : 1,
      "category" : "패션"
  }
 */