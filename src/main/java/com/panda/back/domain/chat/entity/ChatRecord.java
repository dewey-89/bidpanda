package com.panda.back.domain.chat.entity;

import com.panda.back.domain.chat.entity.component.Message;
import jakarta.persistence.Id;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = "chat_records")
public class ChatRecord {
    @Id
    private ObjectId id;

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
