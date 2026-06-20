package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.rasc.sse.eventbus.SseEventBus;
import ch.rasc.sse.eventbus.config.EnableSseEventBus;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableSseEventBus
@EnableScheduling
@RestController
public class DemoApplication {

	private static final long SSE_TIMEOUT = 0L;

	private static final String[] DEMO_EVENTS = { "progress", "event" };

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private final SseEventBus eventBus;

	public DemoApplication(SseEventBus eventBus) {
		this.eventBus = eventBus;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(path = "/register/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter registerClient(@PathVariable("clientId") String clientId,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store");

		return this.eventBus.createSseEmitter(clientId, SSE_TIMEOUT, DEMO_EVENTS);
	}
}
