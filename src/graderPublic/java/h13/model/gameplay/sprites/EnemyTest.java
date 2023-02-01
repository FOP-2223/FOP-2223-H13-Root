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
        verify(enemy, never()).shoot(any());
    }
}
