package h13.model.gameplay.sprites;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.ApplicationSettings;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.context;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class BattleShipTest {

    public final static Map<String, Function<JsonNode, ?>> customConverters = new HashMap<>() {
        {
            put("ship1", JsonConverter::toSprite);
            put("ship2", JsonConverter::toSprite);
        }
    };

    @BeforeEach
    public void initTests(){
        ApplicationSettings.loadTexturesProperty().set(false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "BattleShipTestIsFriend.json", customConverters = "customConverters")
    public void isFriend(JsonParameterSet params){
        BattleShip ship1 = params.get("ship1");
        BattleShip ship2 = params.get("ship2");
        boolean isFriend = params.getBoolean("isFriend");

        Context context = contextBuilder()
            .add("Battleship 1", ship1)
            .add("Battleship 2", ship2)
            .build();

        assertEquals(isFriend, ship1.isFriend(ship2), context, r -> "Ship was not correctly identified as Friend or Foe.");
    }

    @CartesianTest
    @CartesianTest.MethodFactory("provideIsFriend")
    public void isFriend(BattleShip ship1, BattleShip ship2){
        Context context = contextBuilder()
            .add("Battleship 1", ship1)
            .add("Battleship 2", ship2)
            .build();

        assertEquals(ship1.getClass().isInstance(ship2), ship1.isFriend(ship2), context, r -> "Ship was not correctly identified as Friend or Foe.");
    }

    @ParameterizedTest
    @EnumSource(value = Direction.class)
    public void shoot_hasBullet(Direction direction){
        GameState state = new GameState();
        BattleShip ship = spy(new BattleShip(0, 0, 0, mock(Color.class), 1, state));

        Context context = contextBuilder()
            .add("Direction", direction)
            .build();

        ApplicationSettings.instantShooting.setValue(false);
        Bullet firstBullet = spy(new Bullet(0, 0, mock(GameState.class), ship, Direction.UP));
        state.getSprites().add(firstBullet);
        state.getToAdd().add(firstBullet);

        ship.setBullet(firstBullet);
        ship.shoot(direction);

        verify(ship, atMostOnce()).setBullet(any());

        ApplicationSettings.instantShooting.setValue(true);
        ship.shoot(direction);

        verify(ship, times(2)).setBullet(any());
        assertTrue(state.getToAdd().contains(firstBullet), context, r -> "Orignal Bullet was removed but should not have been");
        assertTrue(state.getSprites().contains(firstBullet), context, r -> "Orignal Bullet was removed but should not have been");
        verify(firstBullet, never()).die();
    }

    @ParameterizedTest
    @EnumSource(value = Direction.class)
    public void shoot_hasNoBullet(Direction direction){
        GameState state = new GameState();
        BattleShip ship = spy(new BattleShip(0, 0, 0, mock(Color.class), 1, state));

        Context context = context();

        ship.shoot(direction);
        Bullet bullet = ship.getBullet();

        assertNotNull(bullet, context, r -> "Bullet was not created or not added to Ship");
        assertEquals(direction, bullet.getDirection(), context, r -> "Bullet Direction did not match expected");
        assertTrue(state.getToAdd().contains(bullet), context, r -> "GameState toAdd list does not contain created Bullet");

        assertEquals(ship.getBounds().getCenterX(), bullet.getBounds().getCenterX(), context, r -> "Bullet is not correctly centered on BattleShip. X coordinate is not Correct");
        assertEquals(ship.getBounds().getCenterY(), bullet.getBounds().getCenterY(), context, r -> "Bullet is not correctly centered on BattleShip. Y coordinate is not Correct");
    }

    /**
     * Generates the Arguments used for the tests for isFriend.
     * @return a ArgumentSets containing all arguments for the test
     */
    private static ArgumentSets provideIsFriend(){
        List<BattleShip> ships = List.of(
            new BattleShip(0,0,0, Color.AQUA,1, mock(GameState.class)),
            new BattleShip(10,10,5, Color.AQUA,1, mock(GameState.class)),
            new Enemy(0,0,0,0,mock(GameState.class)),
            new Enemy(10,10,5,0,mock(GameState.class)),
            new Player(0,0,0,mock(GameState.class)),
            new Player(10,10,5,mock(GameState.class))
        );
        return ArgumentSets.argumentsForFirstParameter(ships).argumentsForNextParameter(ships);
    }
}