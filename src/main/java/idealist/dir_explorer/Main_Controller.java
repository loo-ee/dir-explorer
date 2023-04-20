package idealist.dir_explorer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Stack;

public class Main_Controller implements Initializable {
    @FXML
    private Label pathLabel;
    @FXML
    private VBox selectionArea;
    @FXML
    private VBox listDirBox;

    private static Stage stage;
    private File[] directories;

    private final Stack<File[]> directoryOrder = new Stack<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            listDirBox.getStyleClass().add("list-dir-box");
            setup();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addStyleSheet() {
        Scene scene = stage.getScene();
        scene.getStylesheets().add("styles/main_view.css");
    }

    public static void setStage(Stage stage) {
        Main_Controller.stage = stage;
        Main_Controller.addStyleSheet();
    }

    public void setup() throws IOException, InterruptedException {
        File[] roots = File.listRoots();

        for (int i = 0; i < roots.length; i++) {
            Button button1 = new Button();

            button1.setText(roots[i].getPath());
            button1.setId(String.valueOf(i));

            setDirectories(roots, i, button1);
            selectionArea.getChildren().add(button1);
        }
    }

    private void setDirectories(File[] parent, int index, Button button) {
        button.setOnAction(event -> {
            directories = parent[index].listFiles();
            directoryOrder.push(parent);
            showList();
        });
    }

    private void showList() {
        listDirBox.getChildren().clear();

        for (int i = 0; i < directories.length; i++) {
            if (directories[i].isHidden())
                continue;;

            Button dir = new Button();

            dir.getStyleClass().add("dir-button");

            if (!directories[i].getName().equals(""))
                dir.setText(directories[i].getName());
            else
                dir.setText(directories[i].getPath());

            if (directories[i].isDirectory()) {
                setDirectories(directories, i, dir);

                if (directories[0].getParentFile() != null)
                    pathLabel.setText(directories[0].getParentFile().getPath());
            }

            listDirBox.getChildren().add(dir);
        }
    }

    @FXML
    protected void showPreviousFolder() {
        if (directoryOrder.size() != 0)
            directories = directoryOrder.pop();

        showList();
    }
}