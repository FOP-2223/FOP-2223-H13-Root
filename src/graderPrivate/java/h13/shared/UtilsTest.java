package h13.shared;

import h13.controller.GameConstants;
import h13.model.gameplay.Direction;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicFieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;

import java.util.ArrayList;
import java.util.List;

import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;


class UtilsTest {

    record Box(double x, double y, double width, double height){}

        @Test
        void clamp(Box world, Box sprite, Box expected) {
            GameConstants.ORIGINAL_GAME_BOUNDS = boundsFromBox(world);

            Bounds spriteBounds = boundsFromBox(sprite);

            Context context = contextBuilder()
                .add("World Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
                .add("Sprite Bounds", spriteBounds)
                .add("Expected Bounds", spriteBounds)
                .build();


            Bounds clampedBounds = Utils.clamp(spriteBounds);

            assertEquals(expected.x, clampedBounds.getMinX(), context,
                r -> String.format("Sprite wrongly clamped inside Bounding Box. Expected x: %f. But got %f", expected.x, clampedBounds.getMinX())
            );
            assertEquals(expected.y, clampedBounds.getMinY(), context,
                r -> String.format("Sprite wrongly clamped inside Bounding Box. Expected y: %f. But got %f", expected.y, clampedBounds.getMinY())
            );
            assertEquals(sprite.width, clampedBounds.getWidth(), context,
                r -> String.format("Sprite should not change size when clamping. Expected width: %f. But got %f", expected.width(), clampedBounds.getWidth())
            );
            assertEquals(sprite.height, clampedBounds.getHeight(), context,
                r -> String.format("Sprite should not change size when clamping. Expected height: %f. But got %f", expected.height(), clampedBounds.getHeight())
            );
        }

        @CartesianTest
        @CartesianTest.MethodFactory("provideClamp")
        void clamp_generator(Box world, Box sprite, Direction direction, int distance){
            GameConstants.ORIGINAL_GAME_BOUNDS = boundsFromBox(world);

            Bounds spriteBounds = move(boundsFromBox(sprite), direction.getX() * distance, direction.getY() * distance);

            Context context = contextBuilder()
                .add("World Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
                .add("Sprite Bounds", spriteBounds)
                .add("Direction", direction)
                .add("Distance", distance)
                .build();

            Bounds clampedBounds = Utils.clamp(spriteBounds);
            Bounds expected = new BoundingBox(
                Math.max(0, Math.min(ORIGINAL_GAME_BOUNDS.getWidth() - spriteBounds.getWidth(), spriteBounds.getMinX())),
                Math.max(0, Math.min(ORIGINAL_GAME_BOUNDS.getHeight() - spriteBounds.getHeight(), spriteBounds.getMinY())),
                spriteBounds.getWidth(),
                spriteBounds.getHeight()
            );

            assertEquals(expected.getMinX(), clampedBounds.getMinX(), context,
                r -> String.format("Sprite wrongly clamped inside Bounding Box. Expected x: %f. But got %f", expected.getMinX(), clampedBounds.getMinX())
            );
            assertEquals(expected.getMinY(), clampedBounds.getMinY(), context,
                r -> String.format("Sprite wrongly clamped inside Bounding Box. Expected y: %f. But got %f", expected.getMinY(), clampedBounds.getMinY())
            );
            assertEquals(expected.getWidth(), clampedBounds.getWidth(), context,
                r -> String.format("Sprite should not change size when clamping. Expected width: %f. But got %f", expected.getWidth(), clampedBounds.getWidth())
            );
            assertEquals(expected.getHeight(), clampedBounds.getHeight(), context,
                r -> String.format("Sprite should not change size when clamping. Expected height: %f. But got %f", expected.getHeight(), clampedBounds.getHeight())
            );
        }

    @Test
    void getNextPosition(Box bounds, double velocity, Direction direction, double elapsedTime, Box expectedPosition) {
        Bounds startingPos = boundsFromBox(bounds);
        Bounds actual = Utils.getNextPosition(startingPos, velocity, direction, elapsedTime);
        Bounds expected = boundsFromBox(expectedPosition);

        Context context = contextBuilder()
            .add("Starting Position", startingPos)
            .add("Velocity", velocity)
            .add("Direction", direction)
            .add("elapsed Time", elapsedTime)
            .build();

        assertEquals(boundsFromBox(bounds), actual, context, r -> String.format("Next Position does not match expected! Expected %s but was %s", expected, actual));
    }

    public static ArgumentSets provideClamp(){
        List<Box> worldBox = new ArrayList<>();


        worldBox.add(new Box(0, 0, 256, 224));
        worldBox.add(new Box(0, 0, 300, 250));
        worldBox.add(new Box(0, 0, 50, 50));

        List<Box> spriteBox = new ArrayList<>();
        spriteBox.add(new Box(0,0, 5, 5));
        spriteBox.add(new Box(0,0, 15, 20));
        spriteBox.add(new Box(0,0, 9, 4));
        spriteBox.add(new Box(0,0, 49, 49));

        List<Direction> directions = List.of(Direction.values());
        List<Integer> distances = List.of(5,400, 450, 1234);

        ArgumentSets argumentSets = ArgumentSets.create();
        argumentSets.argumentsForNextParameter(worldBox);
        argumentSets.argumentsForNextParameter(spriteBox);
        argumentSets.argumentsForNextParameter(directions);
        argumentSets.argumentsForNextParameter(distances);
        return argumentSets;
    }

    public static Bounds boundsFromBox(Box box){
        return new BoundingBox(box.x, box.y, box.width, box.height);
    }

    public static Bounds move(Bounds bounds, double x, double y){
        return new BoundingBox(bounds.getMinX() + x, bounds.getMinY() + y, bounds.getWidth(), bounds.getHeight());
    }
}
