package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.junitpioneer.jupiter.params.IntRangeSource;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

public class BulletTest {

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
    }

    record Ship(){}

    @CartesianTest
    void canHit(
        @CartesianTest.Values(booleans = {true, false}) boolean enemy,
        @CartesianTest.Values(booleans = {true, false}) boolean isAlive,
        @IntRangeSource(from = -2000, to = 2000, step = 1000, closed = true) int pos1,
        @IntRangeSource(from = -2000, to = 2000, step = 1000, closed = true) int pos2
    ){
        BattleShip ship1 = spy(new Enemy(pos1, pos1, 0, 0, mock(GameState.class)));
        boolean isHit = pos2 == 0;
        BattleShip ship2 = enemy ?
            new Player(pos2,pos2, 0, mock(GameState.class)):
            new Enemy(pos2, pos2, 0, 0, mock(GameState.class));

        if (!isAlive){
            ship2.setHealth(0);
        }
        Bullet bullet = new Bullet(0, 0, mock(GameState.class), ship1, Direction.UP);

        boolean actual = bullet.canHit(ship2);

        if (enemy && isAlive && isHit){
            verify(ship1, atLeastOnce()).isFriend(any());
        }

        assertEquals(enemy && isAlive && isHit, actual, context(), r -> String.format("Expected canHit to return %b but was %b", isHit && isAlive && enemy, actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-500, -43, 0, 100, 532})
    void canHit_MultiHit(int position){
        BattleShip hitter = new Enemy(position*10, position*10, 0, 0, mock(GameState.class));
        BattleShip toHit = new Player(position, position, 0, mock(GameState.class));

        Bullet bullet = new Bullet(position, position, mock(GameState.class), hitter, Direction.UP);

        assertTrue(bullet.canHit(toHit), context(), r -> String.format(""));
        bullet.hit(toHit);
        assertFalse(bullet.canHit(toHit), context(), r -> String.format(""));
    }

    @Test
    public void hit(){
        Bullet bullet = new Bullet(0,0, mock(GameState.class), mock(BattleShip.class), Direction.UP);
        BattleShip ship = new BattleShip(0,0,0, Color.AQUA, 1, mock(GameState.class));

        Context context = contextBuilder()
            .add("Hitting Bullet", bullet)
            .add("Ship to hit", ship)
            .build();

        bullet.hit(ship);

        assertEquals(0, ship.getHealth(), context, r -> "The ship that was hit did not take the expected one damage.");
        assertEquals(0, bullet.getHealth(), context, r -> "The Bullet that was hitting did not take the expected one damage.");
    }

    public void update(){
        Bullet bullet = new Bullet(0,0,mock(GameState.class), mock(BattleShip.class), Direction.UP_LEFT);
        int time = 10;

        Context context = contextBuilder()
            .add("Bullet", bullet)
            .add("Time passed", time)
            .build();

        spy(bullet);
        bullet.update(time);

        verify(bullet, atLeastOnce()).die();
        verify(bullet, atLeastOnce()).update(any()); //TODO test if super.update() is called
    }
}
