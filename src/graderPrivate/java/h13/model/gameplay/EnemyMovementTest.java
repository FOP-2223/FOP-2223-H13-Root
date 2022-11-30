package h13.model.gameplay;

import com.fasterxml.jackson.annotation.JsonProperty;
import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.json.*;
import h13.model.gameplay.sprites.Enemy;
import h13.shared.Utils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.conversion.ArrayConverter;
import spoon.support.compiler.jdt.ContextBuilder;

import java.util.List;

import static h13.util.PrettyPrinter.prettyPrint;
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

        // mock getNextPosition() from Utils class
//        var utilsMock = mockStatic(Utils.class);
//        utilsMock.when(() -> Utils.getNextPosition(any(Bounds.class), anyDouble(), any(Direction.class),anyDouble()))
//            .thenReturn(new BoundingBox(0, 0, 0, 0));
    }
}
