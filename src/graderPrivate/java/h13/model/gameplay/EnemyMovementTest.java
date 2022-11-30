package h13.model.gameplay;

import com.fasterxml.jackson.annotation.JsonProperty;
import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.json.*;
import h13.model.gameplay.sprites.Enemy;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@TestForSubmission
public class EnemyMovementTest {
    public EnemyMovement enemyMovement;
    public GameState gameState;

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        gameState = new GameState();
        enemyMovement = gameState.getEnemyMovement();
    }

    @ParameterizedTest
    @JsonClasspathSource("h13/model/gameplay/EnemyMovementTestGetEnemyBounds.json")
    void testGetEnemyBounds(
        @Property("enemies")
        @ConvertWith(EnemyListConverter.class)
        List<JsonEnemy> enemies,
        @Property("SHIP_SIZE")
        double SHIP_SIZE,
        @Property("expectedBounds")
//        @ConvertWith(JsonBoundsConverter.class)
        JsonBounds expectedBounds
    ){
        System.out.println("SHIP_SIZE = " + SHIP_SIZE);
        System.out.println("expectedBounds = " + expectedBounds);
        System.out.printf("enemies = %s%n", enemies);
        GameConstants.SHIP_SIZE = SHIP_SIZE;
        gameState.getSprites().addAll(enemies.stream().map(JsonEnemy::deserialize).toList());
        Bounds actualBounds = enemyMovement.getEnemyBounds();
        var context = Assertions2.contextBuilder()
                .add("enemies", enemies)
                .add("SHIP_SIZE", SHIP_SIZE)
                .add("expectedBounds", expectedBounds)
                .add("actualBounds", actualBounds)
                .build();
        Assertions2.assertEquals(expectedBounds.deserialize(), actualBounds, context, r -> "Calculated bounds are not correct.");
    }
}
