package com.panda.back.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    //클라이언트가 연결을 잃어도 이벤트 유실을 방지하기 위해 임시로 저장되는 데이터
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) { // 해당 회원과 관련된 모든 emitter를 찾음
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) { // 해당 회원과 관련된 모든 이벤트캐시를 찾음
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

//    @Override
//    public void deleteById(String id) {
//        emitters.remove(id);
//    }

    @Override
    public void deleteAllEmitterStartWithId(String memberId) {  // 해당 회원과 관련된 모든 emitter를 지움
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitters.remove(key);
                        deleteAllEventCacheStartWithId(memberId);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {  // 해당 회원과 관련된 모든 이벤트를 지움
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) { eventCache.remove(key); }
                }
        );
    }
}