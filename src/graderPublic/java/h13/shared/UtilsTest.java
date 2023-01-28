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

    @SuppressWarnings("unused")
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
    public void clamp(final JsonParameterSet params) {
        final Bounds worldBounds = params.get("world", Bounds.class);
        final Bounds spriteBounds = params.get("sprite", Bounds.class);
        final Bounds expectedBounds = params.get("expected", Bounds.class);

        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD.setStatic(worldBounds);

        final Context context = contextBuilder()
            .add("World Bounds", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("Sprite Bounds", spriteBounds)
            .add("Expected Bounds", spriteBounds)
            .build();


        final Bounds clampedBounds = Utils.clamp(spriteBounds);

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

    @ParameterizedTest
    @JsonParameterSetTest(value = "UtilsTestGetNextPosition.json", customConverters = "customConverters")
    public void getNextPosition(final JsonParameterSet params) {
        final Bounds startingPos = params.get("sprite", Bounds.class);
        final Bounds expected = params.get("expected", Bounds.class);
        final double velocity = params.getDouble("velocity");
        final double elapsedTime = params.getDouble("elapsedTime");
        final Direction direction = params.get("direction", Direction.class);

        final Bounds actual = Utils.getNextPosition(startingPos, velocity, direction, elapsedTime);

        final Context context = contextBuilder()
            .add("Starting Position", startingPos)
            .add("Velocity", velocity)
            .add("Direction", direction)
            .add("elapsed Time", elapsedTime)
            .build();

        assertEquals(expected, actual, context, r -> String.format("Next Position does not match expected! Expected %s but was %s", expected, actual));
    }

    /**
     * This method returns a BoundingBox that is moved my x and y. The returned Object is a copy
     * @param bounds the BoundingBox
     * @param x the distance to move in x direction
     * @param y the distance to move in y direction
     * @return the resulting BoundingBox
     */
    public static Bounds move(final Bounds bounds, final double x, final double y){
        return new BoundingBox(bounds.getMinX() + x, bounds.getMinY() + y, bounds.getWidth(), bounds.getHeight());
    }
}
