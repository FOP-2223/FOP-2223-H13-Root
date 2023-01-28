package h13.view.gui;

import h13.controller.ApplicationSettings;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.testfx.framework.junit5.ApplicationTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import javax.annotation.Nullable;
import java.security.ProtectionDomain;

import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ASPECT_RATIO_FIELD;
import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD;
import static h13.util.StudentLinks.GameSceneLinks.GameSceneMethodLink.*;
import static org.mockito.Mockito.spy;

@TestForSubmission
public class GameSceneTest extends ApplicationTest {
    public Stage stage;
    public GameScene gameScene;
    public GameBoard gameBoard;

    public static class NoOpMethod {
        @SuppressWarnings("unused")
        public static void noOp() {
        }
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        final var origGameBounds = new BoundingBox(0, 0, 256, 224);
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(origGameBounds);
        ASPECT_RATIO_FIELD.setStatic(origGameBounds.getWidth() / origGameBounds.getHeight());
        ApplicationSettings.loadBackgroundProperty().set(false);

        gameScene = spy(new GameScene(){
            @Override
            protected void init() {
                // do nothing
            }
        });

        stage.setScene(gameScene);
        this.stage = stage;
        //stage.show();
    }

    private void setSize(final Bounds bounds) {
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        SET_WIDTH_METHOD.invoke(gameScene, bounds.getWidth());
        SET_HEIGHT_METHOD.invoke(gameScene, bounds.getHeight());
    }

    public void testGameBoardSize(final JsonParameterSet params) {
        final var context = params.toContext("expectedGameBoardBounds");
        if (params.availableKeys().contains("GAME_BOUNDS")) {
            final var gameBounds = params.get("GAME_BOUNDS", Bounds.class);
            ORIGINAL_GAME_BOUNDS_FIELD.setStatic(context, gameBounds);
            ASPECT_RATIO_FIELD.setStatic(context, gameBounds.getWidth() / gameBounds.getHeight());
        }
        setSize(params.get("gameSceneBounds", Bounds.class));
        INIT_GAMEBOARD_METHOD.invoke(context, gameScene);
        gameBoard = GET_GAME_BOARD_METHOD.invoke(context, gameScene);
        assertSize(
            params.toContext("expectedGameBoardBounds", "expectedGameBoardBounds2", "gameSceneBounds2"),
            null,
            params.get("expectedGameBoardBounds", Bounds.class),
            params.getBoolean("checkSize"),
            params.getBoolean("checkPosition"),
            params.getBoolean("checkCentered")
        );
        if (params.availableKeys().contains("expectedGameBoardBounds2")) {
            assertSize(
                Assertions2.contextBuilder()
                    .add(params.toContext(
                        "expectedGameBoardBounds2",
                        "expectedGameBoardBounds",
                        "gameSceneBounds"
                    ))
                    .add("Note", "This run re-uses the same gameScene instance, to verify automatic resizing. You may use bindings to achieve this.")
                    .build(),
                params.get("gameSceneBounds2", Bounds.class),
                params.get("expectedGameBoardBounds2", Bounds.class),
                params.getBoolean("checkSize"),
                params.getBoolean("checkPosition"),
                params.getBoolean("checkCentered")
            );
        }
    }

    private void assertSize(
        final Context context,
        final @Nullable Bounds sceneBounds,
        final Bounds expectedBounds,
        final boolean checkSize,
        final boolean checkPosition,
        final boolean checkCentered
    ) {
        if (sceneBounds != null) {
            setSize(sceneBounds);
        }
        if (checkSize) {
            Assertions2.assertEquals(
                expectedBounds.getWidth(),
                gameBoard.getWidth(),
                context,
                r -> "The width of the GameBoard is not correct."
            );
            Assertions2.assertEquals(
                expectedBounds.getHeight(),
                gameBoard.getHeight(),
                context,
                r -> "The height of the GameBoard is not correct."
            );
        }
        if (checkPosition) {
            Assertions2.assertEquals(
                expectedBounds.getMinX(),
                gameBoard.getBoundsInParent().getMinX(),
                context,
                r -> "The x position of the GameBoard is not correct."
            );
            Assertions2.assertEquals(
                expectedBounds.getMinY(),
                gameBoard.getBoundsInParent().getMinY(),
                context,
                r -> "The y position of the GameBoard is not correct."
            );
        }
        if (checkCentered) {
            Assertions2.assertEquals(
                gameScene.getWidth() / 2,
                gameBoard.getBoundsInParent().getCenterX(),
                context,
                r -> "The x position of the GameBoard is not correct."
            );
            Assertions2.assertEquals(
                gameScene.getHeight() / 2,
                gameBoard.getBoundsInParent().getCenterY(),
                context,
                r -> "The y position of the GameBoard is not correct."
            );
        }
    }

    @ParameterizedTest
    @JsonParameterSetTest("GameSceneTestSizeCorrectWithOriginalAspectRatio.json")
    public void testSizeCorrectWithOriginalAspectRatio(final JsonParameterSet params) {
        testGameBoardSize(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest("GameSceneTestSizeCorrectWithDifferentAspectRatio.json")
    public void testSizeCorrectWithDifferentAspectRatio(final JsonParameterSet params) {
        testGameBoardSize(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest("GameSceneTestCentering.json")
    public void testCentering(final JsonParameterSet params) {
        testGameBoardSize(params);
    }
    @ParameterizedTest
    @JsonParameterSetTest("GameSceneTestAllInOne.json")
    public void combinedTest(final JsonParameterSet params) {
        testGameBoardSize(params);
    }
}
