package h13.shared;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.GameConstants;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.Direction;
import h13.util.StudentLinks;
import h13.json.JsonConverter;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class UtilsTest {

    public final static Map<String, Function<JsonNode, ?>> customConverters = new HashMap<>() {
        {
            put("world", JsonConverter::toBounds);
            put("sprite", JsonConverter::toBounds);
            put("expected", JsonConverter::toBounds);
            put("direction", JsonConverter::toDirection);
        }
    };

    @ParameterizedTest
    @JsonParameterSetTest(value = "UtilsTestClamp.json", customConverters = "customConverters")
    public void clamp(JsonParameterSet params) {
        Bounds worldBounds = params.get("world", Bounds.class);
        Bounds spriteBounds = params.get("sprite", Bounds.class);
        Bounds expectedBounds = params.get("expected", Bounds.class);

        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD.setStatic(worldBounds);

        Context context = contextBuilder()
            .add("World Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("Sprite Bounds", spriteBounds)
            .add("Expected Bounds", spriteBounds)
            .build();


        Bounds clampedBounds = Utils.clamp(spriteBounds);

        assertEquals(expectedBounds.getMinX(), clampedBounds.getMinX(), context,
            r -> String.format("Sprite wrongly clamped inside Bounding Box. Expected x: %f. But got %f", expectedBounds.getMinX(), clampedBounds.getMinX())
        );
        assertEquals(expectedBounds.getMinY(), clampedBounds.getMinY(), context,
            r -> String.format("Sprite wrongly clamped inside Bounding Box. Expected y: %f. But got %f", expectedBounds.getMinY(), clampedBounds.getMinY())
        );
        assertEquals(spriteBounds.getWidth(), clampedBounds.getWidth(), context,
            r -> String.format("Sprite should not change size when clamping. Expected width: %f. But got %f", expectedBounds.getWidth(), clampedBounds.getWidth())
        );
        assertEquals(spriteBounds.getHeight(), clampedBounds.getHeight(), context,
            r -> String.format("Sprite should not change size when clamping. Expected height: %f. But got %f", expectedBounds.getHeight(), clampedBounds.getHeight())
        );
    }

    @CartesianTest
    @CartesianTest.MethodFactory("provideClamp")
    public void clamp_generator(Bounds world, Bounds sprite, Direction direction, int distance){
        GameConstants.ORIGINAL_GAME_BOUNDS = world;

        Bounds spriteBounds = move(sprite, direction.getX() * distance, direction.getY() * distance);

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

    @ParameterizedTest
    @JsonParameterSetTest(value = "UtilsTestGetNextPosition.json", customConverters = "customConverters")
    public void getNextPosition(JsonParameterSet params) {
        Bounds startingPos = params.get("sprite", Bounds.class);
        Bounds expected = params.get("expected", Bounds.class);
        double velocity = params.getDouble("velocity");
        double elapsedTime = params.getDouble("elapsedTime");
        Direction direction = params.get("direction", Direction.class);

        Bounds actual = Utils.getNextPosition(startingPos, velocity, direction, elapsedTime);

        Context context = contextBuilder()
            .add("Starting Position", startingPos)
            .add("Velocity", velocity)
            .add("Direction", direction)
            .add("elapsed Time", elapsedTime)
            .build();

        assertEquals(expected, actual, context, r -> String.format("Next Position does not match expected! Expected %s but was %s", expected, actual));
    }

    /**
     * This Method generates a List of Parameters to test clamping of Sprites
     * @return the created ArgumentSet containing all Parameters for testing.
     */
    public static ArgumentSets provideClamp(){
        List<Bounds> worldBox = new ArrayList<>();


        worldBox.add(new BoundingBox(0, 0, 256, 224));
        worldBox.add(new BoundingBox(0, 0, 300, 250));
        worldBox.add(new BoundingBox(0, 0, 50, 50));

        List<Bounds> spriteBox = new ArrayList<>();
        spriteBox.add(new BoundingBox(0,0, 5, 5));
        spriteBox.add(new BoundingBox(0,0, 15, 20));
        spriteBox.add(new BoundingBox(0,0, 9, 4));
        spriteBox.add(new BoundingBox(0,0, 49, 49));

        List<Direction> directions = List.of(Direction.values());
        List<Integer> distances = List.of(5, 400, 450, 1234);

        ArgumentSets argumentSets = ArgumentSets.create();
        argumentSets.argumentsForNextParameter(worldBox);
        argumentSets.argumentsForNextParameter(spriteBox);
        argumentSets.argumentsForNextParameter(directions);
        argumentSets.argumentsForNextParameter(distances);
        return argumentSets;
    }

    /**
     * This method returns a BoundingBox that is moved my x and y. The returned Object is a copy
     * @param bounds the BoundingBox
     * @param x the distance to move in x direction
     * @param y the distance to move in y direction
     * @return the resulting BoundingBox
     */
    public static Bounds move(Bounds bounds, double x, double y){
        return new BoundingBox(bounds.getMinX() + x, bounds.getMinY() + y, bounds.getWidth(), bounds.getHeight());
    }
}
