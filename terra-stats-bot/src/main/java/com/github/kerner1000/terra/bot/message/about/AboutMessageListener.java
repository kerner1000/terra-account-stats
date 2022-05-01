package com.github.kerner1000.terra.bot.message.about;

import discord4j.core.object.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class AboutMessageListener {

    private final BuildProperties buildProperties;

    protected AboutMessageListener(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!about"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(createHelpMessage()))
                .then();
    }

    private String createHelpMessage() {
        return "**About**\n" +
                "Contact: `@CryptoNaut1000`\n" +
                "Donation address: `terra1ucc5zfh5gpqlqedlpwkxqx02hl76l2fzar6wjr`\n" +
                "Version: `" + buildProperties.getVersion() + "`\n" +
                "Time: `" + buildProperties.getTime() + "`"
               ;
    }
}
