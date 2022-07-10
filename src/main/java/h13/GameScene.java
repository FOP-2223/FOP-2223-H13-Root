package h13;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends Scene {

    private Group root;

    private Sprite player;
    private Pane gameBoard;

    private List<KeyCode> keysPressed = new ArrayList<>();


    private double aspectRatio = 256d / 224d; // original aspect ratio of the game
    private Bounds lastGameboardSize;

    public GameScene() {
        super(new Group(), Color.BLACK);
        root = (Group) getRoot();
        init();
    }

    private Dimension2D getGameBounds() {
        var curASR = getWidth() / getHeight();
        return new Dimension2D(
            curASR < aspectRatio ? getWidth() : getHeight() * aspectRatio,
            curASR < aspectRatio ? getWidth() / aspectRatio : getHeight()
        );
    }

    private void init() {
        // Game Board
        gameBoard = new Pane();
        gameBoard.setBorder(
            new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(10))
            )
        );
        gameBoard.prefWidthProperty().bind(
            Bindings
                .when(widthProperty().divide(heightProperty()).lessThanOrEqualTo(aspectRatio))
                .then(widthProperty())
                .otherwise(heightProperty().multiply(aspectRatio))
        );
        gameBoard.prefHeightProperty().bind(
            Bindings
                .when(widthProperty().divide(heightProperty()).lessThanOrEqualTo(aspectRatio))
                .then(widthProperty().divide(aspectRatio))
                .otherwise(heightProperty())
        );
        gameBoard.translateXProperty().bind(widthProperty().subtract(gameBoard.widthProperty()).divide(2));
        gameBoard.translateYProperty().bind(heightProperty().subtract(gameBoard.heightProperty()).divide(2));
        root.getChildren().add(gameBoard);


        player = new Sprite(100, 100, 0.1, 0.1, Color.BLUE, SpriteType.PLAYER, 1.5, gameBoard);
        gameBoard.getChildren().add(player);

        // Keyboard input
        setOnKeyPressed(e -> {
            if (keysPressed.contains(e.getCode())) return;
            keysPressed.add(e.getCode());
            switch (e.getCode()) {
                case LEFT, A -> player.moveLeft();
                case RIGHT, D -> player.moveRight();
                case UP, W -> player.moveUp();
                case DOWN, S -> player.moveDown();
                case SPACE -> player.shoot();
            }
        });
        setOnKeyReleased(e -> {
            keysPressed.remove(e.getCode());    // remove the key from the list of pressed keys
            switch (e.getCode()) {
                case LEFT, A -> player.moveRight();
                case RIGHT, D -> player.moveLeft();
                case UP, W -> player.moveDown();
                case DOWN, S -> player.moveUp();
            }
            if (keysPressed.isEmpty()) {
                player.stop();
            }
        });

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            if (getHeight() > 0 && getWidth() > 0) {
                System.out.println("Height: " + getHeight() + " Width: " + getWidth());
                if (lastGameboardSize != null && lastGameboardSize.getHeight() > 0 && lastGameboardSize.getWidth() > 0) {
                    var oldX = player.getX();
                    var oldY = player.getY();
                    var newCurASR = getWidth() / getHeight();
                    Dimension2D newGameBounds = new Dimension2D(
                        newCurASR < aspectRatio ? getWidth() : getHeight() * aspectRatio,
                        newCurASR < aspectRatio ? getWidth() / aspectRatio : getHeight()
                    );

                    var factor = gameBoard.getBoundsInParent().getWidth() / lastGameboardSize.getWidth();
                    System.out.println("Factor: " + factor);
                    player.setX(factor * oldX);
//                    player.setY(factor * oldY);
//                    player.setY(factor * player.getY());
//                    player.setWidth(player.getRelativeWidth() * newGameBounds.getHeight());
//                    player.setHeight(player.getRelativeWidth() * newGameBounds.getHeight());
//                    player.setY(newGameBounds.getHeight() - player.getHeight());
                }
            }
            lastGameboardSize = gameBoard.getBoundsInParent();
        };
//
        widthProperty().addListener(stageSizeListener);
        heightProperty().addListener(stageSizeListener);

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }

    public void update() {
        gameBoard.getChildren().stream().filter(Sprite.class::isInstance).map(Sprite.class::cast).forEach(s -> {
            switch (s.getType()) {
                case BULLET -> {
                    s.setVelocityY(-s.getVelocity() * gameBoard.getHeight());
                }
            }
        });
    }

    public void apply(Stage stage) {
        stage.setTitle("Space Invaders");
        stage.setScene(this);
        stage.show();
    }
}
