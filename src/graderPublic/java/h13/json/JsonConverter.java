package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static org.mockito.Mockito.*;

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

    public static Sprite toSprite(final JsonNode jsonNode) {
        final int x = jsonNode.get("x").asInt();
        final int y = jsonNode.get("y").asInt();
        final String texture = jsonNode.get("texture").asText();

        final var sprite = spy(switch (jsonNode.get("type").asText()){
            case "bullet" -> new Bullet(x, y, mock(GameState.class), null, Direction.UP);
            case "enemy" -> new EnemyC(x,y, 0, 0, mock(GameState.class));
            default -> new Player(x, y, 0, mock(GameState.class));
        });

        if (!texture.equals("null")){
            final Image image = new Image(Objects.requireNonNull(JsonConverter.class.getResourceAsStream(texture)));
            when(sprite.getTexture()).thenReturn(image);
        } else {
            when(sprite.getTexture()).thenReturn(null);
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
            jsonNode.has("pointsWorth") ? jsonNode.get("pointsWorth").asInt() : 0,
            gameState
        );
        enemy.setX(jsonNode.get("x").asInt());
        enemy.setY(jsonNode.get("y").asInt());
        if (jsonNode.has("health")) {
            enemy.setHealth(jsonNode.get("health").asInt());
        }
        return enemy;
    }

    public static IDBullet toIDBullet(final JsonNode jsonNode) {
        final var b = new IDBullet(
            jsonNode.get("id").asInt(0),
            jsonNode.get("x").asInt(0),
            jsonNode.get("y").asInt(0),
            null,
            null,
            toDirection(jsonNode.get("direction"))
        );
        if (jsonNode.has("health")) {
            b.setHealth(jsonNode.get("health").asInt(0));
        }
        return b;
    }

    public static IDPlayer toIDPlayer(final JsonNode jsonNode) {
        final var p = new IDPlayer(
            jsonNode.get("id").asInt(0),
            jsonNode.get("x").asInt(0),
            jsonNode.get("y").asInt(0),
            jsonNode.get("velocity").asDouble(0),
            null
        );
        if (jsonNode.has("health")) {
            p.setHealth(jsonNode.get("health").asInt(0));
        }
        if (jsonNode.has("score")) {
            p.setScore(jsonNode.get("score").asInt(0));
        }
        return p;
    }

    public static KeyCode toKeyCode(final JsonNode jsonNode) {
        return KeyCode.valueOf(jsonNode.asText());
    }

    public static List<Enemy> toEnemyList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toEnemy);
    }

    public static List<Sprite> toSpriteList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toSprite);
    }

    public static List<IDEnemy> toIDEnemyList(final JsonNode jsonNode, final GameState gameState) {
        return toList(jsonNode, node -> toIDEnemy(node, gameState));
    }

    public static List<IDEnemy> toIDEnemyList(final JsonNode jsonNode) {
        return toIDEnemyList(jsonNode, null);
    }

    public static List<IDBullet> toIDBulletList(final JsonNode jsonNode) {
        return toList(jsonNode, JsonConverter::toIDBullet);
    }

    public static <K, V> Map<K, V> toMap(final JsonNode jsonNode, final Function<String, K> keyMapper, final Function<JsonNode, V> valueMapper) {
        final var map = new HashMap<K, V>();
        jsonNode.fields().forEachRemaining(entry -> map.put(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue())));
        return map;
    }

    public static Map<Integer, Integer> toIntMap(final JsonNode jsonNode) {
        return toMap(jsonNode, Integer::parseInt, JsonNode::asInt);
    }

    // Custom converters
    public final static Map<String, Function<JsonNode, ?>> DEFAULT_CONVERTERS = Map.ofEntries(
            Map.entry("GAME_BOUNDS", JsonConverter::toBounds),
            Map.entry("enemyBounds", JsonConverter::toBounds),
            Map.entry("gameSceneBounds", JsonConverter::toBounds),
            Map.entry("gameSceneBounds2", JsonConverter::toBounds),
            Map.entry("expectedGameBoardBounds", JsonConverter::toBounds),
            Map.entry("expectedGameBoardBounds2", JsonConverter::toBounds),
            Map.entry("newEnemyBounds", JsonConverter::toBounds),
            Map.entry("clampedEnemyBounds", JsonConverter::toBounds),
            Map.entry("direction", JsonConverter::toDirection),
            Map.entry("oldDirection", JsonConverter::toDirection),
            Map.entry("newDirection", JsonConverter::toDirection),
            Map.entry("nextMovementDirection", JsonConverter::toDirection),
            Map.entry("enemies", JsonConverter::toEnemyList)
    );

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
