package idealist.dir_explorer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main_Controller implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private VBox selectionArea;

    private static Stage stage;

    private int count = 0;
    private String currentDrive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setup();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStage(Stage stage) {
        Main_Controller.stage = stage;
    }

    public void setup() throws IOException, InterruptedException {
        File[] roots = File.listRoots();

        for (int i = 0; i < roots.length; i++) {
            Button button1 = new Button();

            button1.setText(roots[i].getPath());
            button1.setOnAction(event -> {
                currentDrive = button1.getText();
                System.out.println(currentDrive);
            });

            selectionArea.getChildren().add(button1);
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        if (count == 1) {
            stage.close();
            return;
        }

        welcomeText.setText("Welcome to JavaFX Application!");
        count++;
    }
}