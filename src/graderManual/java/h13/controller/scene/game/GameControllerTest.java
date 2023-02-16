package h13.controller.scene.game;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.ApplicationSettings;
import h13.controller.gamelogic.EnemyController;
import h13.controller.gamelogic.GameInputHandler;
import h13.controller.gamelogic.PlayerController;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.IDBullet;
import h13.model.gameplay.sprites.IDEnemy;
import h13.model.gameplay.sprites.IDPlayer;
import h13.model.gameplay.sprites.WithID;
import h13.shared.JFXUtils;
import h13.util.StudentLinks;
import h13.view.gui.GameScene;
import javafx.animation.AnimationTimer;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static h13.util.StudentLinks.BulletLinks.BulletFieldLink.OWNER_FIELD;
import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.*;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerFieldLink.*;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerMethodLink.HANDLE_KEYBOARD_INPUTS_METHOD;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@TestForSubmission
public class GameControllerTest {

    private Stage stage;
    private GameScene gameScene;

    @SuppressWarnings("unused")
    public final static Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("GAME_BOUNDS", JsonConverter::toBounds),
        Map.entry("enemies", JsonConverter::toIDEnemyList),
        Map.entry("bullets", JsonConverter::toIDBulletList),
        Map.entry("player", JsonConverter::toIDPlayer),
        Map.entry("bulletOwners", JsonConverter::toIntMap),
        Map.entry("hits", JsonConverter::toIntMap),
        Map.entry("damaged", jsonNode -> JsonConverter.toMap(jsonNode, Integer::parseInt, JsonNode::asBoolean))
    );

    public GameController setupGameController(final JsonParameterSet params) {
        // preparation
        final var origGameBounds = new BoundingBox(0, 0, 256, 224);
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(origGameBounds);
        ASPECT_RATIO_FIELD.setStatic(origGameBounds.getWidth() / origGameBounds.getHeight());
        ApplicationSettings.loadTexturesProperty().set(false);
        final var gameBounds = params.get("GAME_BOUNDS", Bounds.class);
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(gameBounds);
        ASPECT_RATIO_FIELD.setStatic(gameBounds.getWidth() / gameBounds.getHeight());
        final var SHIP_SIZE = params.getDouble("SHIP_SIZE");
        SHIP_SIZE_FIELD.setStatic(SHIP_SIZE);

        // setup Model
        final var gameState = new GameState();
        gameState.getSprites().clear();

        final IDPlayer player = spy(params.get("player", IDPlayer.class));
        gameState.getSprites().add(player);

        final List<IDEnemy> enemies = params.<List<IDEnemy>>get("enemies").stream().map(Mockito::spy).toList();
        gameState.getSprites().addAll(enemies);

        final var bullets = params.<List<IDBullet>>get("bullets").stream().map(Mockito::spy).toList();
        final Map<Integer, Integer> bulletOwners = params.get("bulletOwners");
        bulletOwners.forEach((bulletID, ownerID) -> {
            final var bullet = bullets.stream().filter(b -> b.getId() == bulletID).findFirst().orElseThrow(() -> new RuntimeException("Invalid Test: Bullet not found"));
            // find owner
            final var owner = gameState.getSprites().stream()
                .filter(WithID.class::isInstance)
                .map(WithID.class::cast)
                .filter(s -> s.getId() == ownerID)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid Test: Owner not found"));
            OWNER_FIELD.set(bullet, owner);
        });
        gameState.getSprites().addAll(bullets);

        // setup Controller
        final var gameController = spy(mock(GameController.class, Answers.CALLS_REAL_METHODS));
        GAME_STATE_FIELD.set(gameController, gameState);
        GAME_SCENE_FIELD.set(gameController, gameScene);
        STAGE_FIELD.set(gameController, stage);

        final var playerController = spy(mock(PlayerController.class, Answers.CALLS_REAL_METHODS));
        StudentLinks.PlayerControllerLinks.PlayerControllerFieldLink.GAME_CONTROLLER_FIELD.set(playerController, gameController);
        gameController.setPlayerController(playerController);
        StudentLinks.PlayerControllerLinks.PlayerControllerFieldLink.PLAYER_FIELD.set(
            gameController.getPlayerController(),
            player
        );

        final var enemyController = mock(EnemyController.class, Answers.CALLS_REAL_METHODS);
        StudentLinks.EnemyControllerLinks.EnemyControllerFieldLink.GAME_CONTROLLER_FIELD.set(enemyController, gameController);
        gameController.setEnemyController(enemyController);

        gameController.setGameInputHandler(new GameInputHandler(gameScene));

        return gameController;
    }

    //    @Override
    @BeforeEach
    public void start() throws Exception {
//        super.start(stage);
        JFXUtils.initFX();
        JFXUtils.onJFXThread(() -> {
            var stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            final var origGameBounds = new BoundingBox(0, 0, 256, 224);
            ORIGINAL_GAME_BOUNDS_FIELD.setStatic(origGameBounds);
            ASPECT_RATIO_FIELD.setStatic(origGameBounds.getWidth() / origGameBounds.getHeight());
            ApplicationSettings.loadBackgroundProperty().set(false);

            // TODO: test with private init() for final grading run
            gameScene = spy(new GameScene() {
                @Override
                protected void init() {
                    // do nothing
                }
            });

            stage.setScene(gameScene);
            this.stage = stage;
        });
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
    public void testHandleKeyboardInputsFullScreenF11(final JsonParameterSet params) throws InterruptedException {
        // setup
        final var gameController = setupGameController(params);
        final var gameState = gameController.getGameState();
        final var context = params.toContext();


        // test setup
        HANDLE_KEYBOARD_INPUTS_METHOD.invoke(context, gameController);

        // make sure the game is not in full screen mode
        JFXUtils.onJFXThread(() -> {
            stage.setFullScreen(false);

            // send F11 key press
            final var keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                KeyCode.F11,
                false,
                false,
                false,
                false
            );
            gameScene.getOnKeyPressed().handle(keyEvent);

            // release F11 key press
            final var keyEvent2 = new KeyEvent(
                KeyEvent.KEY_RELEASED,
                "",
                "",
                KeyCode.F11,
                false,
                false,
                false,
                false
            );
            gameScene.getOnKeyReleased().handle(keyEvent2);

            // assert that the game is now in full screen mode
            Assertions2.assertTrue(stage.isFullScreen(), context, r -> "Game should be in full screen mode after pressing F11");
        });
    }

    private void testHandleKeyboardInputsEscape(JsonParameterSet params, boolean hitLose) throws InterruptedException, IOException {
        // setup
        final var gameController = setupGameController(params);
        final var gameState = gameController.getGameState();
        final var context = params.toContext();
        AtomicBoolean pausedWasOnceTrue = new AtomicBoolean(false);
        AtomicBoolean pausedWasOnceFalse = new AtomicBoolean(false);
        GAME_LOOP_FIELD.set(gameController, new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameController.isPaused()) {
                    pausedWasOnceTrue.set(true);
                } else {
                    pausedWasOnceFalse.set(true);
                }
            }
        });
        gameController.getGameLoop().start();

        final var crs = ClassReloadingStrategy.fromInstalledAgent();
        final class LoseMethodSpy {
            static int invocations = 0;

            public static void lose() {
                invocations++;
            }
        }

        LoseMethodSpy.invocations = 0;

        try {
            // use bytebuddy to intercept GameController.lose() call to check if it was called
            new ByteBuddy()
                .redefine(GameController.class)
                .method(ElementMatchers.named("lose"))
                .intercept(MethodDelegation.to(LoseMethodSpy.class))
                .make()
                .load(GameController.class.getClassLoader(), crs);

            // test setup
            HANDLE_KEYBOARD_INPUTS_METHOD.invoke(context, gameController);

            JFXUtils.onJFXThread(() -> {
                stage.show();
                // send Escape key press
                final var keyEvent = new KeyEvent(
                    KeyEvent.KEY_PRESSED,
                    "",
                    "",
                    KeyCode.ESCAPE,
                    false,
                    false,
                    false,
                    false
                );
                gameScene.getOnKeyPressed().handle(keyEvent);

                // release Escape key press
                final var keyEvent2 = new KeyEvent(
                    KeyEvent.KEY_RELEASED,
                    "",
                    "",
                    KeyCode.ESCAPE,
                    false,
                    false,
                    false,
                    false
                );
                // instruct tutor to press the correct button
                if (hitLose) {
                    System.out.println("Press the lose button");
                } else {
                    System.out.println("Press the resume button");
                }

                gameScene.getOnKeyReleased().handle(keyEvent2);

            });
        } finally {
            // reset the class loader
            crs.reset(GameController.class);
            gameController.getGameLoop().stop();

            // assert that the game was paused
            Assertions2.assertTrue(pausedWasOnceTrue.get(), context, r -> "Game should be paused after pressing Escape");

            if (hitLose) {
                // assert that the lose method was called
                System.out.println("lose() Method was called " + LoseMethodSpy.invocations + " times.");
                Assertions2.assertEquals(1, LoseMethodSpy.invocations, context, r -> "Lose could not be selected or lose() Method was not called.");
            } else {
                // assert that the game was resumed
                Assertions2.assertFalse(gameController.isPaused(), context, r -> "Game should be resumed after choosing resume. Either resume() was not called or it was not possible to select the resume button.");

                // assert that the lose method was not called
                Assertions2.assertEquals(0, LoseMethodSpy.invocations, context, r -> "The lose() Method was called even though the resume button was selected or the resume button could not be selected.");
            }
        }
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
    public void testHandleKeyboardInputsEscapeLose(final JsonParameterSet params) throws InterruptedException, IOException {
        testHandleKeyboardInputsEscape(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
    public void testHandleKeyboardInputsEscapeResume(final JsonParameterSet params) throws InterruptedException, IOException {
        testHandleKeyboardInputsEscape(params, false);
    }
}
//    @ExtendWith(JagrExecutionCondition.class)
//        TestCycleResolver.getTestCycle().getClassLoader().l
