package com.github.kerner1000.terra.bot.message.average;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.bot.feign.AccountStatsApiClient;
import com.github.kerner1000.terra.commons.*;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateMono;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.BuySellSwaps;
import org.openapitools.model.BuySellSwapsPerCoin;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AverageMessageListener {
    
    public static final String BUY_COMMAND = "!average-price";

    private final AccountStatsApiClient accountStatsApiClient;

    private final ObjectMapper objectMapper;



    public AverageMessageListener(AccountStatsApiClient accountStatsApiClient, ObjectMapper objectMapper) {

        this.accountStatsApiClient = accountStatsApiClient;
        this.objectMapper = objectMapper;

    }

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith(BUY_COMMAND))
                .flatMap(Message::getChannel)
                .flatMap(channel -> processMessageOnChannel(channel, eventMessage))
                .then();
    }

    private MessageChannel channel;

    private <R> MessageCreateMono processMessageOnChannel(MessageChannel channel, Message eventMessage) {
        this.channel = channel;
        return this.channel.createMessage(createImmediateMessage(eventMessage));
    }

    private String createImmediateMessage(Message message) {
        String content = message.getContent();
        try {
            String buyCommandString = content.substring(BUY_COMMAND.length()).trim();
            triggerBuyCommand(buyCommandString);
            return "Alright, working on it, this might take some time..";
        } catch(Exception e){
            log.error(e.getLocalizedMessage(), e);
        }
         return "Instructions unclear, triggering market buy Luna with all available funds";
    }

    private void triggerBuyCommand(String buyCommandString) {
        CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> processBuyCommand(buyCommandString));
        f.thenAccept(this::processResult);
    }

    void processResult(String string){
        log.debug("Processing returned with {}", string);
        channel.createMessage("Your stats are ready sir:\n" + string).subscribe();
    }

    private String processBuyCommand(String buyCommandString) {
        try {

                String terraAddress = buyCommandString.trim();
                ResponseEntity<BuySellSwapsPerCoin> apiResponse = accountStatsApiClient.getSwaps(terraAddress,null);
                StringBuilder sb = new StringBuilder();
                for(BuySellSwaps buySellSwaps : apiResponse.getBody().getEntries()){
                    sb.append(buySellSwaps.getCoin());
                    sb.append(":\n");
                    BuySellMap buyMap = new BuySellMap(buySellSwaps.getBuy());
                    BinnedBuySellMaps binnedBuyMap = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(buyMap);
                    BuySellMap sellMap = new BuySellMap(buySellSwaps.getSell());
                    BinnedBuySellMaps binnedSellMap = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(sellMap);
                    SwapPrices swapPrices = new SwapPrices(Util.weightedMean(buySellSwaps.getBuy().getSwaps()),Util.weightedMean(buySellSwaps.getSell().getSwaps()));
                    sb.append("Average swap prices are:\n");
                    sb.append(swapPrices);
                    sb.append("\n");
                    sb.append("Your buy price distribution:");
                    sb.append("\n");
                    sb.append("```" + binnedBuyMap.toAsciiHistogram(false) + "```");
                    sb.append("Your sell price distribution:");
                    sb.append("```" + binnedSellMap.toAsciiHistogram(false) + "```");
                    sb.append("\n");
                }
                return sb.toString();

        } catch(Exception e){
            log.error(e.getLocalizedMessage(), e);
        }
        return buyCommandString + " is not supported yet.";
    }
}
