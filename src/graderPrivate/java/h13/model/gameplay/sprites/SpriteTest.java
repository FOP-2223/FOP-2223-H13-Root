package h13.model.gameplay.sprites;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class SpriteTest {

    @Nested
    public class IsDead{

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 763, Integer.MAX_VALUE})
        void isDead_alive(int health) {
            Sprite s = createSprite(health);
            Context context = contextBuilder()
                .add("Sprite Health", health)
                .build();

            assertTrue(s.isAlive(), context, r -> String.format("Sprite is wrongly marked as dead with %d health", health));
        }

        @ParameterizedTest
        @ValueSource(ints = {0})
        void isDead_dead(int health) {
            Sprite s = createSprite(health);
            Context context = contextBuilder()
                .add("Sprite Health", health)
                .build();

            assertTrue(s.isDead(), context, r -> String.format("Sprite is wrongly marked as not dead with %d health", health));
        }
    }

    @ParameterizedTest
    @CsvSource({"0,1", "1,0", "-5,1", "5,10", "100,5", "100,0", "3,1", "2, 1000000"})
    void damage(int health, int damage) {
        Sprite s = createSprite(health);
        Context context = contextBuilder()
            .add("Sprite Health", health)
            .add("Applied Damage", health)
            .build();

        s.damage(damage);

        int expected = health - damage;
        int actual = s.getHealth();
        assertEquals(expected, actual, context, r -> String.format("The Sprite should have had %d health but actually had %d health", expected, actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5, 0, 3, 10, Integer.MAX_VALUE})
    void die(int health) {
        Sprite s = createSprite(health);
        Context context = contextBuilder()
            .add("Sprite Health", health)
            .build();

        s.die();

        assertEquals(0, s.getHealth(), context, r -> String.format("The Sprite should have had 0 health but actually had %d health", health));
    }

    @Test
    void update() {

    }

    private static Sprite createSprite(int health){
        Sprite s = mock(Sprite.class, Mockito.CALLS_REAL_METHODS);
        s.setHealth(health);
        return s;
    }
}
