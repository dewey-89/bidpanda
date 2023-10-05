package com.panda.back.domain.item.entity;

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer startPrice;

    @Column(nullable = false)
    private Integer presentPrice;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Long winMemberId;

    @Column(nullable = false)
    private Integer minBidPrice;
}
