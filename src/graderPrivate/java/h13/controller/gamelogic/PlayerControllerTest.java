package h13.controller.gamelogic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import h13.controller.ApplicationSettings;
import h13.controller.scene.game.GameController;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.Player;
import h13.util.StudentLinks;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static h13.util.StudentLinks.GameInputHandlerLinks.GameInputHandlerFieldLink.KEYS_PRESSED_FIELD;
import static h13.util.StudentLinks.PlayerControllerLinks.PlayerControllerMethodLink.PLAYER_KEY_ACTION_METHOD;
import static org.mockito.Mockito.*;

@TestForSubmission
public class PlayerControllerTest {
    private Player player;
    private PlayerController playerController;
    @SuppressWarnings("unused")
    public final static Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("oldDirection", JsonConverter::toDirection),
        Map.entry("oldShouldKeepShooting", JsonNode::asBoolean),
        Map.entry("keysPressed", n -> JsonConverter.toList(n, JsonConverter::toKeyCode)),
        Map.entry("newShouldKeepShooting", JsonNode::asBoolean),
        Map.entry("newDirection", JsonConverter::toDirection)
    );

    @BeforeEach
    public void setup() {
        ApplicationSettings.loadTexturesProperty().set(false);
        final var gameState = new GameState();
        final var gameController = mock(GameController.class, CALLS_REAL_METHODS);
        StudentLinks.GameControllerLinks.GameControllerFieldLink.GAME_STATE_FIELD.set(gameController, gameState);
        playerController = spy(mock(PlayerController.class, CALLS_REAL_METHODS));
        StudentLinks.PlayerControllerLinks.PlayerControllerFieldLink.GAME_CONTROLLER_FIELD.set(playerController, gameController);

        player = new Player(0, 0, 0, null);
        player.setDirection(Direction.NONE);
        player.setKeepShooting(false);
        gameState.getSprites().add(player);

        StudentLinks.PlayerControllerLinks.PlayerControllerFieldLink.PLAYER_FIELD.set(playerController, player);
        final var gameInputHandler = mock(GameInputHandler.class, CALLS_REAL_METHODS);
        KEYS_PRESSED_FIELD.set(gameInputHandler, new HashSet<>());
        gameController.setGameInputHandler(gameInputHandler);
    }

    public void testPlayerKeyAction(
        final JsonParameterSet params,
        final boolean testDirection,
        final boolean testKeepShooting
    ) {
        final var context = params.toContext();
        final Direction oldDirection = params.get("oldDirection");
        final List<KeyCode> keysPressed = params.get("keysPressed");
        final boolean oldShouldKeepShooting = params.get("oldShouldKeepShooting");
        final Direction newDirection = params.get("newDirection");
        final boolean newShouldKeepShooting = params.get("newShouldKeepShooting");
        player.setDirection(oldDirection);
        player.setKeepShooting(oldShouldKeepShooting);
        final var gameInputHandler = playerController.getGameController().getGameInputHandler();
        KEYS_PRESSED_FIELD.set(context, gameInputHandler, new HashSet<>(keysPressed));
        PLAYER_KEY_ACTION_METHOD.invoke(context, playerController, createKeyPresEvent(keysPressed.isEmpty() ? KeyCode.UNDEFINED : keysPressed.get(0)));
        if (testDirection) {
            Assertions2.assertEquals(
                newDirection,
                player.getDirection(),
                context,
                r -> "Player has wrong direction after key press."
            );
        }
        if (testKeepShooting) {
            Assertions2.assertEquals(
                newShouldKeepShooting,
                player.isKeepShooting(),
                context,
                r -> "Players shouldKeepShooting flag is wrong after key press."
            );
        }
    }

    @CartesianTest
    public void testPlayerKeyActionNoKeys(@CartesianTest.Values(booleans = {true, false}) final boolean isReleased) {
        player.setDirection(Direction.UP);
        PLAYER_KEY_ACTION_METHOD.invoke(playerController, new KeyEvent(
            isReleased ? KeyEvent.KEY_RELEASED : KeyEvent.KEY_PRESSED,
            null,
            "",
            KeyCode.UNDEFINED,
            false,
            false,
            false,
            false
        ));
        Assertions2.assertEquals(
            Direction.NONE,
            player.getDirection(),
            Assertions2.emptyContext(),
            r -> "When no keys are pressed, the player should not move."
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "PlayerControllerTestPlayerKeyActionSingleKey.json", customConverters = "customConverters")
    public void testPlayerKeyActionSingleKey(final JsonParameterSet params) {
        testPlayerKeyAction(
            params,
            true,
            true
        );
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "PlayerControllerTestPlayerKeyActionMultipleKeys.json", customConverters = "customConverters")
    public void testPlayerKeyActionMultipleKeys(final JsonParameterSet params) {
        testPlayerKeyAction(
            params,
            true,
            true
        );
    }

    @Test
    public void generateJsonTestData() throws IOException {
//        generateJsonTestData(
//            true,
//            true,
//            true,
//            true,
//            3,
//            "PlayerControllerTestPlayerKeyActionMultipleKeys"
//        );
    }

    public static KeyEvent createKeyPresEvent(final KeyCode keyCode) {
        return new KeyEvent(
            KeyEvent.KEY_PRESSED,
            null,
            "",
            keyCode,
            false,
            false,
            false,
            false
        );
    }

    public static KeyEvent createKeyReleaseEvent(final KeyCode keyCode) {
        return new KeyEvent(
            KeyEvent.KEY_RELEASED,
            null,
            "",
            keyCode,
            false,
            false,
            false,
            false
        );
    }

    /**
     * Generates a json file with test data for the testPlayerKeyActionMultipleKeys test based on the current implementation.
     *
     * @param testDirectionKeys   Whether to test the direction keys.
     * @param testShootKeys       Whether to test the shoot keys.
     * @param testKeepShooting    Whether to test the keep shooting flag.
     * @param testOldDirections   Whether to test the old directions.
     * @param maxSimultaneousKeys The maximum number of keys that can be pressed simultaneously.
     * @param fileName            The name of the file to write the test data to.
     * @throws IOException If an error occurs while writing the file.
     */
    public void generateJsonTestData(
        final boolean testDirectionKeys,
        final boolean testShootKeys,
        final boolean testKeepShooting,
        final boolean testOldDirections,
        final int maxSimultaneousKeys,
        final String fileName
    ) throws IOException {
        final var directionKeys = List.of(
//            KeyCode.UP,
//            KeyCode.DOWN,
            KeyCode.LEFT,
            KeyCode.RIGHT,
            //KeyCode.W,
            KeyCode.A,
            // KeyCode.S,
            KeyCode.D
        );
        final var shootKeys = List.of(
            KeyCode.SPACE
        );

        // cross product of all keys
        final var keys = new HashSet<KeyCode>();
        if (testDirectionKeys) {
            keys.addAll(directionKeys);
        }
        if (testShootKeys) {
            keys.addAll(shootKeys);
        }
        final var keyCombinations = new HashSet<List<KeyCode>>();
        for (int i = 0; i < maxSimultaneousKeys; i++) {
            final var keyCombinationsCopy = new HashSet<>(keyCombinations);
            for (final var key : keys) {
                if (keyCombinationsCopy.isEmpty()) {
                    keyCombinations.add(List.of(key));
                } else {
                    for (final var keyCombination : keyCombinationsCopy) {
                        final var newKeyCombination = new ArrayList<KeyCode>(keyCombination);
                        newKeyCombination.add(key);
                        keyCombinations.add(newKeyCombination);
                    }
                }
            }
        }

        // generate json data using jackson databind
        /*
        Format:
        [
        { "oldDirection": "UP", "keysPressed": ["UP", "LEFT"], "oldShouldKeepShooting": false, "newDirection": "LEFT", "newShouldKeepShooting": false },
        { "oldDirection": "DOWN", "keysPressed": ["DOWN", "RIGHT"], "oldShouldKeepShooting": false, "newDirection": "RIGHT", "newShouldKeepShooting": false },
        { "oldDirection": "LEFT", "keysPressed": ["LEFT", "UP"], "oldShouldKeepShooting": false, "newDirection": "UP", "newShouldKeepShooting": false }
        ]
         */
        final var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // create array node
        final var arrayNode = mapper.createArrayNode();

        for (final var keyCombination : keyCombinations) {
            for (final var oldShouldKeepShooting : testKeepShooting ? List.of(false, true) : List.of(false)) {
                for (final var oldDirection : testOldDirections ? List.of(
                    //Direction.UP,
                    //Direction.DOWN,
                    Direction.LEFT,
                    Direction.RIGHT,
                    Direction.NONE
                ) : List.of(Direction.NONE)) {
                    player.setDirection(oldDirection);
                    player.setKeepShooting(oldShouldKeepShooting);
                    final var keysPressed = new HashSet<KeyCode>();
                    for (final var key : keyCombination) {
                        if (directionKeys.contains(key)) {
                            keysPressed.add(key);
                        } else if (shootKeys.contains(key)) {
                            player.setKeepShooting(true);
                        }
                    }
                    playerController.getGameController().getGameInputHandler().getKeysPressed().addAll(keysPressed);
                    PLAYER_KEY_ACTION_METHOD.invoke(playerController, createKeyPresEvent(keysPressed.isEmpty() ? KeyCode.UNDEFINED : keysPressed.iterator().next()));
                    final var newDirection = player.getDirection();
                    final var newShouldKeepShooting = player.isKeepShooting();
                    player.setDirection(oldDirection);
                    player.setKeepShooting(oldShouldKeepShooting);
                    playerController.getGameController().getGameInputHandler().getKeysPressed().clear();
                    final var json = mapper.writeValueAsString(Map.of(
                        "oldDirection", oldDirection,
                        "oldShouldKeepShooting", oldShouldKeepShooting,
                        "keysPressed", keysPressed,
                        "newDirection", newDirection,
                        "newShouldKeepShooting", newShouldKeepShooting
                    ));
                    arrayNode.add(mapper.readTree(json));
                }
            }
        }

        System.out.println(mapper.writeValueAsString(arrayNode));
        // write to file in src/graderPrivate/ressources/a/package/name/${fileName}.json
        final var resourcePath = "src/graderPrivate/resources/" + getClass()
            .getPackageName()
            .replace('.', '/') + "/" + fileName + ".json";
        System.out.println("Writing to " + resourcePath);
        mapper.writeValue(new File(resourcePath), arrayNode);
    }
}
