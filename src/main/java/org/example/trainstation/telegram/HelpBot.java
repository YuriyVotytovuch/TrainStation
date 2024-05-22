package org.example.trainstation.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HelpBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "TrainStationHelp_bot";
    }

    @Override
    public String getBotToken() {
        return "7186775850:AAGo_fJlJlxMe5pkoyeYb8sAcwwUVKk1Jas";
    }

    private static final long adminChatID = -4235170556L;
    private static long userChatID;

    @Override
    public void onUpdateReceived(Update update) {
          if (update.hasMessage() && update.getMessage().hasText()) {
              if(update.getMessage().getChatId()==adminChatID){
                  SendMessage sendAnswer = new SendMessage();
                  String answer = update.getMessage().getText();
                  sendAnswer.setChatId(userChatID);
                  sendAnswer.setText("Надійшла відповідь: " + answer + "\nВід модератора: " + update.getMessage().getFrom().getFirstName() + "." +"\nДякуємо за ваше звернення!");
                  try{
                      execute(sendAnswer);
                  }catch (TelegramApiException e){
                      e.printStackTrace();
                  }
              }
              else {
                  userChatID=update.getMessage().getChatId();
                  System.out.println(userChatID);
                  String question = update.getMessage().getText();
                  SendMessage sendQuestion = new SendMessage();
                  sendQuestion.setChatId(adminChatID);
                  sendQuestion.setText("Прийшло повідомлення: "+question+"\nВід користувача: "+update.getMessage().getFrom().getFirstName()+"\nПерешліть повідомлення бота, щоб залишити відповідь.");
                  try {
                      execute(sendQuestion);
                  }catch (TelegramApiException e){
                      e.printStackTrace();
                  }
              }
          }
    }
}
