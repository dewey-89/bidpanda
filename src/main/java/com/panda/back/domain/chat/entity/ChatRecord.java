package com.panda.back.domain.chat.entity;

import com.panda.back.domain.chat.entity.component.Message;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Document(collection = "chat_records")
public class ChatRecord {
    @Id
    private ObjectId id;

    private String roomId;

    private List<Message> messages;

    private Integer messageQnt;

    private LocalDateTime lastSentAt;

    public ChatRecord(String roomId) {
        this.roomId = roomId;
        this.messages = new ArrayList<>();
        this.messageQnt = 0;
        this.lastSentAt = LocalDateTime.now();
    }

    public void recordMessage(Message message) {
        this.messages.add(message);
        this.messageQnt += 1;
        this.lastSentAt = LocalDateTime.now();
    }
}
