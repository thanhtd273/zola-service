package com.thanhtd.zola.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    @EventListener
    public void handeWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // TODO:

    }
}
