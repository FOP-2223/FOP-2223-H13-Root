package h13.view.gui;

import h13.controller.EnemyController;
import h13.controller.GameController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static h13.model.GameConstants.ASPECT_RATIO;
import static h13.model.GameConstants.ORIGINAL_GAME_BOUNDS;

public class GameScene extends Scene {

    private Group root;
    private EnemyController enemyController;
    private Pane gameBoard;
    private GameController gameController;

    private Bounds lastGameboardSize;

    public GameScene() {
        super(new Group(), Color.BLACK);
        root = (Group) getRoot();
        init();
    }

    public Pane getGameBoard() {
        return gameBoard;
    }

    private void init() {
        // Game Board
        initGameboard();

        // Game Controller
        gameController = new GameController(this);
    }

    private void initGameboard() {
        gameBoard = new Pane();
        gameBoard.setBorder(
            new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))
            )
        );
        gameBoard.setMaxSize(ORIGINAL_GAME_BOUNDS.getWidth(), ORIGINAL_GAME_BOUNDS.getHeight());
        gameBoard.setMinSize(ORIGINAL_GAME_BOUNDS.getWidth(), ORIGINAL_GAME_BOUNDS.getHeight());
        gameBoard.scaleXProperty().bind(
            Bindings
                .when(widthProperty().divide(heightProperty()).lessThanOrEqualTo(ASPECT_RATIO))
                .then(widthProperty().divide(gameBoard.widthProperty()))
                .otherwise(heightProperty().multiply(ASPECT_RATIO).divide(gameBoard.widthProperty()))
        );
        gameBoard.scaleYProperty().bind(gameBoard.scaleXProperty());

        gameBoard.translateXProperty().bind(widthProperty().subtract(gameBoard.widthProperty()).divide(2));
        gameBoard.translateYProperty().bind(heightProperty().subtract(gameBoard.heightProperty()).divide(2));
        root.getChildren().add(gameBoard);
    }

    public void apply(Stage stage) {
        stage.setTitle("Space Invaders");
        stage.setScene(this);
        stage.show();
    }
}
