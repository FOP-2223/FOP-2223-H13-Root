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

import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD;
import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class SpriteTest {

    @Nested
    public class IsDead {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 763, Integer.MAX_VALUE})
        void isDead_alive(final int health) {
            final Sprite s = createSprite(health);
            final Context context = contextBuilder()
                .add("Sprite Health", health)
                .build();

            assertTrue(IS_ALIVE_METHOD.invoke(context, s), context, r -> String.format("Sprite is wrongly marked as dead with %d health", health));
        }

        @ParameterizedTest
        @ValueSource(ints = 0)
        void isDead_dead(final int health) {
            final Sprite s = createSprite(health);
            final Context context = contextBuilder()
                .add("Sprite Health", health)
                .build();

            assertTrue(IS_DEAD_METHOD.invoke(context, s), context, r -> String.format("Sprite is wrongly marked as not dead with %d health", health));
        }
    }

    @ParameterizedTest
    @CsvSource({"0,1", "1,0", "-5,1", "5,10", "100,5", "100,0", "3,1", "2, 1000000"})
    public void damage(final int health, final int damage) {
        final Sprite s = createSprite(health);
        final Context context = contextBuilder()
            .add("Sprite Health", health)
            .add("Applied Damage", damage)
            .build();

        DAMAGE_METHOD_WITH_AMOUNT.invoke(context, s, damage);

        final int expected = health - damage;
        final int actual = s.getHealth();
        assertEquals(expected, actual, context, r -> String.format("The Sprite should have had %d health but actually had %d health", expected, actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5, 0, 3, 10, Integer.MAX_VALUE})
    public void die(final int health) {
        final Sprite s = createSprite(health);
        final Context context = contextBuilder()
            .add("Sprite Health", health)
            .build();

        DIE_METHOD.invoke(context, s);

        assertEquals(0, s.getHealth(), context, r -> String.format("The Sprite should have had 0 health but actually had %d health", health));
    }

    @Test
    public void update_inside() {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(0, 0, 100, 100));
        final Bounds destination = new BoundingBox(50, 50, 1, 1);

        final Context context = contextBuilder()
            .add("Game Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("Next Position", destination)
            .build();

        try (final var utilsMock = mockStatic(Utils.class)) {
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

            final Sprite sprite = createSprite(1);
            UPDATE_METHOD.invoke(context, sprite, 0);

            utilsMock.verify(() -> Utils.getNextPosition(
                any(Bounds.class),
                anyDouble(),
                any(),
                anyDouble()
            ), atLeastOnce());

            final ArgumentCaptor<Double> argumentSetX = ArgumentCaptor.forClass(Double.class);
            verify(sprite).setX(argumentSetX.capture());

            final ArgumentCaptor<Double> argumentSetY = ArgumentCaptor.forClass(Double.class);
            verify(sprite).setY(argumentSetY.capture());

            assertTrue(argumentSetX.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, true)), context, r -> String.format("SetX was called with out of bounds coordinates. Called Values: %s", argumentSetX.getAllValues()));
            assertTrue(argumentSetY.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, false)), context, r -> String.format("SetY was called with out of bounds coordinates. Called Values: %s", argumentSetY.getAllValues()));

            assertEquals(destination.getMinX(), sprite.getX(), context, r -> "Sprite was not clamped to the correct X-Coordinate");
            assertEquals(destination.getMinY(), sprite.getY(), context, r -> "Sprite was not clamped to the correct Y-Coordinate");
        }
    }

    @Test
    public void update_outside() {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(0, 0, 100, 100));
        final Bounds destination = new BoundingBox(500, 500, 1, 1);
        final Bounds clampedDestination = new BoundingBox(50, 50, 1, 1);

        final Context context = contextBuilder()
            .add("Game Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("Next Position", destination)
            .build();

        try (final var utilsMock = mockStatic(Utils.class)) {
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

            final Sprite sprite = createSprite(1);
            UPDATE_METHOD.invoke(context, sprite, 0);

            utilsMock.verify(() -> Utils.getNextPosition(
                any(Bounds.class),
                anyDouble(),
                any(),
                anyDouble()
            ), atLeastOnce());

            utilsMock.verify(() -> Utils.clamp(
                any(Bounds.class)
            ), atLeastOnce());

            final ArgumentCaptor<Double> argumentSetX = ArgumentCaptor.forClass(Double.class);
            verify(sprite, atLeast(0)).setX(argumentSetX.capture());

            final ArgumentCaptor<Double> argumentSetY = ArgumentCaptor.forClass(Double.class);
            verify(sprite, atLeast(0)).setY(argumentSetY.capture());

            assertTrue(argumentSetX.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, true)), context, r -> String.format("SetX was called with out of bounds coordinates. Called Values: %s", argumentSetX.getAllValues()));
            assertTrue(argumentSetY.getAllValues().stream().noneMatch(d -> isOutOfBounds(sprite, d, false)), context, r -> String.format("SetY was called with out of bounds coordinates. Called Values: %s", argumentSetY.getAllValues()));

            assertEquals(clampedDestination.getMinX(), sprite.getX(), context, r -> "Sprite was not clamped to the correct X-Coordinate");
            assertEquals(clampedDestination.getMinY(), sprite.getY(), context, r -> "Sprite was not clamped to the correct Y-Coordinate");
        }
    }

    /**
     * Checks whether a coordinate is out of bounds or not
     *
     * @param sprite     the sprite to check the coordinate for
     * @param coordinate the coordinate the sprite is placed at
     * @param isX        if the coordinate is the x coordinate of the sprite. false otherwise
     * @return true if the sprite is out of bounds
     */
    private static boolean isOutOfBounds(final Sprite sprite, final double coordinate, final boolean isX) {
        if (coordinate < 0) {
            return true;
        }
        if (isX) {
            return coordinate > GameConstants.ORIGINAL_GAME_BOUNDS.getWidth() - sprite.getWidth();
        } else {
            return coordinate > GameConstants.ORIGINAL_GAME_BOUNDS.getHeight() - sprite.getHeight();
        }
    }

    /**
     * Creates a Sprite with the given heath
     *
     * @param health the health the Sprite should have after creation
     * @return the newly created sprite
     */
    public static Sprite createSprite(final int health) {
        final Sprite s = mock(Sprite.class, Mockito.CALLS_REAL_METHODS);
        s.setHealth(health);
        return s;
    }
}
