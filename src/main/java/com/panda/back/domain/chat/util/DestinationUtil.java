package com.panda.back.domain.chat.util;

import org.springframework.stereotype.Component;

@Component
public class DestinationUtil {
    public String getRecordIdFromDestination(String destination) {
        return destination.split("/")[4];
    }
}
