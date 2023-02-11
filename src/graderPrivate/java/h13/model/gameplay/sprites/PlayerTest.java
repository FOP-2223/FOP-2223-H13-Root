package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.util.PrettyPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static h13.util.StudentLinks.BattleShipLinks.BattleShipMethodLink.SHOOT_METHOD;
import static h13.util.StudentLinks.PlayerLinks.PlayerMethodLink.UPDATE_METHOD;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        player = spy(new Player(0, 0, 0, mock(GameState.class)));
    }

    @Test
    public void testUpdateWithKeepShooting() {
        player.setKeepShooting(true);
        final var context = contextBuilder()
            .add("player", PrettyPrinter.prettyPrint(player))
            .add("keepShooting", player.isKeepShooting())
            .add("elapsedTime", 0)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();
        SHOOT_METHOD.alwaysDoNothing(context, player);

        UPDATE_METHOD.invoke(context, player, 0);
        SHOOT_METHOD.assertInvokedNTimesWithParams(context, player, 1, Direction.UP);
    }

    @Test
    public void testUpdateWithoutKeepShooting() {
        player.setKeepShooting(false);
        final var context = contextBuilder()
            .add("player", PrettyPrinter.prettyPrint(player))
            .add("keepShooting", player.isKeepShooting())
            .add("elapsedTime", 0)
            .add("requires other methods to work:", "super.update(elapsedTime)")
            .build();

        SHOOT_METHOD.alwaysDoNothing(context, player);

        UPDATE_METHOD.invoke(context, player, 0);
        SHOOT_METHOD.invokeWithoutCatching(context, verify(player, Mockito.never().description(context.toString())), any(Direction.class));
    }
}
