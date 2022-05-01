package com.github.kerner1000.terra.bot.message.help;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class HelpMessageListener {

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!help"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(createHelpMessage()))
                .then();
    }

    private String createHelpMessage() {
        return "<< **Help** >>\n" +
                "The following commands are available:\n" +
                "`!help`\n" +
                "\tPrints this help text.\n" +
                "`!about`\n" +
                "\tPrints some infos about this bot.\n" +
                "`!average-price LUNA <a terra address>`\n" +
                "\tPrints the weighted mean price of all Luna-UST swaps via **Terraswap**, **Astroport** or **Market** for given Terra address.\n\t" +
                "Other contracts, such as Kujira Orca are **not** supported (yet)."
               ;
    }
}
