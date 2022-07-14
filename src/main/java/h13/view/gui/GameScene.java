package h13.view.gui;

import h13.controller.EnemyController;
import h13.controller.GameController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static h13.model.GameConstants.ASPECT_RATIO;
import static h13.model.GameConstants.ORIGINAL_GAME_BOUNDS;

public class GameScene extends Scene {

    private final Group root;
    private EnemyController enemyController;
    private GameBoard gameBoard;
    private GameController gameController;

    private Bounds lastGameboardSize;

    public GameScene() {
        super(new Group(), Color.BLACK);
        root = (Group) getRoot();
        init();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    private void init() {
        // Game Board
        initGameboard();

        // Game Controller
        gameController = new GameController(this);
    }

    private void initGameboard() {
        final var scaleFactor = 3;
        gameBoard = new GameBoard(scaleFactor * ORIGINAL_GAME_BOUNDS.getWidth(), scaleFactor * ORIGINAL_GAME_BOUNDS.getHeight());

        // Border
//        gameBoard.setBorder(
//            new Border(
//                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))
//            )
//        );

        // Scaling
//        gameBoard.scaleXProperty().bind(
//            Bindings
//                .when(widthProperty().divide(heightProperty()).lessThanOrEqualTo(ASPECT_RATIO))
//                .then(widthProperty().divide(gameBoard.widthProperty()))
//                .otherwise(heightProperty().multiply(ASPECT_RATIO).divide(gameBoard.widthProperty()))
//        );
//        gameBoard.scaleYProperty().bind(gameBoard.scaleXProperty());

        // Size
        gameBoard.widthProperty().bind(
            Bindings
                .when(widthProperty().divide(heightProperty()).lessThanOrEqualTo(ASPECT_RATIO))
                .then(widthProperty())
                .otherwise(heightProperty().multiply(ASPECT_RATIO))
        );
        gameBoard.heightProperty().bind(gameBoard.widthProperty().divide(ASPECT_RATIO));
//
//        // Positioning
        gameBoard.translateXProperty().bind(widthProperty().subtract(gameBoard.widthProperty()).divide(2));
        gameBoard.translateYProperty().bind(heightProperty().subtract(gameBoard.heightProperty()).divide(2));

//        root.getChildren().add(gameBoard);
        root.getChildren().add(gameBoard);

        final GraphicsContext gc = gameBoard.getGraphicsContext2D();

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
    }

    public void apply(final Stage stage) {
        stage.setTitle("Space Invaders");
        stage.setScene(this);
        stage.show();
    }

    public GameController getGameController() {
        return gameController;
    }
}
