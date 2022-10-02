package h13.controller.scene.game;

import h13.controller.ApplicationSettings;
import h13.controller.gamelogic.EnemyController;
import h13.controller.gamelogic.GameInputHandler;
import h13.controller.gamelogic.PlayerController;
import h13.controller.scene.SceneController;
import h13.controller.scene.SceneSwitcher;
import h13.model.HighscoreEntry;
import h13.model.gameplay.GameState;
import h13.model.gameplay.Updatable;
import h13.model.gameplay.sprites.*;
import h13.view.gui.GameBoard;
import h13.view.gui.GameScene;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link SceneController} that controls the {@link GameScene}.
 * It is also responsible for the game loop and various other parts of the game logic.
 */

public class GameController extends SceneController implements Updatable {

    // --Variables-- //

    /**
     * The {@link GameState} that is our main access point to the model.
     */
    private final GameState gameState = new GameState();

    /**
     * The {@link GameScene} that is controlled by this {@link GameController}.
     *
     * @see #gameScene
     */
    private final GameScene gameScene;

    /**
     * The last time the game loop was updated.
     */
    private double lastUpdate = 0;

    /**
     * Whether the game is paused.
     */
    private boolean paused = false;

    /**
     * The {@link PlayerController} that controls the {@link Player}.
     *
     * @see #playerController
     */
    private PlayerController playerController;
    /**
     * The {@link EnemyController} that controls the {@link h13.model.gameplay.sprites.Enemy}s.
     *
     * @see #enemyController
     */
    private EnemyController enemyController;

    /**
     * The {@link GameInputHandler} that handles the input of the player.
     *
     * @see #gameInputHandler
     */
    private GameInputHandler gameInputHandler;

    /**
     * A {@link AnimationTimer} that represents the game loop.
     */
    private final AnimationTimer gameLoop = new AnimationTimer() {
        @Override
        public void handle(final long now) {
            if (lastUpdate > 0 && !isPaused()) {
                final double elapsedTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                update(elapsedTime);
            } else {
                lastUpdate = now;
            }
        }
    };

    // --Constructors-- //

    /**
     * Creates a new {@link GameController}.
     *
     * @param gameScene The {@link GameScene} that is controlled by this {@link GameController}.
     */
    public GameController(final GameScene gameScene) {
        this.gameScene = gameScene;
        init();
    }

    // --Getters and Setters-- //

    /**
     * Gets the value of {@link #gameState} field.
     *
     * @return The value of {@link #gameState} field.
     * @see #gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Gets the value of {@link #gameScene} field.
     *
     * @return The value of {@link #gameScene} field.
     * @see #gameScene
     */
    public GameScene getGameScene() {
        return gameScene;
    }

