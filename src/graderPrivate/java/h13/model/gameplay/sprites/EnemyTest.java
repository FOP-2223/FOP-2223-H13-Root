package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.util.PrettyPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static h13.util.StudentLinks.BattleShipLinks.BattleShipMethodLink.SHOOT_METHOD;
import static h13.util.StudentLinks.EnemyLinks.EnemyMethodLink.UPDATE_METHOD;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class EnemyTest {
    private Enemy enemy;

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        // replace Math.random() calls in Enemy with MathRandomTester.random() calls
    }

    @Test
    public void testUpdateShootCalledWithMaxProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        final var context = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 10)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();
        UPDATE_METHOD.invoke(context, enemy, 10);
        SHOOT_METHOD.assertInvokedNTimesWithParams(context, enemy, 1, Direction.DOWN);
    }

    @Test
    public void testUpdateWithMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0);
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        final var context = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 0)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();

        UPDATE_METHOD.invoke(context, enemy, 0);
        verify(enemy, never().description(context.toString())).shoot(any());
    }

    @Test
    public void testUpdateWithDelay() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(1000);
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        final var context = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 0)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();
        UPDATE_METHOD.invoke(context, enemy, 0);
        verify(enemy, never().description(context.toString())).shoot(Direction.DOWN);
        final var context2 = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 1000)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();
        UPDATE_METHOD.invoke(context2, enemy, 1000);
        verify(enemy, times(1).description(context2.toString())).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithDelayAndMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0);
        ApplicationSettings.enemyShootingDelayProperty().set(1000);
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        final var context = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 0)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();
        UPDATE_METHOD.invoke(context, enemy, 0);
        verify(enemy, never().description(context.toString())).shoot(Direction.DOWN);
        final var context2 = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 1000)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();
        UPDATE_METHOD.invoke(context2, enemy, 1000);
        verify(enemy, never().description(context2.toString())).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithFiftyPercentProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0.5);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        final var context = contextBuilder()
            .add("enemy", PrettyPrinter.prettyPrint(enemy))
            .add("shootingProbability", ApplicationSettings.enemyShootingProbabilityProperty().get())
            .add("shootingDelay", ApplicationSettings.enemyShootingDelayProperty().get())
            .add("elapsedTime", 0)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .add("note:", "this test case tests 1000 iterations of the update method, and is therefore not deterministic."
                + "If you are certain that your implementation is correct but you still get a failure, write a complaint")
            .build();
        for (double i = 0; i <= 1; i+=0.001) {
            enemy.update(1000);
        }
        verify(enemy, atLeast(1).description(context.toString())).shoot(Direction.DOWN);
        verify(enemy, atMost(999).description(context.toString())).shoot(Direction.DOWN);
    }
}
