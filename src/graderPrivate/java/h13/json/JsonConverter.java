package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import h13.model.gameplay.Direction;
import h13.model.gameplay.sprites.Enemy;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class JsonConverter {

    public static Bounds toBounds(final JsonNode jsonNode) {
        return new BoundingBox(
            jsonNode.get("x").asDouble(),
            jsonNode.get("y").asDouble(),
            jsonNode.get("width").asDouble(),
            jsonNode.get("height").asDouble()
        );
    }

    public static Direction toDirection(final JsonNode jsonNode) {
        return Direction.valueOf(jsonNode.asText());
    }

    public static <T> List<T> toList(final JsonNode jsonNode, final Function<JsonNode, T> mapper) {
        return StreamSupport.stream(jsonNode.spliterator(), false)
            .map(mapper)
            .toList();
    }

    public static Enemy toEnemy(final JsonNode jsonNode) {
        return JsonEnemy.fromJsonNode(jsonNode).deserialize();
    }

    public static List<Enemy> toEnemyList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toEnemy);
    }

    // Custom converters
    public final static Map<String, Function<JsonNode, ?>> DEFAULT_CONVERTERS = new HashMap<>() {
        {
            put("GAME_BOUNDS", JsonConverter::toBounds);
            put("enemyBounds", JsonConverter::toBounds);
            put("gameSceneBounds", JsonConverter::toBounds);
            put("expectedGameBoardBounds", JsonConverter::toBounds);
            put("newEnemyBounds", JsonConverter::toBounds);
            put("clampedEnemyBounds", JsonConverter::toBounds);
            put("direction", JsonConverter::toDirection);
            put("oldDirection", JsonConverter::toDirection);
            put("newDirection", JsonConverter::toDirection);
            put("nextMovementDirection", JsonConverter::toDirection);
            put("enemies", JsonConverter::toEnemyList);
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> T convert(
        final JsonNode node,
        final String key,
        final Class<T> type,
        final ObjectMapper objectMapper,
        final Map<String, Function<JsonNode, ?>> converters
    ) {
        if (node == null) {
            return null;
        }
        if (key != null && key.length() > 0) {
            final var converter = converters.get(key);
            if (converter != null && (type == null || type.isAssignableFrom(converter.apply(node).getClass()))) {
                return (T) converter.apply(node);
            }
        }
        return Assertions
            .assertDoesNotThrow(
                () ->
                    objectMapper.treeToValue(
                        node,
                        Objects.requireNonNullElseGet(type, () -> (Class<T>) Object.class)
                    ),
                "Invalid JSON Source."
            );
    }

    public <T> T convert(final JsonNode node, final String key, final Class<T> type, final ObjectMapper objectMapper) {
        return convert(node, key, type, objectMapper, DEFAULT_CONVERTERS);
    }
}
