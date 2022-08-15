package h13.controller.game;

import h13.controller.ApplicationSettings;
import h13.controller.scene.SceneController;
import h13.controller.scene.SceneSwitcher;
import h13.model.HighscoreEntry;
import h13.model.gameplay.GamePlay;
import h13.model.gameplay.Playable;
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

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class GameController extends SceneController implements Playable {
    private final GameScene gameScene;
    private GameState gameState;
    private final Set<Sprite> sprites = new HashSet<>();


    private GamePlay gamePlay;
    private EnemyController enemyController;
    private PlayerController playerController;

    private final AnimationTimer gameLoop = new AnimationTimer() {
        @Override
        public void handle(final long now) {
            if (resume) {
                resume();
                resume = false;
            } else {
                update(now);
            }
        }
    };

    public Set<Sprite> getSprites() {
        return sprites;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }

    public Player getPlayer() {
        return playerController.getPlayer();
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    public GameBoard getGameBoard() {
        return gameScene.getGameBoard();
    }

    private boolean resume = false;

    public <T extends Sprite> Set<T> getSprites(final Class<T> clazz) {
        return getSprites().stream().filter(clazz::isInstance).map(clazz::cast).collect(HashSet::new, Set::add, Set::addAll);
    }

    public Set<Sprite> getSprites(final Predicate<Sprite> predicate) {
        return getSprites().stream().filter(predicate).collect(HashSet::new, Set::add, Set::addAll);
    }

    public void addSprite(final Sprite sprite) {
        getSprites().add(sprite);
    }

    public void removeSprite(final Sprite sprite) {
        getSprites().remove(sprite);
    }

    public void clearSprites() {
        getSprites().clear();
    }

    public void clearSprites(final Class<? extends Sprite> clazz) {
        getSprites().stream().filter(clazz::isInstance).forEach(sprite -> getSprites().remove(sprite));
    }

    public void clearSprites(final Predicate<Sprite> predicate) {
        getSprites().stream().filter(predicate).forEach(getSprites()::remove);
    }


    public GameController(final GameScene gameScene) {
        this.gameScene = gameScene;
        init();
    }

    private void init() {
        gamePlay = new GamePlay(this);

        handleKeyboardInputs();

        gameLoop.start();
    }

    @Override
    public void initStage(final Stage stage) {
        super.initStage(stage);
        stage.setTitle("Space Invaders");

        // Full Screen
        stage.setFullScreen(ApplicationSettings.fullscreenProperty().get());
    }

    private void handleKeyboardInputs() {
        getGameScene().setOnKeyTyped(k -> {
            // escape
            if (k.getCharacter().equals("\u001b")) {
                Platform.runLater(() -> {
                    gameLoop.stop();
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
            }
        });
    }

    @Override
    public void update(final long now) {
        Platform.runLater(() -> {
            if (enemyController != null && enemyController.getEnemyMovement() != null)
                enemyController.getEnemyMovement().update(now);
        });
        getSprites()
            .stream()
            .filter(Objects::nonNull)
            .map(Playable.class::cast)
            .forEach(s -> {
                Platform.runLater(() -> s.update(now));
            });
//        getGameBoard().getSprites().forEach(s -> s.update(now));
        getGameBoard().update(now);
        if (getEnemyController() != null && getEnemyController().defeated()) {
            win();
        }
        if (getPlayer().isDead() || (getEnemyController() != null && getEnemyController().getEnemyMovement().bottomWasReached())) {
            lose();
        }
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void resume(final long now) {
        Platform.runLater(() -> {
            if (enemyController != null && enemyController.getEnemyMovement() != null)
                enemyController.getEnemyMovement().resume(now);
        });
        getSprites()
            .stream()
            .filter(Objects::nonNull)
            .map(Playable.class::cast)
            .forEach(s -> {
                Platform.runLater(() -> s.resume(now));
            });
    }

    private void lose() {
        Platform.runLater(() -> {
            gameLoop.stop();
            if (getPlayer().getScore() > 0) {
                ApplicationSettings.highscores.add(
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
                gameLoop.start();
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

    public void pause() {
        gameLoop.stop();
    }

    public void resume() {
        resume = true;
        gameLoop.start();
    }

    private void win() {
        Platform.runLater(() -> {
            gameLoop.stop();
//            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Won!!!\nContinue?", ButtonType.YES, ButtonType.NO);
//            alert.showAndWait();
//
//            if (alert.getResult() == ButtonType.YES) {
            final var score = getPlayer().getScore();
            final var health = getPlayer().getHealth();
            final var playerXCoordinate = getPlayer().getX();
            reset();
            getPlayer().setScore(score);
            getPlayer().setHealth(health);
            getPlayer().setX(playerXCoordinate);
            gameLoop.start();
            //do stuff
//            }
        });
    }

    public void reset() {
        clearSprites();
        init();
    }

    public void setEnemyController(final EnemyController enemyController) {
        this.enemyController = enemyController;
    }

    public void setPlayerController(final PlayerController playerController) {
        this.playerController = playerController;
    }
}
