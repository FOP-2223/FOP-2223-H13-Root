package h13.controller.scene.game;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.ApplicationSettings;
import h13.controller.gamelogic.EnemyController;
import h13.controller.gamelogic.PlayerController;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import h13.util.StudentLinks;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static h13.util.StudentLinks.BulletLinks.BulletFieldLink.OWNER_FIELD;
import static h13.util.StudentLinks.BulletLinks.BulletMethodLink.HIT_METHOD;
import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.*;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerFieldLink.GAME_STATE_FIELD;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerMethodLink.DO_COLLISIONS_METHOD;
import static h13.util.StudentLinks.GameControllerLinks.GameControllerMethodLink.UPDATE_POINTS_METHOD;
import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.IS_DEAD_METHOD;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@TestForSubmission
public class GameControllerTest {

    @SuppressWarnings("unused")
    public final static Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("GAME_BOUNDS", JsonConverter::toBounds),
        Map.entry("enemies", JsonConverter::toIDEnemyList),
        Map.entry("bullets", JsonConverter::toIDBulletList),
        Map.entry("player", JsonConverter::toIDPlayer),
        Map.entry("bulletOwners", JsonConverter::toIntMap),
        Map.entry("hits", JsonConverter::toIntMap),
        Map.entry("damaged", jsonNode -> JsonConverter.toMap(jsonNode, Integer::parseInt, JsonNode::asBoolean))
    );

    public static GameController setupGameController(final JsonParameterSet params) {
        // preparation
        final var origGameBounds = new BoundingBox(0, 0, 256, 224);
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(origGameBounds);
        ASPECT_RATIO_FIELD.setStatic(origGameBounds.getWidth() / origGameBounds.getHeight());
        ApplicationSettings.loadTexturesProperty().set(false);
        final var gameBounds = params.get("GAME_BOUNDS", Bounds.class);
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(gameBounds);
        ASPECT_RATIO_FIELD.setStatic(gameBounds.getWidth() / gameBounds.getHeight());
        final var SHIP_SIZE = params.getDouble("SHIP_SIZE");
        SHIP_SIZE_FIELD.setStatic(SHIP_SIZE);

        // setup Model
        final var gameState = new GameState();
        gameState.getSprites().clear();

        final IDPlayer player = spy(params.get("player", IDPlayer.class));
        gameState.getSprites().add(player);

        final List<IDEnemy> enemies = params.<List<IDEnemy>>get("enemies").stream().map(Mockito::spy).toList();
        gameState.getSprites().addAll(enemies);

        final var bullets = params.<List<IDBullet>>get("bullets").stream().map(Mockito::spy).toList();
        final Map<Integer, Integer> bulletOwners = params.get("bulletOwners");
        bulletOwners.forEach((bulletID, ownerID) -> {
            final var bullet = bullets.stream().filter(b -> b.getId() == bulletID).findFirst().orElseThrow(() -> new RuntimeException("Invalid Test: Bullet not found"));
            // find owner
            final var owner = gameState.getSprites().stream()
                .filter(WithID.class::isInstance)
                .map(WithID.class::cast)
                .filter(s -> s.getId() == ownerID)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid Test: Owner not found"));
            OWNER_FIELD.set(bullet, owner);
        });
        gameState.getSprites().addAll(bullets);

        // setup Controller
        final var gameController = spy(mock(GameController.class, Answers.CALLS_REAL_METHODS));
        GAME_STATE_FIELD.set(gameController, gameState);

        final var playerController = spy(mock(PlayerController.class, Answers.CALLS_REAL_METHODS));
        StudentLinks.PlayerControllerLinks.PlayerControllerFieldLink.GAME_CONTROLLER_FIELD.set(playerController, gameController);
        gameController.setPlayerController(playerController);
        StudentLinks.PlayerControllerLinks.PlayerControllerFieldLink.PLAYER_FIELD.set(
            gameController.getPlayerController(),
            player
        );

        final var enemyController = mock(EnemyController.class, Answers.CALLS_REAL_METHODS);
        StudentLinks.EnemyControllerLinks.EnemyControllerFieldLink.GAME_CONTROLLER_FIELD.set(enemyController, gameController);
        gameController.setEnemyController(enemyController);

        return gameController;
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameControllerTestDoCollisions.json", customConverters = "customConverters")
    public void testDoCollisions(final JsonParameterSet params) {
        // setup
        final var gameController = setupGameController(params);
        final var gameState = gameController.getGameState();
        final var context = params.toContext();

        final List<IDBullet> bullets = gameState.getSprites().stream()
            .filter(IDBullet.class::isInstance)
            .map(IDBullet.class::cast)
            .toList();
        final var hits = params.<Map<Integer, Integer>>get("hits")
            .entrySet()
            .stream()
            .map(e -> Map.entry(
                bullets
                    .stream()
                    .filter(b -> b.getId() == e.getKey())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invalid Test: Bullet not found")),
                gameState.getSprites()
                    .stream()
                    .filter(WithID.class::isInstance)
                    .map(WithID.class::cast)
                    .filter(s -> s.getId() == e.getValue())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invalid Test: Sprite not found"))
            ))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
        bullets.forEach(b -> {
            Mockito.doAnswer(invocation -> {
                final WithID target = invocation.getArgument(0);
                return hits.containsKey(b) && hits.get(b) == target;
            }).when(b).canHit(Mockito.any());
//            CAN_HIT_METHOD.doAnswer(
//                context,
//                b,
//                invocation -> {
//                    final WithID target = invocation.getArgument(0);
//                final var hit = hits.getOrDefault(b.getId(), -1);
//                    if (hit == -1) {
//                        return false;
//                    }
//                    return target.getId() == hit;
//                },
//                Mockito.any()
//            );

            HIT_METHOD.alwaysDoNothing(context, b);
        });

        // test
        DO_COLLISIONS_METHOD.invoke(context, gameController);

        hits.forEach((bullet, target) -> {
            // verify canHit was called at least once before each hit
            Mockito.verify(bullet, Mockito.atLeastOnce()).canHit((BattleShip) target);
            // verify hit was called exactly once for each hit
            Mockito.verify(bullet, Mockito.times(1)).hit((BattleShip) target);
        });

        // verify no other calls to hit
        bullets.forEach(b -> {
            Mockito.verify(b, Mockito.atMost(hits.containsKey(b) ? 1 : 0)).hit(Mockito.any());
        });
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameControllerTestUpdatePoints.json", customConverters = "customConverters")
    public void testUpdatePoints(final JsonParameterSet params) {
        // setup
        final var gameController = setupGameController(params);
        final var gameState = gameController.getGameState();
        final var context = params.toContext("expectedPoints");

        final Map<Sprite, Boolean> damaged = params.<Map<Integer, Boolean>>get("damaged")
            .entrySet()
            .stream()
            .map(d -> Map.entry(
                     gameState.getSprites()
                         .stream()
                         .filter(s -> s instanceof WithID sid && sid.getId() == d.getKey())
                         .findFirst()
                         .orElseThrow(() -> new RuntimeException("Invalid Test: Bullet not found")),
                     d.getValue()
                 )
            )
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
        damaged.forEach((sprite, isDead) -> {
            IS_DEAD_METHOD.doReturnAlways(context, sprite, isDead);
        });

        // test
        UPDATE_POINTS_METHOD.invoke(context, gameController, damaged.keySet().stream().toList());

        Assertions2.assertEquals(
            params.get("expectedPoints"),
            gameController.getPlayer().getScore(),
            context,
            r -> "Incorrect player score after updatePoints() was called."
        );
    }
}
//    @ExtendWith(JagrExecutionCondition.class)
//        TestCycleResolver.getTestCycle().getClassLoader().l