    /**
     * Gets the value of {@link #playerController} field.
     *
     * @return The value of {@link #playerController} field.
     * @see #playerController
     */
    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * Sets the value of {@link #playerController} field to the given value.
     *
     * @param playerController The new {@link #playerController}.
     * @see #playerController
     */
    public void setPlayerController(final PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Gets the value of {@link #enemyController} field.
     *
     * @return The value of {@link #enemyController} field.
     * @see #enemyController
     */
    public EnemyController getEnemyController() {
        return enemyController;
    }

    /**
     * Sets the value of {@link #enemyController} field to the given value.
     *
     * @param enemyController The new {@link #enemyController}.
     * @see #enemyController
     */
    public void setEnemyController(final EnemyController enemyController) {
        this.enemyController = enemyController;
    }

    /**
     * Gets the value of {@link #gameInputHandler} field.
     *
     * @return The value of {@link #gameInputHandler} field.
     * @see #gameInputHandler
     */
    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }

    /**
     * Sets the value of {@link #gameInputHandler} field to the given value.
     *
     * @param gameInputHandler The new {@link #gameInputHandler}.
     * @see #gameInputHandler
     */
    public void setGameInputHandler(final GameInputHandler gameInputHandler) {
        this.gameInputHandler = gameInputHandler;
    }

    /**
     * Gets the value of the {@link #gameLoop} field.
     *
     * @return The value of the {@link #gameLoop} field.
     * @see #gameLoop
     */
    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    // --Utility Methods-- //

    /**
     * Retrieves the {@link Player} from the {@link #playerController}.
     *
     * @return The {@link Player} of the {@link #playerController}.
     */
    public Player getPlayer() {
        return playerController.getPlayer();
    }

    /**
     * Retrieves the {@link GameBoard} from the {@link #gameScene}.
     *
     * @return The {@link GameBoard} of the {@link #gameScene}.
     */
    public GameBoard getGameBoard() {
        return gameScene.getGameBoard();
    }

    /**
     * Checks whether the game is Paused.
     *
     * @return {code true} if the game is paused, {code false} otherwise.
     * @see #paused
     */
    public boolean isPaused() {
        return paused;
    }

    @Override
    public String getTitle() {
        return "Space Invaders";
    }

    @Override
    public void initStage(final Stage stage) {
        super.initStage(stage);

        // Full Screen
        stage.setFullScreen(ApplicationSettings.fullscreenProperty().get());
    }

    /**
     * Initializes the {@link #gameScene}.
     */
    private void init() {
        // Keyboard input handler
        setGameInputHandler(new GameInputHandler(getGameScene()));

        // Player
        setPlayerController(new PlayerController(this));

        // Enemies
        setEnemyController(new EnemyController(this));

        // register keybindings for the game scene
        handleKeyboardInputs();

        // start the game loop
        gameLoop.start();
    }

    /**
     * Changes {@link #paused} to {@code true}.
     */
    public void pause() {
        paused = true;
    }

    /**
     * Changes {@link #paused} to {@link false}.
     */
    public void resume() {
        paused = false;
    }

    /**
     * Prepares the next level if the current level is finished.
     */
    public void refillEnemiesIfNecessary() {
        if (getEnemyController() != null && getEnemyController().defeated()) {
            enemyController.nextLevel();
        }
    }

    /**
     * Handles what happens when the {@linkplain Player player} is defeated.
     */
    private void lose() {
        Platform.runLater(() -> {
            pause();
            if (getPlayer().getScore() > 0) {
                ApplicationSettings.getHighscores().add(
                    new HighscoreEntry(
                        "getPlayer().getName()",
                        new Date().toString(),
                        getPlayer().getScore()
                    )
                );
            }
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Loose!!!\nContinue?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                reset();
                resume();
                //do stuff
            } else {
                try {
                    SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, (Stage) getGameScene().getWindow());
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Resets the sprites and starts a new game.
     */
    public void reset() {
        getGameState().getSprites().clear();
        init();
    }

    /**
     * Handles the keyboard inputs.
     */
    private void handleKeyboardInputs() {
        getGameInputHandler().addOnKeyReleased(k -> {
            // escape
            switch (k.getCode()) {
                case ESCAPE -> Platform.runLater(() -> {
                    pause();
                    final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit to main Menu?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        try {
                            SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, (Stage) getGameScene().getWindow());
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        resume();
                    }
                });

                // F11
                case F11 -> Platform.runLater(() -> {
                    final Stage stage = (Stage) getGameScene().getWindow();
                    stage.setFullScreen(!stage.isFullScreen());
                    System.out.println("Fullscreen: " + stage.isFullScreen());
                });
            }
        });
    }

    @Override
    public void update(final double elapsedTime) {
        // Movement
        updateOthers(elapsedTime);

        // Hit detection
        doCollisions();

        var killed = getGameState().getSprites().stream().filter(Sprite::isDead).collect(Collectors.toList());
        // check if the player is defeated
        updatePoints(killed);

        // check loose condition
        if (killed.contains(getPlayer()) || getGameState().getEnemyMovement().bottomWasReached()) {
            lose();
        }

        killed.forEach(getGameState().getSprites()::remove);

        refillEnemiesIfNecessary();
    }

    /**
     * Updates the {@link Sprite}s and the other Controllers.
     */
    private void updateOthers(double elapsedTime) {
        // update the Enemy Movement
        Platform.runLater(() -> {
            getGameState().getEnemyMovement().update(elapsedTime);
        });
        getGameState().getSprites()
            .stream()
            .filter(Sprite::isAlive)
            .forEach(s -> Platform.runLater(() -> s.update(elapsedTime)));
        getGameBoard().update(elapsedTime);
    }

    /**
     * Calculate the collision between the sprites and damages the collided sprites.
     */
    private void doCollisions() {
        getGameState().getSprites().stream().filter(Bullet.class::isInstance).map(Bullet.class::cast).forEach(b -> {
            final var damaged = getGameState().getSprites().stream()
                .filter(Sprite::isAlive)
                .filter(BattleShip.class::isInstance)
                .map(BattleShip.class::cast)
                .filter(b::hit)
                .findFirst().orElse(null);
            if (damaged != null) {
                damaged.damage(1);
                b.damage();
            }
        });
    }

    /**
     * Updates the points of the {@linkplain Player player}.
     *
     * @param damaged The damaged sprites.
     */
    public void updatePoints(final List<Sprite> damaged) {
        for (final Sprite sprite : damaged) {
            if (sprite instanceof Enemy e) {
                getPlayer().addPoints(e.getPointsWorth());
            }
        }
    }
}
