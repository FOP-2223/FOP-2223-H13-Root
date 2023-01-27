package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.shared.Utils;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.junitpioneer.jupiter.params.IntRangeSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class BulletTest {

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
    }

    @CartesianTest
    void canHit(
        @CartesianTest.Values(booleans = {true, false}) final boolean enemy,
        @CartesianTest.Values(booleans = {true, false}) final boolean isAlive,
        @IntRangeSource(from = -2000, to = 2000, step = 1000, closed = true) final int pos1,
        @IntRangeSource(from = -2000, to = 2000, step = 1000, closed = true) final int pos2
    ) {
        final BattleShip ship1 = spy(new Enemy(pos1, pos1, 0, 0, mock(GameState.class)));
        final boolean isHit = pos2 == 0;
        final BattleShip ship2 = enemy ?
            new Player(pos2, pos2, 0, mock(GameState.class)) :
            new Enemy(pos2, pos2, 0, 0, mock(GameState.class));

        if (!isAlive) {
            ship2.setHealth(0);
        }
        final Bullet bullet = new Bullet(0, 0, mock(GameState.class), ship1, Direction.UP);

        final boolean actual = bullet.canHit(ship2);

        if (enemy && isAlive && isHit) {
            verify(ship1, atLeastOnce()).isFriend(any());
        }

        assertEquals(enemy && isAlive && isHit, actual, context(), r -> String.format("Expected canHit to return %b but was %b", isHit && isAlive && enemy, actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-500, -43, 0, 100, 532})
    void canHit_MultiHit(final int position) {
        final BattleShip hitter = new Enemy(position * 10, position * 10, 0, 0, mock(GameState.class));
        final BattleShip toHit = new Player(position, position, 0, mock(GameState.class));

        final Bullet bullet = new Bullet(position, position, mock(GameState.class), hitter, Direction.UP);

        assertTrue(bullet.canHit(toHit), context(), r -> String.format("The bullet should be able to hit the ship at position %d", position));
        bullet.hit(toHit);
        assertFalse(bullet.canHit(toHit), context(), r -> String.format("The bullet should not be able to hit the ship at position %d", position));
    }

    @Test
    public void hit() {
        final Bullet bullet = new Bullet(0, 0, mock(GameState.class), mock(BattleShip.class), Direction.UP);
        final BattleShip ship = new BattleShip(0, 0, 0, Color.AQUA, 1, mock(GameState.class));

        final Context context = contextBuilder()
            .add("Hitting Bullet", bullet)
            .add("Ship to hit", ship)
            .build();

        // TODO: Mock damage, make canHit return true
        bullet.hit(ship);

        assertEquals(0, ship.getHealth(), context, r -> "The ship that was hit did not take the expected one damage.");
        assertEquals(0, bullet.getHealth(), context, r -> "The Bullet that was hitting did not take the expected one damage.");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20, 100})
    public void update(final int time) {
        final Bullet bullet = spy(new Bullet(0, 0, mock(GameState.class), mock(BattleShip.class), Direction.UP_LEFT));

        final Context context = contextBuilder()
            .add("Bullet", bullet)
            .add("Time passed", time)
            .build();

        try (final var utilsMock = mockStatic(Utils.class)) {
            bullet.update(time);

            verify(bullet, atLeastOnce()).die();
        }
    }

    @Test
    public void updateBasic() {
        try (final var testMock = mockStatic(SpriteTest.class)) {
            testMock.when(() -> SpriteTest.createSprite(
                    any(int.class)
                ))
                .thenReturn(spy(new Bullet(0, 0, mock(GameState.class), mock(BattleShip.class), Direction.UP_LEFT)));

            new SpriteTest().update_inside();
        }
    }
}
