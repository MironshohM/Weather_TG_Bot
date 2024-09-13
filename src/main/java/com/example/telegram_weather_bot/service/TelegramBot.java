package com.example.telegram_weather_bot.service;

import com.example.telegram_weather_bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    final BotConfig config;
    private WeatherService weatherService;

    static final String HELP_TEXT= """
             This bot is created to demonstrate Spring capabilities
             You can execute commands from the main menu on the left or by
             Typing command:
             Type /start to see welcome message.
             Type /help to get help.""";

    public TelegramBot(BotConfig config, WeatherService weatherService){
        this.config=config;
        this.weatherService = weatherService;
        List<BotCommand> listofCommands=new ArrayList<>();
        listofCommands.add(new BotCommand("/start","get a welcome message"));
        listofCommands.add(new BotCommand("/help","info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/")) {
                switch (messageText) {
                    case "/start":
                        startCommandReceiver(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        sendMessage(chatId, HELP_TEXT);
                        break;
                    default:
                        sendMessage(chatId, "Sorry, command was not recognized");
                }
            } else {
                // Assume the message is a city name
                String weatherInfo = weatherService.getWeatherByCity(messageText);
                log.info("Weather Info: " + weatherInfo);
                sendMessage(chatId, weatherInfo);
            }
        } else if (update.hasCallbackQuery()) {
            long messageId = update.getCallbackQuery().getMessage().getChatId();
        }
    }


    private void startCommandReceiver(long chatId,String name){

        String answer="Hi, "+name+", nice to meet you!";
        sendMessage(chatId,answer);

    }
    private void sendMessage(long chatId,String textToSend) {
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        }catch (TelegramApiException e){

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
