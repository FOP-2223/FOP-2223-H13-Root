package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static org.mockito.Mockito.*;

@TestForSubmission
public class EnemyTest {

    @BeforeAll
    public static void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
    }

    @Test
    public void testUpdateShootCalledWithMaxProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        final var enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        enemy.update(0);
        verify(enemy, times(1)).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        final var enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithDelay() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(1000);
        final var enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
        enemy.update(1000);
        verify(enemy, times(1)).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithDelayAndMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0);
        ApplicationSettings.enemyShootingDelayProperty().set(1000);
        final var enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
        enemy.update(1000);
        verify(enemy, never()).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithFifetyPercentProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0.5);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        final var enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
        // 50% chance of shooting, using Math.random()
    }
}
