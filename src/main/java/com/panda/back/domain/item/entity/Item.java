package com.panda.back.domain.item.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.panda.back.domain.item.entity.AuctionStatus.IN_PROGRESS;

@Entity
@Getter
@NoArgsConstructor
public class Item extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long startPrice;

    @Column(nullable = false)
    private Long presentPrice;

    @Column(nullable = false)
    private Long minBidPrice;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime auctionEndTime;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private AuctionStatus auctionStatus = IN_PROGRESS;

    @Column(nullable = false)
    private Long winnerId = 0L;

    @Column(nullable = false)
    private Integer bidCount = 0;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @ElementCollection
    private List<URL> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Item(ItemRequestDto itemRequestDto, Member member) {
        this.title = itemRequestDto.getTitle();
        this.content = itemRequestDto.getContent();
        this.startPrice = itemRequestDto.getStartPrice();
        this.presentPrice = itemRequestDto.getStartPrice();
        this.minBidPrice = itemRequestDto.getMinBidPrice();
        this.auctionEndTime = LocalDateTime.now().plusDays(itemRequestDto.getDeadline());
        this.category = itemRequestDto.getCategory();
        this.member = member;
    }

    public void addImages(URL imageUrl) {
        this.images.add(imageUrl);
    }

    public void update(ItemRequestDto itemRequestDto) {
        this.title = itemRequestDto.getTitle();
        this.content = itemRequestDto.getContent();
        this.startPrice = itemRequestDto.getStartPrice();
        this.presentPrice = itemRequestDto.getStartPrice();
        this.minBidPrice = itemRequestDto.getMinBidPrice();
        this.auctionEndTime = LocalDateTime.now().plusDays(itemRequestDto.getDeadline());
    }
}

