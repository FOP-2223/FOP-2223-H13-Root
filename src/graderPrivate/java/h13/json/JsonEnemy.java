package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.Enemy;

public record JsonEnemy(int xIndex, int yIndex, int x, int y, int velocity, int health) implements JsonDataClass<Enemy> {
    public static GameState gameState;

    @Override
    public Enemy deserialize() {
        final var enemy = new Enemy(xIndex, yIndex, velocity, health, gameState);
        enemy.setX(x);
        enemy.setY(y);
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
