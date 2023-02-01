package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.util.PrettyPrinter;
import h13.util.StudentLinks;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.junitpioneer.jupiter.params.IntRangeSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static h13.util.StudentLinks.BulletLinks.BulletMethodLink.CAN_HIT_METHOD;
import static h13.util.StudentLinks.BulletLinks.BulletMethodLink.HIT_METHOD;
import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.*;
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
                                 spy(new Player(pos2, pos2, 0, mock(GameState.class))) :
                                 spy(new Enemy(pos2, pos2, 0, 0, mock(GameState.class)));

        IS_ALIVE_METHOD.doReturn(ship2, isAlive);
        IS_DEAD_METHOD.doReturn(ship2, !isAlive);

        doReturn(enemy).when(ship1).isEnemy(any());
        doReturn(!enemy).when(ship1).isFriend(any());
        doReturn(enemy).when(ship2).isEnemy(any());
        doReturn(!enemy).when(ship2).isFriend(any());
        final Bullet bullet = new Bullet(0, 0, mock(GameState.class), ship1, Direction.UP);

        Context context = contextBuilder()
            .add("ship1", PrettyPrinter.prettyPrint(ship1))
            .add("ship2", PrettyPrinter.prettyPrint(ship2))
            .add("bullet", PrettyPrinter.prettyPrint(bullet))
            .add("isAlive", isAlive)
            .build();

        final boolean actual = CAN_HIT_METHOD.invoke(context, bullet, ship2);
        assertEquals(enemy && isAlive && isHit, actual, context, r -> String.format("Expected canHit to return %b but was %b", isHit && isAlive && enemy, actual));
    }

    @ParameterizedTest
    @ValueSource(ints = {-500, -43, 0, 100, 532})
    void canHit_MultiHit(final int position) {
        final BattleShip hitter = spy(new Enemy(position * 10, position * 10, 0, 0, mock(GameState.class)));
        final BattleShip toHit = spy(new Player(position, position, 0, mock(GameState.class)));

        final Bullet bullet = spy(new Bullet(position, position, mock(GameState.class), hitter, Direction.UP));

        Context context = contextBuilder()
            .add("Hitter", PrettyPrinter.prettyPrint(hitter))
            .add("To hit", PrettyPrinter.prettyPrint(toHit))
            .add("Bullet", PrettyPrinter.prettyPrint(bullet))
            .build();

        doReturn(false).when(hitter).isFriend(any());
        doReturn(false).when(toHit).isFriend(any());

        IS_DEAD_METHOD.doReturn(hitter, false);
        IS_DEAD_METHOD.doReturn(toHit, false);
        IS_DEAD_METHOD.doReturn(bullet, false);

        DAMAGE_METHOD_WITH_AMOUNT.alwaysDoNothing(context, bullet);
        DAMAGE_METHOD_WITH_AMOUNT.alwaysDoNothing(context, toHit);

        assertTrue(bullet.canHit(toHit), context, r -> String.format("The bullet should be able to hit the ship at position (%d,%d)", position, position));
        HIT_METHOD.invoke(context, bullet, toHit);
        assertFalse(bullet.canHit(toHit), context, r -> String.format("The bullet should not be able to hit the ship at position (%d,%d), because the ship has been hit already", position, position));
    }

    @Test
    public void hit() {
        final Bullet bullet = spy(new Bullet(0, 0, mock(GameState.class), mock(BattleShip.class), Direction.UP));
        final BattleShip ship = spy(new BattleShip(0, 0, 0, Color.AQUA, 1, mock(GameState.class)));

        final Context context = contextBuilder()
            .add("Hitting Bullet", PrettyPrinter.prettyPrint(bullet))
            .add("Ship to hit", PrettyPrinter.prettyPrint(ship))
            .build();

        DAMAGE_METHOD_WITH_AMOUNT.alwaysDoNothing(context, ship);
        DAMAGE_METHOD_WITH_AMOUNT.alwaysDoNothing(context, bullet);

        StudentLinks.BulletLinks.BulletMethodLink.CAN_HIT_METHOD.doReturn(bullet, true, ship);
        HIT_METHOD.invoke(context, bullet, ship);

        DAMAGE_METHOD_WITH_AMOUNT.verify(
            contextBuilder().add(context).add("error", "The ship that was hit did not take the expected one damage.").build(),
            ship,
            times(1),
            1
        );
        DAMAGE_METHOD_WITH_AMOUNT.verify(
            contextBuilder().add(context).add("error", "The Bullet that was hitting did not take the expected one damage.").build(),
            bullet,
            times(1),
            1
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20, 100})
    public void update(final int time) {
        final Bullet bullet = spy(new Bullet(0, 0, mock(GameState.class), mock(BattleShip.class), Direction.UP_LEFT));

        final Context context = contextBuilder()
            .add("Bullet", bullet)
            .add("Time passed", time)
            .build();

        IS_DEAD_METHOD.doReturnAlways(context, bullet, false);
        DIE_METHOD.alwaysDoNothing(context, bullet);

        UPDATE_METHOD.invoke(context, bullet, time);
        DIE_METHOD.verify(context, bullet, atLeastOnce());
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
