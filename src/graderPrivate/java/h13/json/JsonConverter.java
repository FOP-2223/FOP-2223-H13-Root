package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import h13.util.StudentLinks;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.mockito.MockSettings;
import org.mockito.junit.jupiter.MockitoSettings;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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

    private static Sprite toSprite(JsonNode jsonNode) {
        int x = jsonNode.get("x").asInt();
        int y = jsonNode.get("y").asInt();
        String color = jsonNode.get("color").asText();

        var sprite = spy(switch (jsonNode.get("type").asText()){
            case "bullet" -> new Bullet(x, y, mock(GameState.class), null, Direction.UP);
            case "enemy" -> new Enemy(x,y, 0, 0, mock(GameState.class));
            default -> new Player(x, y, 0, mock(GameState.class));
        });

        if (!color.equals("null")){
            sprite.setTexture(null);
            StudentLinks.SpriteLinks.SpriteFieldLink.COLOR_FIELD.set(sprite, Color.valueOf(color));
        }
        return sprite;
    }

    public static Enemy toEnemy(final JsonNode jsonNode) {
        return JsonEnemy.fromJsonNode(jsonNode).deserialize();
    }

    public static IDEnemy toIDEnemy(final JsonNode jsonNode, final GameState gameState) {
        final var enemy = new IDEnemy(
            jsonNode.get("id").asInt(),
            jsonNode.get("xIndex").asInt(),
            jsonNode.get("yIndex").asInt(),
            jsonNode.get("velocity").asInt(),
            jsonNode.get("health").asInt(),
            gameState
        );
        enemy.setX(jsonNode.get("x").asInt());
        enemy.setY(jsonNode.get("y").asInt());
        return enemy;
    }

    public static IDBullet toIDBullet(final JsonNode jsonNode) {
        return new IDBullet(
            jsonNode.get("id").asInt(0),
            jsonNode.get("x").asInt(0),
            jsonNode.get("y").asInt(0),
            null,
            null,
            JsonConverter.toDirection(jsonNode.get("direction"))
        );
    }

    public static IDPlayer toIDPlayer(final JsonNode jsonNode) {
        return new IDPlayer(
            jsonNode.get("id").asInt(0),
            jsonNode.get("x").asInt(0),
            jsonNode.get("y").asInt(0),
            jsonNode.get("velocity").asDouble(0),
            null
        );
    }

    public static List<Enemy> toEnemyList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toEnemy);
    }

    public static List<Sprite> toSpriteList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toSprite);
    }

    public static List<IDEnemy> toIDEnemyList(final JsonNode jsonNode, final GameState gameState) {
        return toList(jsonNode, node -> JsonConverter.toIDEnemy(node, gameState));
    }

    public static List<IDEnemy> toIDEnemyList(final JsonNode jsonNode) {
        return toIDEnemyList(jsonNode, null);
    }

    public static List<IDBullet> toIDBulletList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toIDBullet);
    }

    public static Map<Integer, Integer> toIntMap(final JsonNode jsonNode) {
        final var map = new HashMap<Integer, Integer>();
        jsonNode.fields().forEachRemaining(entry -> map.put(Integer.parseInt(entry.getKey()), entry.getValue().asInt()));
        return map;
    }

    // Custom converters
    public final static Map<String, Function<JsonNode, ?>> DEFAULT_CONVERTERS = new HashMap<>() {
        {
            put("GAME_BOUNDS", JsonConverter::toBounds);
            put("enemyBounds", JsonConverter::toBounds);
            put("gameSceneBounds", JsonConverter::toBounds);
            put("gameSceneBounds2", JsonConverter::toBounds);
            put("expectedGameBoardBounds", JsonConverter::toBounds);
            put("expectedGameBoardBounds2", JsonConverter::toBounds);
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
