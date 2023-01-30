package h13.model.gameplay;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.sprites.Enemy;
import h13.shared.Utils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.junitpioneer.jupiter.params.DoubleRangeSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static h13.util.PrettyPrinter.prettyPrint;
import static h13.util.StudentLinks.EnemyMovementLinks.EnemyMovementFieldLink;
import static h13.util.StudentLinks.EnemyMovementLinks.EnemyMovementFieldLink.*;
import static h13.util.StudentLinks.EnemyMovementLinks.EnemyMovementMethodLink.*;
import static h13.util.StudentLinks.GameConstantsLinks.GameConstantsFieldLink.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestForSubmission
public class EnemyMovementTest {
    public EnemyMovement enemyMovement;
    public GameState gameState;

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        gameState = new GameState();
        enemyMovement = spy(gameState.getEnemyMovement());
    }

    @ParameterizedTest
    @JsonParameterSetTest("EnemyMovementTestGetEnemyBounds.json")
    void testGetEnemyBounds(final JsonParameterSet params) {
        SHIP_SIZE_FIELD.setStatic(params.getDouble("SHIP_SIZE"));
        final var context = params.toContext("enemyBounds");
        gameState.getSprites().addAll(params.get("enemies"));
        final Bounds actualBounds = GET_ENEMY_BOUNDS_METHOD.invoke(context, enemyMovement);
        Assertions2.assertEquals(
            params.get("enemyBounds"),
            actualBounds,
            context,
            r -> "Calculated bounds are not correct."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest("EnemyMovementTestBottomWasReached.json")
    void testBottomWasReached(final JsonParameterSet params) {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(params.get("GAME_BOUNDS"));
        SHIP_SIZE_FIELD.setStatic(params.getDouble("SHIP_SIZE"));
        final var enemyBounds = params.get("enemyBounds");
        gameState.getSprites().addAll(params.get("enemies"));
        final var context = params.toContext("bottomWasReached");
        GET_ENEMY_BOUNDS_METHOD.doReturn(context, enemyMovement, enemyBounds);
        final var actual = BOTTOM_WAS_REACHED_METHOD.invoke(context, enemyMovement);
        Assertions2.assertEquals(
            params.getBoolean("bottomWasReached"),
            actual,
            context,
            r -> "The return value is not correct."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest("EnemyMovementTestNextMovement.json")
    void testNextMovement(
        final JsonParameterSet params
    ) {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(params.get("GAME_BOUNDS"));
        SHIP_SIZE_FIELD.setStatic(params.getDouble("SHIP_SIZE"));
        ENEMY_MOVEMENT_SPEED_INCREASE_FIELD.setStatic(params.getDouble("ENEMY_MOVEMENT_SPEED_INCREASE"));
        VERTICAL_ENEMY_MOVE_DISTANCE_FIELD.setStatic(params.getDouble("VERTICAL_ENEMY_MOVE_DISTANCE"));
        final Bounds enemyBounds = params.get("enemyBounds");
        final var context = params.toContext("newYtarget", "newDirection", "newVelocity");
        gameState.getSprites().addAll(createEnemiesForBounds(enemyBounds));
        GET_ENEMY_BOUNDS_METHOD.doReturn(context, enemyMovement, enemyBounds);
        BOTTOM_WAS_REACHED_METHOD.doReturn(context, enemyMovement, false);
        Y_TARGET_FIELD.set(context, enemyMovement, params.getDouble("oldYtarget"));
        DIRECTION_FIELD.set(context, enemyMovement, params.get("oldDirection"));
        VELOCITY_FIELD.set(context, enemyMovement, params.getDouble("oldVelocity"));

        NEXT_MOVEMENT_METHOD
            .invoke(context, enemyMovement, enemyBounds);

        Stream.of(
                List.of("new yTarget", params.getDouble("newYtarget"), Y_TARGET_FIELD),
                List.of("new direction", params.get("newDirection"), DIRECTION_FIELD),
                List.of("new velocity", params.getDouble("newVelocity"), VELOCITY_FIELD)
            )
            .forEach(list -> {
                final var name = (String) list.get(0);
                final var expected = list.get(1);
                final var fieldLink = (EnemyMovementFieldLink) list.get(2);
                Assertions2.assertEquals(
                    expected,
                    fieldLink.get(enemyMovement),
                    context,
                    r -> "The " + name + " Field is not correct."
                );
            });
    }

    // test targetReached
    @ParameterizedTest
    @JsonParameterSetTest("EnemyMovementTestTargetReached.json")
    void testTargetReached(final JsonParameterSet params) {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(params.get("GAME_BOUNDS"));
        SHIP_SIZE_FIELD.setStatic(params.getDouble("SHIP_SIZE"));
        final Bounds enemyBounds = params.get("enemyBounds");
        gameState.getSprites().addAll(createEnemiesForBounds(enemyBounds));
        final var context = params.toContext("targetReached");
        GET_ENEMY_BOUNDS_METHOD.doReturn(context, enemyMovement, enemyBounds);
        BOTTOM_WAS_REACHED_METHOD.doReturn(context, enemyMovement, false);
        Y_TARGET_FIELD.set(context, enemyMovement, params.getDouble("yTarget"));
        DIRECTION_FIELD.set(context, enemyMovement, params.get("direction"));

        final boolean actual = TARGET_REACHED_METHOD.invoke(context, enemyMovement, enemyBounds);

        Assertions2.assertEquals(
            params.getBoolean("targetReached"),
            actual,
            context,
            r -> "The return value is not correct."
        );
    }

    @CartesianTest
    void testUpdatePositions(
        @DoubleRangeSource(from = -200, to = 200, step = 50, closed = true) final double deltaX,
        @DoubleRangeSource(from = -200, to = 200, step = 50, closed = true) final double deltaY
    ) {
        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(0, 0, 1000, 1000));
        final List<Point2D> enemyPositions = List.of(
            new Point2D(500, 500),
            new Point2D(690, 420)
        );
        final List<Enemy> enemies = List.of(
            new Enemy(0, 0, 0, 0, null),
            new Enemy(1, 1, 0, 0, null)
        );
        IntStream.range(0, enemies.size())
            .forEach(i -> {
                enemies.get(i).setX(enemyPositions.get(i).getX());
                enemies.get(i).setY(enemyPositions.get(i).getY());
            });
        gameState.getSprites().addAll(enemies);

        final var context = Assertions2.contextBuilder()
            .add("GAME_BOUNDS", GameConstants.ORIGINAL_GAME_BOUNDS)
            .add("enemies", prettyPrint(enemies))
            .add("deltaX", deltaX)
            .add("deltaY", deltaY)
            .build();
        UPDATE_POSITIONS_METHOD.invoke(context, enemyMovement, deltaX, deltaY);
        IntStream.range(0, enemies.size())
            .forEachOrdered(i -> {
                final var enemy = enemies.get(i);
                final var expectedX = enemyPositions.get(i).getX() + deltaX;
                final var expectedY = enemyPositions.get(i).getY() + deltaY;
                Assertions2.assertEquals(
                    expectedX,
                    enemy.getX(),
                    context,
                    r -> "The x position of enemy " + i + " is not correct."
                );
                Assertions2.assertEquals(
                    expectedY,
                    enemy.getY(),
                    context,
                    r -> "The y position of enemy " + i + " is not correct."
                );
            });
    }

    void testUpdate(final JsonParameterSet params, final boolean mockStudentCode) {
        final var context = params.toContext();
        final var gameBounds = params.<Bounds>get("GAME_BOUNDS");
        final var enemyBounds = params.<Bounds>get("enemyBounds");
        final var newEnemyBounds = params.<Bounds>get("newEnemyBounds");
        final var clampedEnemyBounds = params.<Bounds>get("clampedEnemyBounds");
        final boolean bottomWasReached = params.get("bottomWasReached");
        final boolean targetReached = params.get("targetReached");
        final double yTarget = params.get("yTarget", Double.class);
        final Direction direction = params.get("direction");
        final double velocity = params.get("velocity", Double.class);

        ORIGINAL_GAME_BOUNDS_FIELD.setStatic(gameBounds);
        SHIP_SIZE_FIELD.setStatic(params.get("SHIP_SIZE", Double.class));
        VERTICAL_ENEMY_MOVE_DISTANCE_FIELD.setStatic(params.get("VERTICAL_ENEMY_MOVE_DISTANCE", Double.class));
        ENEMY_MOVEMENT_SPEED_INCREASE_FIELD.setStatic(params.get("ENEMY_MOVEMENT_SPEED_INCREASE", Double.class));
        gameState.getSprites().addAll(createEnemiesForBounds(enemyBounds));
        if (mockStudentCode) {
            GET_ENEMY_BOUNDS_METHOD.doReturn(context, enemyMovement, enemyBounds);
            BOTTOM_WAS_REACHED_METHOD.doReturn(context, enemyMovement, bottomWasReached);
            TARGET_REACHED_METHOD.doReturn(context, enemyMovement, targetReached, enemyBounds);
        }
        Y_TARGET_FIELD.set(enemyMovement, yTarget);
        DIRECTION_FIELD.set(enemyMovement, direction);
        VELOCITY_FIELD.set(enemyMovement, velocity);
        final var enemies = createEnemiesForBounds(enemyBounds);
        gameState.getSprites().addAll(enemies);
        try (final var utilsMock = mockStatic(Utils.class, CALLS_REAL_METHODS)) {
            if (mockStudentCode) {
                utilsMock.when(() -> Utils.getNextPosition(
                        any(Bounds.class),
                        anyDouble(),
                        any(Direction.class),
                        anyDouble()
                    ))
                    .thenReturn(newEnemyBounds);
                utilsMock.when(() -> Utils.clamp(any(Bounds.class)))
                    .thenReturn(clampedEnemyBounds);
            }

            UPDATE_METHOD.invoke(enemyMovement, 1);
            TARGET_REACHED_METHOD.verify(

                context,
                enemyMovement,
                atLeast(1),
                newEnemyBounds
            );
            NEXT_MOVEMENT_METHOD.verify(
                context,
                enemyMovement,
                params.getBoolean("expectsNextMovementCall") ? atLeast(1) : never(),
                enemyBounds
            );
            UPDATE_POSITIONS_METHOD.verify(
                context,
                enemyMovement,
                params.get("expectsUpdatePositionsCall") ? atLeast(1) : never(),
                params.get("deltaX"),
                params.get("deltaY")
            );
        }
    }

    @ParameterizedTest
    @JsonParameterSetTest("EnemyMovementTestUpdateRegular.json")
    void testUpdateRegular(
        final JsonParameterSet params
    ) {
        testUpdate(params, true);
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
