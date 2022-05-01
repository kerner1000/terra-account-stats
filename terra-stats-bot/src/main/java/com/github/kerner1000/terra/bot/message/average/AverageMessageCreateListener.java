package com.github.kerner1000.terra.bot.message.average;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.bot.EventListener;
import com.github.kerner1000.terra.bot.feign.AccountStatsApiClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AverageMessageCreateListener extends AverageMessageListener implements EventListener<MessageCreateEvent> {

    public AverageMessageCreateListener(AccountStatsApiClient accountStatsApiClient, ObjectMapper objectMapper) {
        super(accountStatsApiClient, objectMapper);
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return processCommand(event.getMessage());
    }
}
