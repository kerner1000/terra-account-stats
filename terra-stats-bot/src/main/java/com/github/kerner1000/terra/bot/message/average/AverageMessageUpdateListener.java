package com.github.kerner1000.terra.bot.message.average;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.bot.EventListener;
import com.github.kerner1000.terra.bot.feign.AccountStatsApiClient;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AverageMessageUpdateListener extends AverageMessageListener implements EventListener<MessageUpdateEvent> {

    public AverageMessageUpdateListener(AccountStatsApiClient accountStatsApiClient, ObjectMapper objectMapper) {
        super(accountStatsApiClient, objectMapper);
    }

    @Override
    public Class<MessageUpdateEvent> getEventType() {
        return MessageUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageUpdateEvent event) {
        return Mono.just(event)
                .filter(MessageUpdateEvent::isContentChanged)
                .flatMap(MessageUpdateEvent::getMessage)
                .flatMap(super::processCommand);
    }
}
