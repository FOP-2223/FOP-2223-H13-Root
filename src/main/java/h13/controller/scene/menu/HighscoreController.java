package h13.controller.scene.menu;

import h13.controller.ApplicationSettings;
import h13.controller.scene.SceneController;
import h13.model.HighscoreEntry;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HighscoreController extends SceneController implements Initializable {
    @FXML
    public TableView<HighscoreEntry> highscoreTableView;
    @FXML
    public TableColumn<HighscoreEntry, StringProperty> playerTableColumn;
    @FXML
    public TableColumn<HighscoreEntry, StringProperty> dateTableColumn;
    @FXML
    public TableColumn<HighscoreEntry, IntegerProperty> scoreTableColumn;

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        playerTableColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        scoreTableColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        highscoreTableView.setItems(ApplicationSettings.highscores);
    }

    @Override
    public void initStage(final Stage stage) {
        stage.setTitle("Space Invaders - Highscore");
    }
}
