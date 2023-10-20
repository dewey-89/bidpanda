package com.panda.back.domain.chat.entity;

import com.panda.back.domain.chat.entity.component.Message;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = "chat_records")
@AllArgsConstructor
public class ChatRecord {
    @Id
    private String id;

    @Field(name = "messages")
    private List<Message> messages;

    private Integer messageQnt;

    private LocalDateTime lastSentAt;

    public ChatRecord() {
        this.messages = new ArrayList<>();
        this.messageQnt = 0;
        this.lastSentAt = LocalDateTime.now();
    }

    public void recordMessage(Message message) {
        this.messageQnt += 1;
        this.messages.add(message);
        this.lastSentAt = LocalDateTime.now();
    }
}
