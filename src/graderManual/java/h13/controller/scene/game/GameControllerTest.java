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
import h13.model.gameplay.sprites.*;
import h13.util.StudentLinks;
import h13.view.gui.GameScene;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.testfx.framework.junit5.ApplicationTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static h13.util.StudentLinks.BulletLinks.BulletFieldLink.OWNER_FIELD;
import static h13.util.StudentLinks.BulletLinks.BulletMethodLink.HIT_METHOD;
import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.*;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerFieldLink.*;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerMethodLink.*;
import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.IS_DEAD_METHOD;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@TestForSubmission
public class GameControllerTest extends ApplicationTest {

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

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
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
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
    public void testHandleKeyboardInputsFullScreenF11(final JsonParameterSet params) {
        // setup
        final var gameController = setupGameController(params);
        final var gameState = gameController.getGameState();
        final var context = params.toContext();


        // test setup
        HANDLE_KEYBOARD_INPUTS_METHOD.invoke(context, gameController);

        // make sure the game is not in full screen mode
        Platform.runLater(() -> {
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

//    @ParameterizedTest
//    @JsonParameterSetTest(value = "GameControllerDummyState.json", customConverters = "customConverters")
//    public void testHandleKeyboardInputsFullScreenEscape(final JsonParameterSet params) {
//        // setup
//        final var gameController = setupGameController(params);
//        final var gameState = gameController.getGameState();
//        final var context = params.toContext();
//        GAME_LOOP_FIELD.set(gameController, spy(AnimationTimer.class));
//
//
//        // test setup
//        HANDLE_KEYBOARD_INPUTS_METHOD.invoke(context, gameController);
//
//        Platform.runLater(() -> {
//            stage.show();
//            // send Escape key press
//            final var keyEvent = new KeyEvent(
//                KeyEvent.KEY_PRESSED,
//                "",
//                "",
//                KeyCode.ESCAPE,
//                false,
//                false,
//                false,
//                false
//            );
//            gameScene.getOnKeyPressed().handle(keyEvent);
//
//            // release Escape key press
//            final var keyEvent2 = new KeyEvent(
//                KeyEvent.KEY_RELEASED,
//                "",
//                "",
//                KeyCode.ESCAPE,
//                false,
//                false,
//                false,
//                false
//            );
//            gameScene.getOnKeyReleased().handle(keyEvent2);
//
//            // assert that the game is now in full screen mode
////            Assertions2.assertTrue(stage.isFullScreen(), context, r -> "Game should be in full screen mode after pressing F11");
//        });
//    }
}
//    @ExtendWith(JagrExecutionCondition.class)
//        TestCycleResolver.getTestCycle().getClassLoader().l
