package com.panda.back.domain.job.controller;

import com.panda.back.domain.job.dto.ItemCUDEvent;
import com.panda.back.domain.job.service.CronJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event/webhook")
@RequiredArgsConstructor
public class JobController {
    private final CronJobService cronJobService;
    @EventListener
    public void itemCUDEvent(ItemCUDEvent itemEvent) {
        cronJobService.itemCUDEvent(itemEvent);
    }
}
