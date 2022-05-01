package com.github.kerner1000.terra.bot.message.about;

import com.github.kerner1000.terra.bot.EventListener;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AboutMessageCreateListener extends AboutMessageListener implements EventListener<MessageCreateEvent> {

    public AboutMessageCreateListener(BuildProperties buildProperties) {
        super(buildProperties);
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
