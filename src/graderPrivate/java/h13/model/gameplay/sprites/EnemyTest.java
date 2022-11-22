package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.MemberSubstitution;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.security.ProtectionDomain;
import java.util.Random;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.*;

@TestForSubmission
@ExtendWith(MockitoExtension.class)
public class EnemyTest {
    private static Enemy enemy;

    @BeforeAll
    public static void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        // replace Math.random() calls in Enemy with MathRandomTester.random() calls
        ByteBuddyAgent.install().getAllLoadedClasses();
        new AgentBuilder
            .Default()
            .type(is(Enemy.class))
            .transform(
                (builder, typeDescription, classLoader, module, pd) -> builder
                    .visit(
                        MemberSubstitution
                            .relaxed()
                            .method(ElementMatchers.named("random"))
                            .replaceWith(
                                Assertions
                                    .assertDoesNotThrow(
                                        () -> MathRandomTester.class.getDeclaredMethod("random"),
                                        "Could not find MathRandomTester.random() method"
                                    )
                            )
                            .on(ElementMatchers.any())
                    ))
            .installOnByteBuddyAgent();
        enemy = spy(new Enemy(0, 0, 0, 0, mock(GameState.class)));
    }

    @Test
    public void testUpdateShootCalledWithMaxProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        enemy.update(0);
        verify(enemy, times(1)).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithDelay() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(1);
        ApplicationSettings.enemyShootingDelayProperty().set(1000);
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
        enemy.update(1000);
        verify(enemy, times(1)).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithDelayAndMinProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0);
        ApplicationSettings.enemyShootingDelayProperty().set(1000);
        enemy.update(0);
        verify(enemy, never()).shoot(Direction.DOWN);
        enemy.update(1000);
        verify(enemy, never()).shoot(Direction.DOWN);
    }

    @Test
    public void testUpdateWithFiftyPercentProbability() {
        ApplicationSettings.enemyShootingProbabilityProperty().set(0.5);
        ApplicationSettings.enemyShootingDelayProperty().set(0);
        for (double i = 0; i <= 1; i+=0.1) {
            MathRandomTester.withRandom(i, () -> enemy.update(0));
        }
        verify(enemy, times(5)).shoot(Direction.DOWN);
    }
}
