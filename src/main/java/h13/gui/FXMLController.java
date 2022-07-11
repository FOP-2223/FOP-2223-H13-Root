package h13.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    @FXML
    private Label label;
    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
//    private Sprite player = new Sprite(100, 100, 20, 20, Color.BLUE, "player", 5);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
//        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    private void loadScene(String sceneName, ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(sceneName));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void showMainMenuScene(ActionEvent e) throws IOException {
        loadScene("mainMenuScene.fxml", e);
    }
    public void showGameScene(ActionEvent e) throws IOException {
        new GameScene().apply((Stage) ((Node)e.getSource()).getScene().getWindow());
    }
}
