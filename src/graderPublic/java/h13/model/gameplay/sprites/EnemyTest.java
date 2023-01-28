package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static org.mockito.Mockito.*;

@TestForSubmission
public class EnemyTest {
    private Enemy enemy;

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        // replace Math.random() calls in Enemy with MathRandomTester.random() calls
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
    }

    @Test
    public void testUpdateShootCalledWithMaxProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        enemy.update(10);
        verify(enemy, times(1)).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
    }
}
