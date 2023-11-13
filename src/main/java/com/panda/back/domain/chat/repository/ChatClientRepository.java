package com.panda.back.domain.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatClientRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    public Integer addMember(String recordId, String sessionId) {
        try {
            redisTemplate.opsForValue().set(generateKey(sessionId, recordId), recordId);
        }catch (Exception e) {
            return 0;
        }
        return 1;
    }

    public Integer deleteMember(String sessionId) {
        try{
            Set<String> keyToDelete = redisTemplate.keys(findKeyword(sessionId));
            if (Objects.nonNull(keyToDelete)) {
                redisTemplate.delete(keyToDelete);
                return 1;
            }
            return 0;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Set<String> findKeysByRecordId(Long recordId) {
        String keyword = String.format("chat:*:%s",recordId);
        return redisTemplate.keys(keyword);
    }


    private String findKeyword(String sessionId) {
        return String.format("chat:%s:*", sessionId);
    }

    private String generateKey(String sessionId,String recordId) {
        return String.format("chat:%s:%s", sessionId, recordId);
    }
}
