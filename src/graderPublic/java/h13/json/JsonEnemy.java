package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.Enemy;

import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.IS_DEAD_METHOD;
import static org.mockito.Mockito.spy;

public record JsonEnemy(int xIndex, int yIndex, int x, int y, int velocity, int health) implements JsonDataClass<Enemy> {
    public static GameState gameState;

    @Override
    public Enemy deserialize() {
        final var enemy = spy(new Enemy(xIndex, yIndex, velocity, health, gameState));
        enemy.setX(x);
        enemy.setY(y);

        IS_DEAD_METHOD.doReturnAlways(enemy, health <= 0);
        return enemy;
    }

    /**
     * Creates a JsonEnemy from a JsonNode.
     * @param node The JSON node to deserialize.
     * @return The deserialized JSON enemy.
     */
    public static JsonEnemy fromJsonNode(final JsonNode node) {
        return new JsonEnemy(
                node.get("xIndex").asInt(),
                node.get("yIndex").asInt(),
                node.get("x").asInt(),
                node.get("y").asInt(),
                node.get("velocity").asInt(),
                node.get("health").asInt()
        );
    }
}
