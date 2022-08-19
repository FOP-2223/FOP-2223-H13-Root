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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A {@link SceneController} that manages the "Highscore" scene.
 */
public class HighscoreController extends SceneController implements Initializable {

    // --Variables-- //

    /**
     * The table view.
     */
    @FXML
    public TableView<HighscoreEntry> highscoreTableView;

    /**
     * The player name column.
     */
    @FXML
    public TableColumn<HighscoreEntry, StringProperty> playerTableColumn;

    /**
     * The date column.
     */
    @FXML
    public TableColumn<HighscoreEntry, StringProperty> dateTableColumn;

    /**
     * The score column.
     */
    @FXML
    public TableColumn<HighscoreEntry, IntegerProperty> scoreTableColumn;

    @Override
    public String getTitle() {
        return "Space Invaders - Highscore";
    }

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
        highscoreTableView.setItems(ApplicationSettings.getHighscores());
    }
}
