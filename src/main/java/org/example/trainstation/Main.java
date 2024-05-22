package org.example.trainstation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.trainstation.telegram.HelpBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

/**
 * Курсова робота з ООП. Тема: "Система менеджменту залізничного вокзалу"
 * @author Yuriy Voytovych
 */
@SpringBootApplication
public class Main extends Application {
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setPrimaryStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/trainstation/TrainStationVisualFind.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root,380,590);
        primaryStage.setTitle("TrainStation");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) {
        SpringApplication.run(HelpBot.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new HelpBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}