package sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RandomTicker {

    @Value("${tick.interval.max:5000}")
    private Integer MAX_TICK_INTERVAL;

    @Value("${tick.interval.least:1000}")
    private Integer LEAST_TICK_INTERVAL;

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private ScheduledFuture<?> future;

    public synchronized void addSession(WebSocketSession session) throws Exception {
        if (sessions.isEmpty()) {
            start();
        }
        sessions.put(session.getId(), session);
    }

    public synchronized void removeSession(String id) {
        sessions.remove(id);
        if (sessions.isEmpty()) {
            stop();
        }
    }

    public void broadcast(String message) throws Exception {
        TextMessage textMessage = new TextMessage(message);
        for (ConcurrentHashMap.Entry<String, WebSocketSession> session : sessions.entrySet()) {
            session.getValue().sendMessage(textMessage);
        }
    }

    private void start() throws Exception {
        Random rand = new Random();
        future = Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                int delay = rand.nextInt(MAX_TICK_INTERVAL - LEAST_TICK_INTERVAL);
                Thread.sleep(delay);

                broadcast(LEAST_TICK_INTERVAL + delay + "ms");

            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }, 0, LEAST_TICK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void stop() {
        future.cancel(false);
    }
}
