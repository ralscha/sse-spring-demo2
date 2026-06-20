package com.example.demo;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.rasc.sse.eventbus.config.SseEventBusConfigurer;

@Configuration
public class SseEventBusConfiguration {

	@Bean
	SseEventBusConfigurer sseEventBusConfigurer() {
		return new SseEventBusConfigurer() {
			@Override
			public Duration heartbeatInterval() {
				return Duration.ofSeconds(15);
			}
		};
	}
}
