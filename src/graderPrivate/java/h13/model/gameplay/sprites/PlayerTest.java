package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.model.gameplay.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import static org.mockito.Mockito.*;

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
        player.update(0);
        verify(player, times(1)).shoot();
    }

    @Test
    public void testUpdateWithoutKeepShooting() {
        player.setKeepShooting(false);
        player.update(0);
        verify(player, never()).shoot();
    }
}
