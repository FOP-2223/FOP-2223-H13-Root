package h13.view.gui;

import javafx.geometry.BoundingBox;
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
import org.junit.jupiter.api.Test;
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
        public static void noOp() {}
    }

    @Override
    public void start(final Stage stage) throws Exception {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(0, 0, 256, 224));

        // use bytebuddy to intercept init() call when gameScene is constructed
        new AgentBuilder.Default()
            .type(ElementMatchers.nameStartsWith("h13.view.gui.GameScene"))
            .transform((
                           DynamicType.Builder<?> builder,
                           TypeDescription typeDescription,
                           ClassLoader classLoader,
                           JavaModule module,
                           ProtectionDomain protectionDomain) -> builder
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
        gameScene = spy((GameScene)dynamicType.getConstructor().newInstance());

//        gameScene = spy(new GameScene());
        stage.setScene(gameScene);
        this.stage = stage;
        //stage.show();
    }

    private void setSize(final double width, final double height) {
        stage.setWidth(width);
        stage.setHeight(height);
        SET_WIDTH_METHOD.invoke(gameScene, width);
        SET_HEIGHT_METHOD.invoke(gameScene, height);
    }

    @Test
    public void testSizeCorrectWithOriginalAspectRatio() throws InterruptedException {
        setSize(256, 224);
        INIT_GAMEBOARD_METHOD.invoke(gameScene);
        gameBoard = gameScene.getGameBoard();
        Assertions2.assertEquals(256d, gameBoard.getWidth(), Assertions2.emptyContext(), e -> "The width of the GameBoard is not correct.");
        Assertions2.assertEquals(224d, gameBoard.getHeight(), Assertions2.emptyContext(), e -> "The height of the GameBoard is not correct.");
    }
}
