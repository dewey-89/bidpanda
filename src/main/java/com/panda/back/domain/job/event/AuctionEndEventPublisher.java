package com.panda.back.domain.job.event;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.job.dto.AuctionEndEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionEndEventPublisher {
    private final ApplicationEventPublisher publisher;
    public void sendAuctionEndAlarmToMembers(Item item) {
        publisher.publishEvent(new AuctionEndEvent(item, item.getMember(), item.getWinner()));
    }
}
