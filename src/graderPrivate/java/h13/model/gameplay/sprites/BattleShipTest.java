package h13.model.gameplay.sprites;

import h13.controller.GameConstants;
import h13.model.gameplay.GameState;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
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
