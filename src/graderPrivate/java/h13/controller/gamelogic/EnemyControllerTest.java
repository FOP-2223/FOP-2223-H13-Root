package h13.controller.gamelogic;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.scene.game.GameController;
import h13.controller.scene.game.GameControllerTest;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.IDEnemy;
import h13.model.gameplay.sprites.WithID;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.IS_DEAD_METHOD;

@TestForSubmission
public class EnemyControllerTest {
    private EnemyController enemyController;
    private GameState gameState;
    private GameController gameController;

    @SuppressWarnings("unused")
    public final static Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("GAME_BOUNDS", JsonConverter::toBounds),
        Map.entry("enemies", JsonConverter::toIDEnemyList),
        Map.entry("bullets", JsonConverter::toIDBulletList),
        Map.entry("player", JsonConverter::toIDPlayer),
        Map.entry("bulletOwners", JsonConverter::toIntMap),
        Map.entry("hits", JsonConverter::toIntMap),
        Map.entry("dead", jsonNode -> JsonConverter.toList(jsonNode, JsonNode::asInt)),
        Map.entry("expectedIsDefeated", JsonNode::asBoolean)
    );

    @ParameterizedTest
    @JsonParameterSetTest(value = "EnemyControllerTestIsDefeated.json", customConverters = "customConverters")
    public void testIsDefeated(final JsonParameterSet params) {

        // preparation
        gameController = GameControllerTest.setupGameController(params);
        gameState = gameController.getGameState();
        enemyController = gameController.getEnemyController();
        final var context = params.toContext();

        // mocking
        final List<Integer> dead = params.get("dead");
        gameState.getSprites().forEach(sprite -> {
            IS_DEAD_METHOD.doReturnAlways(
                context,
                sprite,
                sprite instanceof WithID sid && dead.contains(sid.getId())
            );
        });

        // execution
        Assertions2.assertEquals(
            params.getBoolean("expectedIsDefeated"),
            enemyController.isDefeated(),
            context,
            r -> "Incorrect result for isDefeated()."
        );
    }
}
