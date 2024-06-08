package com.example.demo;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.rasc.sse.eventbus.SseEvent;

@Component
public class ServerSentEventService {

	public enum StatusEnum {
		STARTED, IN_PROGRESS, COMPLETED, FAILED
	}

	public record ServerSentEventProgress(StatusEnum status, double percentage) {
	}

	private final ApplicationEventPublisher eventPublisher;

	public ServerSentEventService(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void sendProgress(String clientId, String eventType, StatusEnum status,
			double percentage) {
		ServerSentEventProgress progressData = new ServerSentEventProgress(status,
				percentage);
		SseEvent event = SseEvent.builder().event(eventType).data(progressData)
				.addClientId(clientId).build();

		this.eventPublisher.publishEvent(event);
	}

	public void sendEvent(String clientId, String eventType, Object data) {
		SseEvent event = SseEvent.builder().event(eventType).data(data)
				.addClientId(clientId).build();

		this.eventPublisher.publishEvent(event);
	}

	@Scheduled(initialDelay = 2000, fixedRate = 5_000)
	public void sendData() {
		this.sendProgress("client1", "progress", StatusEnum.IN_PROGRESS,
				Math.random() * 100);
		this.sendEvent("client1", "event", "Hello World");
	}
}