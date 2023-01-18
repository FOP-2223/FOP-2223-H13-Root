package h13.view.gui;

import h13.controller.ApplicationSettings;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.util.StudentLinks;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.stage.Stage;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.testfx.framework.junit5.ApplicationTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.security.ProtectionDomain;

import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD;
import static h13.util.StudentLinks.GameSceneLinks.GameSceneMethodLink.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestForSubmission
public class GameSceneTest extends ApplicationTest {
    public Stage stage;
    public GameScene gameScene;
    public GameBoard gameBoard;

    public static class NoOpMethod {
        public static void noOp() {
        }
    }

    @Override
    public void start(final Stage stage) throws Exception {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(0, 0, 256, 224));
        ApplicationSettings.loadBackgroundProperty().set(false);

        // use bytebuddy to intercept init() call when gameScene is constructed
        new AgentBuilder.Default()
            .type(ElementMatchers.nameStartsWith("h13.view.gui.GameScene"))
            .transform((
                           DynamicType.Builder<?> builder,
                           TypeDescription typeDescription,
                           ClassLoader classLoader,
                           JavaModule module,
                           ProtectionDomain protectionDomain
                       ) -> builder
                .method(ElementMatchers.named("init"))
                .intercept(MethodDelegation.to(NoOpMethod.class))
            )
            .installOn(ByteBuddyAgent.install());

        Class<?> dynamicType = new ByteBuddy()
            .redefine(GameScene.class)
            .method(ElementMatchers.named("init"))
            .intercept(MethodDelegation.to(NoOpMethod.class))
            .make()
            .load(GameScene.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
            .getLoaded();
//
        gameScene = spy((GameScene) dynamicType.getConstructor().newInstance());

//        gameScene = spy(new GameScene());
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
        if (params.availableKeys().contains("ORIGINAL_GAME_BOUNDS")) {
            ORIGINAL_GAME_BOUNDS_FIELD.setStatic(context, params.get("ORIGINAL_GAME_BOUNDS", Bounds.class));
        }
        setSize(params.get("gameSceneBounds", Bounds.class));
        INIT_GAMEBOARD_METHOD.invoke(context,gameScene);
        gameBoard = GET_GAME_BOARD_METHOD.invoke(context, gameScene);
        final Bounds expectedBounds = params.get("expectedGameBoardBounds", Bounds.class);
        if (params.getBoolean("checkSize")) {
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
        if(params.getBoolean("checkPosition")) {
            Assertions2.assertEquals(
                expectedBounds.getMinX(),
                gameBoard.getLayoutX(),
                context,
                r -> "The x position of the GameBoard is not correct."
            );
            Assertions2.assertEquals(
                expectedBounds.getMinY(),
                gameBoard.getLayoutY(),
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
}
