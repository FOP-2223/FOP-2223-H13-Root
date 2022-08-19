package h13.controller.scene.game;

import h13.controller.ApplicationSettings;
import h13.controller.gamelogic.EnemyController;
import h13.controller.gamelogic.GameInputHandler;
import h13.controller.gamelogic.PlayerController;
import h13.controller.scene.SceneController;
import h13.controller.scene.SceneSwitcher;
import h13.model.HighscoreEntry;
import h13.model.gameplay.Updatable;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.Sprite;
import h13.view.gui.GameBoard;
import h13.view.gui.GameScene;
import h13.model.gameplay.sprites.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A {@link SceneController} that controls the {@link GameScene}.
 * It is also responsible for the game loop and various other parts of the game logic.
 */

public class GameController extends SceneController implements Updatable {

    // --Variables-- //

    /**
     * The {@link Sprite}s that are present in the game.
     * This is a set because there is no guarantee that the sprites are not duplicated.
     * This is not a problem because the set is used to remove duplicates.
     *
     * @see #getSprites()
     */
    private final Set<Sprite> sprites = new HashSet<>();

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
     * The {@link GameState} of the game.
     *
     * @see #gameState
     */
    private GameState gameState = GameState.PLAYING;

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
     * Gets the value of {@link #sprites} field.
     *
     * @return The value of {@link #sprites} field.
     * @see #sprites
     */
    public Set<Sprite> getSprites() {
        return sprites;
    }

    /**
     * Gets all the {@link Sprite}s that are an instance of the given {@link Class}.
     *
     * @param clazz The {@link Class} of the {@link Sprite}s that are to be returned.
     * @param <T>   The type of the {@link Sprite}s that are to be returned.
     * @return All the {@link Sprite}s that are an instance of the given {@link Class}.
     */
    public <T extends Sprite> Set<T> getSprites(final Class<T> clazz) {
        return getSprites().stream().filter(clazz::isInstance).map(clazz::cast).collect(HashSet::new, Set::add, Set::addAll);
    }

    /**
     * Gets all the {@link Sprite}s that fulfill the given {@link Predicate}.
     *
     * @param predicate The {@link Predicate} that is used to filter the {@link Sprite}s.
     * @return All the {@link Sprite}s that fulfill the given {@link Predicate}.
     */
    public Set<Sprite> getSprites(final Predicate<Sprite> predicate) {
        return getSprites().stream().filter(predicate).collect(HashSet::new, Set::add, Set::addAll);
    }

    /**
     * Adds the given {@link Sprite} to the {@link #sprites} set.
     *
     * @param sprite The {@link Sprite} to be added.
     */
    public void addSprite(final Sprite sprite) {
        getSprites().add(sprite);
    }

    /**
     * Removes the given {@link Sprite} from the {@link #sprites} set.
     *
     * @param sprite The {@link Sprite} to be removed.
     */
    public void removeSprite(final Sprite sprite) {
        getSprites().remove(sprite);
    }

    /**
     * Clears the {@link #sprites} set.
     */
    public void clearSprites() {
        getSprites().clear();
    }

    /**
     * Clears all the {@link Sprite}s that are an instance of the given {@link Class} from the {@link #sprites} set.
     *
     * @param clazz The {@link Class} of the {@link Sprite}s that are to be removed.
     */
    public void clearSprites(final Class<? extends Sprite> clazz) {
        getSprites().stream().filter(clazz::isInstance).forEach(sprite -> getSprites().remove(sprite));
    }

    /**
     * Clears all the {@link Sprite}s that fulfill the given {@link Predicate} from the {@link #sprites} set.
     *
     * @param predicate The {@link Predicate} that is used to filter the {@link Sprite}s.
     */
    public void clearSprites(final Predicate<Sprite> predicate) {
        getSprites().stream().filter(predicate).forEach(getSprites()::remove);
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
     * Gets the value of {@link #gameState} field.
     *
     * @return The value of {@link #gameState} field.
     * @see #gameState
     */
    public GameState getGameState() {
        return gameState;
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
     * @see #gameState
     */
    public boolean isPaused() {
        return getGameState().equals(GameState.PAUSED);
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
     * resumes the object after pausing. This is necessary, when the game resumes after a pause.
     *
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    public void resume(final long now) {
        lastUpdate = now;
    }

    /**
     * Changes the {@link #gameState} to {@link GameState#PAUSED}.
     */
    public void pause() {
        gameState = GameState.PAUSED;
    }

    /**
     * Changes the {@link #gameState} to {@link GameState#PLAYING}.
     */
    public void resume() {
        gameState = GameState.PLAYING;
    }

    /**
     * Prepares the next level.
     */
    public void nextLevel() {
        enemyController.nextLevel();
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
        clearSprites();
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
        // update the game state
        Platform.runLater(() -> {
            if (enemyController != null && enemyController.getEnemyMovement() != null)
                enemyController.getEnemyMovement().update(elapsedTime);
        });
        getSprites()
            .stream()
            .filter(Objects::nonNull)
            .forEach(s -> Platform.runLater(() -> s.update(elapsedTime)));
        getGameBoard().update(elapsedTime);

        // check if the player is defeated
        if (getEnemyController() != null && getEnemyController().defeated()) {
            nextLevel();
        }
        if (getPlayer().isDead() || (getEnemyController() != null && getEnemyController().getEnemyMovement().bottomWasReached())) {
            lose();
        }
    }
}
