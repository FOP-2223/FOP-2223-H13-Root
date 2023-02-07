package h13.model.gameplay.sprites;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.ApplicationSettings;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.util.PrettyPrinter;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static h13.util.StudentLinks.BattleShipLinks.BattleShipMethodLink.IS_FRIEND_METHOD;
import static h13.util.StudentLinks.BattleShipLinks.BattleShipMethodLink.SHOOT_METHOD;
import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.DIE_METHOD;
import static h13.util.StudentLinks.SpriteLinks.SpriteMethodLink.IS_ALIVE_METHOD;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class BattleShipTest {

    @SuppressWarnings("unused")
    public final static Map<String, Function<JsonNode, ?>> customConverters = new HashMap<>() {
        {
            put("ship1", JsonConverter::toSprite);
            put("ship2", JsonConverter::toSprite);
        }
    };

    @BeforeEach
    public void initTests() {
        ApplicationSettings.loadTexturesProperty().set(false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "BattleShipTestIsFriend.json", customConverters = "customConverters")
    public void isFriend(final JsonParameterSet params) {
        final BattleShip ship1 = params.get("ship1");
        final BattleShip ship2 = params.get("ship2");
        final boolean isFriend = params.getBoolean("isFriend");

        final Context context = contextBuilder()
            .add("Battleship 1", ship1)
            .add("Battleship 2", ship2)
            .build();

        assertEquals(isFriend, IS_FRIEND_METHOD.invoke(context, ship1, ship2), context, r -> "Ship was not correctly identified as Friend or Foe.");
    }

    @CartesianTest
    @CartesianTest.MethodFactory("provideIsFriend")
    public void isFriend(final BattleShip ship1, final BattleShip ship2) {
        final Context context = contextBuilder()
            .add("Battleship 1", ship1)
            .add("Battleship 2", ship2)
            .build();

        assertEquals(ship1.getClass().isInstance(ship2), IS_FRIEND_METHOD.invoke(context, ship1, ship2), context, r -> "Ship was not correctly identified as Friend or Foe.");
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    public void shoot_hasBullet(final Direction direction) {
        final GameState state = new GameState();
        final BattleShip ship = spy(new BattleShip(0, 0, 0, mock(Color.class), 1, state));

        final Context context = contextBuilder()
            .add("Ship has Bullet", false)
            .add("Direction", direction)
            .build();

        ApplicationSettings.instantShootingProperty().setValue(false);
        final Bullet firstBullet = spy(new Bullet(0, 0, mock(GameState.class), ship, Direction.UP));
        state.getSprites().add(firstBullet);
        state.getToAdd().add(firstBullet);

        IS_ALIVE_METHOD.doReturnAlways(context,firstBullet, true);

        ship.setBullet(firstBullet);
        SHOOT_METHOD.invoke(context, ship, direction);

        Assertions2.call(
            () -> verify(ship, atMostOnce()).setBullet(any()),
            context,
            r -> "Ship.setBullet() was called more than once"
        );
        ApplicationSettings.instantShootingProperty().setValue(true);
        ship.shoot(direction);


        Assertions2.call(
            () -> verify(ship, times(2)).setBullet(any()),
            context,
            r -> "Ship.setBullet() was not called twice"
        );
        assertTrue(state.getToAdd().contains(firstBullet), context, r -> "Original Bullet was removed but should not have been");
        assertTrue(state.getSprites().contains(firstBullet), context, r -> "Original Bullet was removed but should not have been");
        DIE_METHOD.verify(context, firstBullet, never());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    public void shoot_hasNoBullet(final Direction direction) {
        final GameState state = new GameState();
        final BattleShip ship = spy(new BattleShip(0, 0, 0, mock(Color.class), 1, state));

        final Context context = contextBuilder()
            .add("Ship bounds", PrettyPrinter.prettyPrint(ship.getBounds()))
            .add("Ship has Bullet", true)
            .add("Direction", direction)
            .build();

        SHOOT_METHOD.invoke(context, ship, direction);
        final Bullet bullet = ship.getBullet();
        assertNotNull(bullet, context, r -> "Bullet was not created or not added to Ship");

        final Context context2 = contextBuilder()
            .add(context)
            .add("Bullet bounds", PrettyPrinter.prettyPrint(Objects.requireNonNull(bullet).getBounds()))
            .build();


        assertEquals(direction, Objects.requireNonNull(bullet).getDirection(), context2, r -> "Bullet Direction did not match expected");
        assertTrue(state.getToAdd().contains(bullet), context2, r -> "GameState toAdd list does not contain created Bullet");

        assertEquals(ship.getBounds().getCenterX(), bullet.getBounds().getCenterX(), context2, r -> "Bullet is not correctly centered on BattleShip. X coordinate is not Correct");
        assertEquals(ship.getBounds().getCenterY(), bullet.getBounds().getCenterY(), context2, r -> "Bullet is not correctly centered on BattleShip. Y coordinate is not Correct");
    }

    /**
     * Generates the Arguments used for the tests for isFriend.
     *
     * @return a ArgumentSets containing all arguments for the test
     */
    @SuppressWarnings("unused")
    private static ArgumentSets provideIsFriend() {
        ApplicationSettings.loadTexturesProperty().set(false);
        final List<BattleShip> ships = List.of(
            new BattleShip(0, 0, 0, Color.AQUA, 1, mock(GameState.class)),
            new BattleShip(10, 10, 5, Color.AQUA, 1, mock(GameState.class)),
            new Enemy(0, 0, 0, 0, mock(GameState.class)),
            new Enemy(10, 10, 5, 0, mock(GameState.class)),
            new Player(0, 0, 0, mock(GameState.class)),
            new Player(10, 10, 5, mock(GameState.class))
        );
        return ArgumentSets.argumentsForFirstParameter(ships).argumentsForNextParameter(ships);
    }
}
