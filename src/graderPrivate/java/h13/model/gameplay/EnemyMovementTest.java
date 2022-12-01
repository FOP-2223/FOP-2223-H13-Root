package h13.model.gameplay;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.json.EnemyListConverter;
import h13.json.JsonBounds;
import h13.json.JsonEnemy;
import h13.model.gameplay.sprites.Enemy;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static h13.util.PrettyPrinter.prettyPrint;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers.sameTypes;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;

@TestForSubmission
public class EnemyMovementTest {
    public EnemyMovement enemyMovement;
    public GameState gameState;

    public enum EnemyMovementLinks {
        Y_TARGET_FIELD(BasicTypeLink.of(EnemyMovement.class).getField(identical("yTarget"))),
        VELOCITY_FIELD(BasicTypeLink.of(EnemyMovement.class).getField(identical("velocity"))),
        DIRECTION_FIELD(BasicTypeLink.of(EnemyMovement.class).getField(identical("direction"))),
        GET_ENEMY_BOUNDS_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("getEnemyBounds"))),
        TARGET_REACHED_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("targetReached"))),
        UPDATE_POSITIONS_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("updatePositions"))),
        NEXT_MOVEMENT_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("nextMovement"))),
        NEXT_ROUND_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("nextRound"))),
        UPDATE_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("update"))),
        ;
        private final Link link;

        EnemyMovementLinks(final Link link) {
            this.link = link;
        }

        public Link getLink() {
            return link;
        }

        public MethodLink getMethodLink() {
            return (MethodLink) link;
        }

        public FieldLink getFieldLink() {
            return (FieldLink) link;
        }

        public void invoke(final Object instance, final Object... args) {
            Assertions2.call(
                () -> getMethodLink().invoke(instance, args),
                Assertions2.emptyContext(),
                result -> "The method " + getMethodLink().name() + " should not throw any exceptions"
            );
        }

        public void invoke(final Context context, final Object instance, final Object... args) {
            Assertions2.call(
                () -> getMethodLink().invoke(instance, args),
                context,
                result -> "The method " + getMethodLink().name() + " should not throw any exceptions"
            );
        }
    }

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        gameState = new GameState();
        enemyMovement = spy(gameState.getEnemyMovement());
    }

    @ParameterizedTest
    @JsonClasspathSource("h13/model/gameplay/EnemyMovementTestGetEnemyBounds.json")
    void testGetEnemyBounds(
        @Property("enemies") @ConvertWith(EnemyListConverter.class) final List<JsonEnemy> enemies,
        @Property("SHIP_SIZE") final double SHIP_SIZE,
        @Property("enemyBounds") final JsonBounds expectedBounds
    ) {
        GameConstants.SHIP_SIZE = SHIP_SIZE;
        gameState.getSprites().addAll(enemies.stream().map(JsonEnemy::deserialize).toList());
        final Bounds actualBounds = enemyMovement.getEnemyBounds();
        final var context = Assertions2.contextBuilder()
            .add("enemies", prettyPrint(gameState.getEnemies()))
            .add("SHIP_SIZE", SHIP_SIZE)
            .build();
        Assertions2.assertEquals(expectedBounds.deserialize(), actualBounds, context, r -> "Calculated bounds are not correct.");
    }

    @ParameterizedTest
    @JsonClasspathSource("h13/model/gameplay/EnemyMovementTestBottomWasReached.json")
    void testBottomWasReached(
        @Property("GAME_BOUNDS") final JsonBounds GAME_BOUNDS,
        @Property("enemies") @ConvertWith(EnemyListConverter.class) final List<JsonEnemy> enemies,
        @Property("SHIP_SIZE") final double SHIP_SIZE,
        @Property("enemyBounds") final JsonBounds enemyBounds,
        @Property("bottomWasReached") final boolean expectedBottomWasReached
    ) {
        GameConstants.ORIGINAL_GAME_BOUNDS = GAME_BOUNDS.deserialize();
        GameConstants.SHIP_SIZE = SHIP_SIZE;
        gameState.getSprites().addAll(enemies.stream().map(JsonEnemy::deserialize).toList());
        doReturn(enemyBounds.deserialize()).when(enemyMovement).getEnemyBounds();
        final var actual = enemyMovement.bottomWasReached();
        final var context = Assertions2.contextBuilder()
            .add("GAME_BOUNDS", GAME_BOUNDS.deserialize())
            .add("enemies", prettyPrint(gameState.getEnemies()))
            .add("SHIP_SIZE", SHIP_SIZE)
            .add("enemyBounds", enemyBounds.deserialize())
            .build();
        Assertions2.assertEquals(expectedBottomWasReached, actual, context, r -> "The return value is not correct.");
    }

    @ParameterizedTest
    @JsonClasspathSource("h13/model/gameplay/EnemyMovementTestNextMovement.json")
    void testNextMovement(
        @Property("GAME_BOUNDS") final JsonBounds GAME_BOUNDS,
        @Property("SHIP_SIZE") final double SHIP_SIZE,
        @Property("enemyBounds") final JsonBounds enemyBounds,
        @Property("VERTICAL_ENEMY_MOVE_DISTANCE") final double VERTICAL_ENEMY_MOVE_DISTANCE,
        @Property("oldYtarget") final double oldYtarget,
        @Property("newYtarget") final double newYtarget,
        @Property("oldDirection") final Direction oldDirection,
        @Property("newDirection") final Direction newDirection,
        @Property("ENEMY_MOVEMENT_SPEED_INCREASE") final double ENEMY_MOVEMENT_SPEED_INCREASE,
        @Property("oldVelocity") final double oldVelocity,
        @Property("newVelocity") final double newVelocity
    ) throws Exception {
        GameConstants.ORIGINAL_GAME_BOUNDS = GAME_BOUNDS.deserialize();
        GameConstants.SHIP_SIZE = SHIP_SIZE;
        GameConstants.ENEMY_MOVEMENT_SPEED_INCREASE = ENEMY_MOVEMENT_SPEED_INCREASE;
        GameConstants.VERTICAL_ENEMY_MOVE_DISTANCE = VERTICAL_ENEMY_MOVE_DISTANCE;
        gameState.getSprites().addAll(createEnemiesForBounds(enemyBounds.deserialize()));
        doReturn(enemyBounds.deserialize()).when(enemyMovement).getEnemyBounds();
        doReturn(false).when(enemyMovement).bottomWasReached();
        EnemyMovementLinks.Y_TARGET_FIELD.getFieldLink().set(enemyMovement, oldYtarget);
        EnemyMovementLinks.DIRECTION_FIELD.getFieldLink().set(enemyMovement, oldDirection);
        EnemyMovementLinks.VELOCITY_FIELD.getFieldLink().set(enemyMovement, oldVelocity);

        EnemyMovementLinks.NEXT_MOVEMENT_METHOD
            .invoke(enemyMovement, enemyBounds.deserialize());

        final var context = Assertions2.contextBuilder()
            .add("GAME_BOUNDS", GAME_BOUNDS.deserialize())
            .add("SHIP_SIZE", SHIP_SIZE)
            .add("enemies", prettyPrint(gameState.getEnemies()))
            .add("enemyBounds", enemyBounds.deserialize())
            .add("oldYtarget", oldYtarget)
            .add("oldDirection", oldDirection)
            .add("ENEMY_MOVEMENT_SPEED_INCREASE", ENEMY_MOVEMENT_SPEED_INCREASE)
            .add("oldVelocity", oldVelocity)
            .build();
        Stream.of(
                List.of("new yTarget", newYtarget, EnemyMovementLinks.Y_TARGET_FIELD),
                List.of("new direction", newDirection, EnemyMovementLinks.DIRECTION_FIELD),
                List.of("new velocity", newVelocity, EnemyMovementLinks.VELOCITY_FIELD)
            )
            .forEach(list -> {
                final var name = (String) list.get(0);
                final var expected = list.get(1);
                final var fieldLink = (EnemyMovementLinks) list.get(2);
                Assertions2.assertEquals(
                    expected,
                    fieldLink.getFieldLink().get(enemyMovement),
                    context,
                    r -> "The " + name + " Field is not correct."
                );
            });
    }

    /**
     * Creates two enemies that form a hitbox forming the given bounds.
     *
     * @param enemyBounds The bounds of the hitbox.
     */
    private List<Enemy> createEnemiesForBounds(final Bounds enemyBounds) {
        // check if it is even possible to create enemies for the given bounds
        if (enemyBounds.getWidth() < GameConstants.SHIP_SIZE || enemyBounds.getHeight() < GameConstants.SHIP_SIZE) {
            throw new IllegalArgumentException("The given bounds are too small to create enemies for.");
        }
        final var topleftEnemy = new Enemy(0, 0, 0, 1, null);
        final var bottomRightEnemy = new Enemy(0, 0, 0, 1, null);
        topleftEnemy.setX(enemyBounds.getMinX());
        topleftEnemy.setY(enemyBounds.getMinY());
        bottomRightEnemy.setX(enemyBounds.getMaxX() - GameConstants.SHIP_SIZE);
        bottomRightEnemy.setY(enemyBounds.getMaxY() - GameConstants.SHIP_SIZE);
        return List.of(topleftEnemy, bottomRightEnemy);
    }
}
