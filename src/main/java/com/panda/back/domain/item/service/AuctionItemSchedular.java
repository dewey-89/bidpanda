package com.panda.back.domain.item.service;

import com.panda.back.domain.item.entity.AuctionStatus;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionItemSchedular {

    private final ItemRepository itemRepository;

    @Scheduled(fixedDelay = 60000) // 매 분마다 실행
    @Transactional
    public void checkAndCloseAuctionItems() {
        // 현재 시간을 가져옵니다.
        LocalDateTime currentTime = LocalDateTime.now();

        // 마감 시간이 지난 아이템을 조회합니다.
        List<Item> expiredItems = itemRepository.findAuctionEndTimeItems(currentTime);

        // 조회된 아이템을 종료 상태로 업데이트합니다.
        for (Item item : expiredItems) {
            if(item.getBidCount() == 0){
                item.updateAuctionStatus(AuctionStatus.AUCTION_LOST);
            }else {
                item.updateAuctionStatus(AuctionStatus.AUCTION_WON);
            }
        }

        // 변경된 상태를 저장합니다.
        itemRepository.saveAll(expiredItems);
    }
}