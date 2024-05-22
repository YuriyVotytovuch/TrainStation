package org.example.trainstation;

import com.google.zxing.WriterException;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import org.example.trainstation.database.Database;
import java.io.IOException;
import java.util.List;

import static org.example.trainstation.Main.getPrimaryStage;

public class ControllerTrainVisual extends Database {
    private Scene currentScene;
    @FXML
    private TextArea loginReg;
    @FXML
    private TextArea passwordReg;
    @FXML
    private TextArea loginAuto;
    @FXML
    private TextArea passwordAuto;

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea from;
    @FXML
    private TextArea to;
    @FXML
    private ComboBox timeComboBox;

    @FXML
    private TextArea fromTrain;
    @FXML
    private TextArea toTrain;
    @FXML
    private DatePicker dateTrain;
    @FXML
    private ComboBox priceComboBox;

    @FXML
    private void handleTrainFinder() throws IOException {
        loadScene("/org/example/trainstation/TrainStationVisualFind.fxml");
    } // працює

    @FXML
    private void handleMyTickets() throws IOException {
        if (isIsLogin()) {
            delete(isloginuse());
            VBox root = new VBox();
            root.setPadding(new Insets(20));
            root.setSpacing(50);
            List<String> trainsuser = Database.getAllUserTrains();
            root.getChildren().clear();
            root.setStyle("-fx-background-color: #333");
            for (String train : trainsuser) {
                HBox trainBox = new HBox();
                Label trainLabel = new Label(train);
                trainLabel.setTextFill(Color.WHITE);
                trainBox.getChildren().add(trainLabel); // Додати trainLabel до trainBox
                root.getChildren().add(trainBox); // Додати trainBox до root
            }
            Button backupbutton = new Button("Back UP");
            backupbutton.setStyle("-fx-border-color: #66f254; -fx-border-width: 2px; -fx-background-color: #333; -fx-text-fill: WHITE");
            backupbutton.setOnAction(event -> {
                try {
                    handleTrainFinder();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            HBox buttonsBox = new HBox(430, backupbutton);
            buttonsBox.setAlignment(Pos.CENTER);
            root.getChildren().addAll(buttonsBox);
            Scene scene = new Scene(root, 450, 600);
            getPrimaryStage().setScene(scene);
            getPrimaryStage().setTitle("TrainStation Tickets");
            getPrimaryStage().show();
        }else {
            loadScene();
        }
    } // працює

    @FXML
    private void regbutton() throws IOException {
        loadScene("/org/example/trainstation/TrainStationVisualAccountReg.fxml");
    } // працює

    @FXML
    private void singupbutton() throws IOException {
        String login = loginReg.getText();
        String password = passwordReg.getText();
        if(checkUser(login)){
            loadScene();
        }else{
            register(login, password);
        }
    } // працює

    @FXML
    private void handleAccount() throws IOException {
        if(isIsLogin()){
            if(isAdmin(isloginuse())){
                loadScene("/org/example/trainstation/TrainStationVisualAdminMenu.fxml");
            } else {
                loadScene("/org/example/trainstation/TrainStationVisualUserMenu.fxml");
            }
        } else {
            loadScene("/org/example/trainstation/TrainStationVisualAccountAuto.fxml");
        }
    } // працює

    @FXML
    private void loginbutton() throws IOException{
        String login = (loginAuto != null) ? loginAuto.getText() : null;
        setLoginuse(login);
        String password = (passwordAuto != null) ? passwordAuto.getText() : null;
        if(checkLogin(login,password)){
            setIsLogin(true);
            if(isAdmin(login)){
                loadScene("/org/example/trainstation/TrainStationVisualAdminMenu.fxml");
            } else {
                loadScene("/org/example/trainstation/TrainStationVisualUserMenu.fxml");
            }
        } else {
            loadScene();
        }
    } // працює

    @FXML
    private void addTrain() throws IOException {
        try{
            // Парсинг дати до формату yyyy-MM-dd
            String date = datePicker.getValue().toString();
            String froms = from.getText();
            String tos = to.getText();
            String time = timeComboBox.getValue().toString();
            String price = priceComboBox.getValue().toString();
            addTicket(froms, tos, date,time,price);
        }catch (NullPointerException e){
            loadScene();
        }
    } // працює
    @FXML
    private void findbutton() throws IOException{
        if(isIsLogin()) {
            delete();
            try {
                String From = fromTrain.getText();
                String To = toTrain.getText();
                String date1 = dateTrain.getValue().toString();
                if (!checkTrain(From, To, date1)) {
                    loadScene();
                } else {
                    VBox root = new VBox();
                    root.setPadding(new Insets(20));
                    root.setSpacing(50);
                    List<String> trains = Database.getAllTrains(From, To, date1);
                    root.getChildren().clear();
                    root.setStyle("-fx-background-color: #333");
                    for (String train : trains) {
                        HBox trainBox = new HBox();
                        Label trainLabel = new Label(train);
                        trainLabel.setTextFill(Color.WHITE);
                        Button reserveButton = new Button("Забронювати");
                        reserveButton.setStyle("-fx-border-color: #66f254; -fx-border-width: 2px; -fx-background-color: #333; -fx-text-fill: WHITE");
                        reserveButton.setOnAction(event -> {
                            System.out.println("Зарезервовано місце на потязі: " + train);
                            String[] parts = train.split(";");
                            String trainNumber = parts[0].trim().substring(parts[0].indexOf(":") + 1).trim();
                            String from = parts[1].trim().substring(parts[1].indexOf(":") + 1).trim();
                            String to = parts[2].trim().substring(parts[2].indexOf(":") + 1).trim();
                            String date = parts[3].trim().substring(parts[3].indexOf(":") + 1).trim();
                            String time = parts[4].trim().substring(parts[4].indexOf(":") + 1).trim();
                            String price = parts[5].trim().substring(parts[5].indexOf(":") + 1).trim();
                            buyTrain(from,to,date,time,trainNumber);
                            try {
                                Checks.generateCheck(trainNumber,from,to,date,time,price);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (WriterException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        trainBox.getChildren().addAll(trainLabel, reserveButton);
                        root.getChildren().add(trainBox);
                    }
                    Button backupbutton = new Button("Back UP");
                    backupbutton.setStyle("-fx-border-color: #66f254; -fx-border-width: 2px; -fx-background-color: #333; -fx-text-fill: WHITE");
                    backupbutton.setOnAction(event -> {
                        try {
                            handleTrainFinder();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    HBox buttonsBox = new HBox(430, backupbutton);
                    buttonsBox.setAlignment(Pos.CENTER);
                    root.getChildren().addAll(buttonsBox);
                    Scene scene = new Scene(root, 580, 700);
                    getPrimaryStage().setScene(scene);
                    getPrimaryStage().setTitle("TrainStation Tickets");
                    getPrimaryStage().show();
                }

            }catch (NullPointerException e){
                loadScene();
            }
        }
        else{
            loadScene();
                }
            } // працюює
    @FXML()
    private void logOutbutton() throws IOException {
        setIsLogin(false);
        loadScene("/org/example/trainstation/TrainStationVisualAccountAuto.fxml");
    } // працює
    @FXML
    private void helpbot() throws IOException, WriterException {
        loadSceneHelp();
        Checks.generateQRCode(200,200);
    }//працює

    private void loadScene(String fxmlResource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
        Parent root = loader.load();
        currentScene = new Scene(root, 380, 590);
        getPrimaryStage().setScene(currentScene);
        getPrimaryStage().setTitle("TrainStation");
        getPrimaryStage().show();
    } // працює
    private void loadScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/trainstation/TrainStationVisualEror.fxml"));
        Parent root = loader.load();
        currentScene = new Scene(root, 200, 200);
        Stage newStage = new Stage();
        newStage.setScene(currentScene);
        newStage.setTitle("TrainStation");
        newStage.show();
    } // працює
    private void loadSceneHelp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/trainstation/TrainStationVisualBotInfo.fxml"));
        Parent root = loader.load();
        currentScene = new Scene(root, 200, 400);
        Stage newStage = new Stage();
        newStage.setScene(currentScene);
        newStage.setTitle("TrainStation");
        newStage.show();
    } // працює
}