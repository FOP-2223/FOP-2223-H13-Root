package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.mockito.Mockito;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.context;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

public class BattleShipTest {

    @Test
    public void isFriend(BattleShip ship1, BattleShip ship2, boolean isFriend){
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
    public void shoot_hasBullet_noInstant(Direction direction){
        BattleShip ship = spy(new BattleShip(0, 0, 0, mock(Color.class), 1, mock(GameState.class)));

        ApplicationSettings.instantShooting = new SimpleBooleanProperty(false);
        ship.setBullet(mock(Bullet.class));
        ship.shoot(direction);

        verify(ship, atMostOnce()).setBullet(any());

        ApplicationSettings.instantShooting = new SimpleBooleanProperty(true);
        ship.shoot(direction);

        verify(ship, times(2)).setBullet(any());
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
        assertTrue(state.getToAdd().contains(bullet), context, r -> "GameState toAdd list does not Contain created Bullet");

        assertEquals(ship.getBounds().getCenterX(), bullet.getBounds().getCenterX(), context, r -> "Bullet is not correctly centered on BattleShip. X coordinate is not Correct");
        assertEquals(ship.getBounds().getCenterY(), bullet.getBounds().getCenterY(), context, r -> "Bullet is not correctly centered on BattleShip. Y coordinate is not Correct");
    }

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
