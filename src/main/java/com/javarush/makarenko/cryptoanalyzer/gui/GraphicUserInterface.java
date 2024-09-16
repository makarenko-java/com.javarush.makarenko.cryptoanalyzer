package com.javarush.makarenko.cryptoanalyzer.gui;

import com.javarush.makarenko.cryptoanalyzer.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GraphicUserInterface extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GraphicUserInterface-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        GraphicUserInterfaceController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setResizable(false);
        stage.setTitle("CaesarCipher - Шифр Цезаря");
        stage.setScene(scene);
        stage.show();
    }

    public static void startGUI() {
        Application.launch();
    }
}
