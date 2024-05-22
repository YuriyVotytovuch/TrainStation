module org.example.trainstation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.zxing;
    requires java.sql;
    requires telegrambots.meta;
    requires telegrambots;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;


    opens org.example.trainstation to javafx.fxml;
    exports org.example.trainstation;

    opens org.example.trainstation.telegram to spring.boot;
    exports org.example.trainstation.telegram;
}