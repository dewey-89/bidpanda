package com.panda.back.domain.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

 /*
    - 추가적으로 Emitter REPO 를 구현한 이유 -
    sseEmitter 를 이욯해서 알림을 실제로 보내게 되는데,
    어떤 회원에게 어떤 Emitter가 연결되어있는지 저장해줘야하며,
    어떤 이벤트들이 현재까지 발생했는지에 대해서도 저장하고 있어야한다.
    -> Emitter의 연결이 끊기게 되면 저장되어 있는 Event 를 기반으로 이를 전송해 줄 수 있어야 되기 때문임.
      추후 확장성과 변동성을 생각해 INTERFACE 방식으로 구현
 */

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);   // emitter 저장

    void saveEventCache(String emitterId, Object event); // event 저장

    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    // 해당 회원과 관련된 모든 emitter 찾는다 .

    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);
    // 해당 회원과 관련된 모든 이벤트를 찾는다. - 브라우저 당 여러개 연결이 가능하기에 여러 emitter가 존재

    void deleteById(String id);
    // emitter 삭제

    void deleteAllEmitterStartWithId(String memberId);
    // 해당 회원과 관련된 모든 emitter 삭제

    void deleteAllEventCacheStartWithId(String memberId);
    // 해당 회원과 관련된 모든 이벤트 삭제

}