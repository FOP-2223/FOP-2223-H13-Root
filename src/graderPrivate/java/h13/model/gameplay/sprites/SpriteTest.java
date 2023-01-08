package h13.model.gameplay.sprites;

import h13.controller.GameConstants;
import h13.shared.Utils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
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
    void update_inside() {
        GameConstants.ORIGINAL_GAME_BOUNDS = new BoundingBox(0, 0, 100, 100);
        Bounds destination = new BoundingBox(50, 50, 1, 1);

        Context context = contextBuilder()
            .add("Game Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("Next Position", destination)
            .build();

        try (var utilsMock = mockStatic(Utils.class)){
            utilsMock.when(() -> Utils.getNextPosition(
                    any(Bounds.class),
                    anyDouble(),
                    any(),
                    anyDouble()
                ))
                .thenReturn(destination);
            utilsMock.when(() -> Utils.clamp(
                    any(Bounds.class)
                ))
                .thenReturn(destination);

            Sprite sprite = createSprite(1);
            sprite.update(0);

            utilsMock.verify(() -> Utils.getNextPosition(
                any(Bounds.class),
                anyDouble(),
                any(),
                anyDouble()
            ), atLeastOnce());

            ArgumentCaptor<Double> argumentSetX = ArgumentCaptor.forClass(Double.class);
            verify(sprite).setX(argumentSetX.capture());

            ArgumentCaptor<Double> argumentSetY = ArgumentCaptor.forClass(Double.class);
            verify(sprite).setX(argumentSetY.capture());

            assertTrue(argumentSetX.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, true)), context, r -> String.format("SetX was called with out of bounds coordinates. Called Values: %s", argumentSetX.getAllValues()));
            assertTrue(argumentSetY.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, false)), context, r -> String.format("SetY was called with out of bounds coordinates. Called Values: %s", argumentSetY.getAllValues()));

            assertEquals(destination.getMinX(), sprite.getX(), context, r-> "Sprite was not clamped to the correct X-Coordinate");
            assertEquals(destination.getMinY(), sprite.getY(), context, r-> "Sprite was not clamped to the correct Y-Coordinate");
        }
    }

    @Test
    void update_outside() {
        GameConstants.ORIGINAL_GAME_BOUNDS = new BoundingBox(0, 0, 100, 100);
        Bounds destination = new BoundingBox(500, 500, 1, 1);
        Bounds clampedDestination = new BoundingBox(50, 50, 1, 1);

        Context context = contextBuilder()
            .add("Game Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("Next Position", destination)
            .build();

        try (var utilsMock = mockStatic(Utils.class)){
            utilsMock.when(() -> Utils.getNextPosition(
                    any(Bounds.class),
                    anyDouble(),
                    any(),
                    anyDouble()
                ))
                .thenReturn(destination);

            utilsMock.when(() -> Utils.clamp(
                    any(Bounds.class)
                ))
                .thenReturn(new BoundingBox(50, 50, 1, 1));

            Sprite sprite = createSprite(1);
            sprite.update(0);

            utilsMock.verify(() -> Utils.getNextPosition(
                any(Bounds.class),
                anyDouble(),
                any(),
                anyDouble()
            ), atLeastOnce());

            utilsMock.verify(() -> Utils.clamp(
                any(Bounds.class)
            ), atLeastOnce());

            ArgumentCaptor<Double> argumentSetX = ArgumentCaptor.forClass(Double.class);
            verify(sprite).setX(argumentSetX.capture());

            ArgumentCaptor<Double> argumentSetY = ArgumentCaptor.forClass(Double.class);
            verify(sprite).setX(argumentSetY.capture());

            assertTrue(argumentSetX.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, true)), context, r -> String.format("SetX was called with out of bounds coordinates. Called Values: %s", argumentSetX.getAllValues()));
            assertTrue(argumentSetY.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, false)), context, r -> String.format("SetY was called with out of bounds coordinates. Called Values: %s", argumentSetY.getAllValues()));

            assertEquals(clampedDestination.getMinX(), sprite.getX(), context, r-> "Sprite was not clamped to the correct X-Coordinate");
            assertEquals(clampedDestination.getMinY(), sprite.getY(), context, r-> "Sprite was not clamped to the correct Y-Coordinate");
        }
    }

    /**
     * Checks whether a coordinate is out of bounds or not
     * @param sprite the sprite to check the coordinate for
     * @param coordinate the coordinate the sprite is placed at
     * @param isX if the coordinate is the x coordinate of the sprite. false otherwise
     * @return true if the sprite is out of bounds
     */
    private static boolean isOutOfBounds(Sprite sprite, double coordinate, boolean isX){
        if (coordinate < 0){
            return true;
        }
        if (isX){
            return coordinate > GameConstants.ORIGINAL_GAME_BOUNDS.getWidth() - sprite.getWidth();
        } else {
            return coordinate > GameConstants.ORIGINAL_GAME_BOUNDS.getHeight() - sprite.getHeight();
        }
    }

    /**
     * Creates a Sprite with the given heath
     * @param health the health the Sprite should have after creation
     * @return the newly created sprite
     */
    private static Sprite createSprite(int health){
        Sprite s = mock(Sprite.class, Mockito.CALLS_REAL_METHODS);
        s.setHealth(health);
        return s;
    }
}
