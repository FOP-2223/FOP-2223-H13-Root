package h13.view.gui;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.controller.gamelogic.PlayerController;
import h13.controller.scene.game.GameController;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import h13.util.PrettyPrinter;
import h13.util.StudentLinks;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class GameBoardTest extends FxTest {

    private GraphicsContext graphicsContext;
    private Player player;
    private GameScene scene;
    private GameController controller;
    private PlayerController playerController;
    private GameState state;
    private GameBoard board;

    @SuppressWarnings("unused")
    public final static Map<String, Function<JsonNode, ?>> customConverters = new HashMap<>() {
        {
            put("enemies", JsonConverter::toSpriteList);
            put("bullets", JsonConverter::toSpriteList);
            put("player", JsonConverter::toSprite);
        }
    };

    @BeforeEach
    public void setupGameVariables(){
        //load game constants
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(
            0,
            0,
            testScale * 256,
            testScale * 224
        ));
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.SHIP_SIZE_FIELD.setStatic(testScale * 15.825454545454544);
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.HUD_FONT_PATH_FIELD.setStatic("/h13/fonts/PressStart2P-Regular.ttf");
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.HUD_FONT_SIZE_FIELD.setStatic(0.045 * GameConstants.ORIGINAL_GAME_BOUNDS.getHeight());
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.HUD_FONT_FIELD.setStatic(javafx.scene.text.Font.loadFont(GameConstants.class.getResourceAsStream(GameConstants.HUD_FONT_PATH), GameConstants.HUD_FONT_SIZE));
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.HUD_PADDING_FIELD.setStatic(0.02 * GameConstants.ORIGINAL_GAME_BOUNDS.getHeight());
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.HUD_TEXT_COLOR_FIELD.setStatic(Color.BLACK);
        ApplicationSettings.loadTexturesProperty().set(false);

        //setup graphics Context
        final Canvas canvas = new Canvas(GameConstants.ORIGINAL_GAME_BOUNDS.getWidth(), GameConstants.ORIGINAL_GAME_BOUNDS.getHeight());
        graphicsContext = canvas.getGraphicsContext2D();

        //mock needed Objects
        player = spy(mock(Player.class, CALLS_REAL_METHODS));
        scene = spy(mock(GameScene.class, CALLS_REAL_METHODS));
        controller = spy(mock(GameController.class, CALLS_REAL_METHODS));
        playerController = spy(mock(PlayerController.class, CALLS_REAL_METHODS));
        state = spy(new GameState());

        StudentLinks.GameSceneLinks.GameSceneMethodLink.GET_CONTROLLER_METHOD.doReturnAlways(scene,controller);
        StudentLinks.GameControllerLinks.GameControllerMethodLink.GET_PLAYER_METHOD.doReturnAlways(controller,player);
        StudentLinks.GameControllerLinks.GameControllerMethodLink.GET_GAME_STATE_METHOD.doReturnAlways(controller,state);
        StudentLinks.GameControllerLinks.GameControllerMethodLink.GET_PLAYER_CONTROLLER_METHOD.doReturnAlways(controller,playerController);

        //init GameBoard
        board = new GameBoard(GameConstants.ORIGINAL_GAME_BOUNDS.getWidth(), GameConstants.ORIGINAL_GAME_BOUNDS.getHeight(), scene);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1000, 2000, 3000})
    public void testDrawBackground_NoImage(final int seed) {
        final Random random = new Random(seed);
        final Context context = contextBuilder()
            .add("Seed", seed)
            .add("Expected Image", "/h13/view/gui/image/GameBoardTest_DrawBackground_NoImage_" + seed + ".png")
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        for (int i = 0; i < 10; i++){
            graphicsContext.setStroke(Color.GREEN);
            graphicsContext.fillRect(
                random.nextDouble(GameConstants.ORIGINAL_GAME_BOUNDS.getWidth()-10),
                random.nextDouble(GameConstants.ORIGINAL_GAME_BOUNDS.getHeight()-10),
                10,10
            );
        }

        StudentLinks.GameBoardLinks.GameBoardFieldLink.BACKGROUND_IMAGE_FIELD.set(board, null);
        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_BACKGROUND_METHOD.invoke(board, graphicsContext);
        final BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));

        final BufferedImage expected = loadImage("/h13/view/gui/image/GameBoardTest_DrawBackground_NoImage_" + seed + ".png");

        //saveImage("DrawBackground_NoImage_" + seed, actual);

        assertEqualsImage(expected, actual, context);
    }

    @ParameterizedTest
    @CsvSource({
        "/h13/images/wallpapers/Galaxy3.jpg,/h13/view/gui/image/GameBoardTest_DrawBackground_Image_Galaxy3.png",
        "/h13/images/wallpapers/Galaxy2.jpg,/h13/view/gui/image/GameBoardTest_DrawBackground_Image_Galaxy2.png",
        "/h13/images/wallpapers/Galaxy1.jpg,/h13/view/gui/image/GameBoardTest_DrawBackground_Image_Galaxy1.png"
    })
    public void testDrawBackground_Image(final String backgroundImage, final String expectedImage) {
        final Context context = contextBuilder()
            .add("Background Image", backgroundImage)
            .add("Expected Image", expectedImage)
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        StudentLinks.GameBoardLinks.GameBoardFieldLink.BACKGROUND_IMAGE_FIELD.set(board, SwingFXUtils.toFXImage(loadImage(backgroundImage),null));
        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_BACKGROUND_METHOD.invoke(board, graphicsContext);

        final BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));
        final BufferedImage expected = loadImage(expectedImage);

        //saveImage("GameBoardTest_DrawBackground_Image_" + backgroundImage.substring(backgroundImage.lastIndexOf("/")+1), actual);

        assertEqualsImage(expected, actual, context);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameBoardTestDrawSprites.json", customConverters = "customConverters")
    public void testDrawSprites(final JsonParameterSet params){
        final String expectedImageLocation = params.getString("image");
        final List<Enemy> enemyList = params.get("enemies");
        final List<Bullet> bullets = params.get("bullets");
        final Player player = params.get("player");

        final List<Sprite> sprites = new ArrayList<>();
        sprites.addAll(enemyList);
        sprites.addAll(bullets);
        sprites.add(player);

        Collections.shuffle(sprites, new Random(0));

        StudentLinks.GameStateLinks.GameStateMethodLink.GET_SPRITES_METHOD.doReturnAlways(state, new HashSet<>(sprites));
        StudentLinks.GameStateLinks.GameStateMethodLink.GET_ENEMIES_METHOD.doReturnAlways(state, new HashSet<>(enemyList));
        StudentLinks.GameStateLinks.GameStateMethodLink.GET_ALIVE_ENEMIES_METHOD.doReturnAlways(state, new HashSet<>(enemyList));
        StudentLinks.PlayerControllerLinks.PlayerControllerMethodLink.GET_PLAYER_METHOD.doReturnAlways(playerController, player);

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_SPRITES_METHOD.invoke(board, graphicsContext);

        final BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));
        final BufferedImage expected = loadImage(expectedImageLocation);

        assertEqualsImage(expected, actual, params.toContext());
    }

    @ParameterizedTest
    @CsvSource({
        "0,0",
        "1,500",
        "2,1000",
        "3,0",
        "3,10000",
        "3,7544",
        "2,2147483647"
    })
    public void testDrawHUD(final int lives, final int score){
        final Context context = contextBuilder()
            .add("Lives", lives)
            .add("Score", score)
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        StudentLinks.PlayerLinks.PlayerMethodLink.GET_SCORE_METHOD.doReturnAlways(player, score);
        StudentLinks.SpriteLinks.SpriteMethodLink.GET_HEALTH_METHOD.doReturnAlways(player, lives);

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_H_U_D_METHOD.invoke(board, graphicsContext);

        final BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));
        final BufferedImage expected = loadImage("/h13/view/gui/image/GameBoardTest_DrawHUD_"+score+"_"+lives+".png");

        //saveImage("DrawHUD_"+score+"_"+lives, actual);

        assertEqualsImage(expected, actual, context);
        //TODO gc.save()?
    }

    @ParameterizedTest
    @CsvSource({
        "GRAY,5",
        "BLUE,10",
        "CHOCOLATE,2",
        "TAN,1"
    })
    public void testDrawBorder(final String color, final int borderWidth){
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.BORDER_COLOR_FIELD.setStatic(Color.valueOf(color));
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.BORDER_WIDTH_FIELD.setStatic((double) borderWidth);

        final String expectedImage = "/h13/view/gui/image/GameBoardTest_DrawBorder_" + color + ".png";

        final Context context = contextBuilder()
            .add("Color", color)
            .add("Border Width", borderWidth)
            .add("Expected Image", expectedImage)
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_BORDER_METHOD.invoke(board, graphicsContext);

        final BufferedImage expected = loadImage(expectedImage);
        final BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));

        //saveImage("DrawBorder_" + color, actual);

        assertEqualsImage(expected, actual, context);
    }

//    @Test
//    public void run(){
//        generateDrawSprites();
//    }

    private void generateDrawSprites(){
        final String name = "Random";
        final List<Bullet> bullets = List.of(
            new Bullet(100,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(200,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(100,200, mock(GameState.class), null, Direction.UP),
            new Bullet(5,47, mock(GameState.class), null, Direction.UP),
            new Bullet(87,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(85,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(43,98, mock(GameState.class), null, Direction.UP),
            new Bullet(57,47, mock(GameState.class), null, Direction.UP)
        );
        final List<Enemy> enemies = List.of(
            new EnemyC(100,100,0, 0, mock(GameState.class)),
            new EnemyC(75,184,0, 0, mock(GameState.class)),
            new EnemyC(276,234,0, 0, mock(GameState.class)),
            new EnemyC(45,174,0, 0, mock(GameState.class)),
            new EnemyC(8,84,0, 0, mock(GameState.class)),
            new EnemyC(23,92,0, 0, mock(GameState.class)),
            new EnemyC(98,2,0, 0, mock(GameState.class)),
            new EnemyC(340,85,0, 0, mock(GameState.class))
        );
        final Player player = new Player(100, 100, 0, state);

        final Set<Sprite> sprites = new HashSet<>();
        sprites.addAll(bullets);
        sprites.addAll(enemies);
        sprites.add(player);


        final String json = String.format("""
            {
                "bullets" : %s,
                "enemies" : %s,
                "player" : %s,
                "image" : "/h13/view/gui/GameBoardTest_%s.png"
            },""", generateJsonFromSprites(bullets), generateJsonFromSprites(enemies), generateJsonFromSprite(player), name);

        StudentLinks.GameStateLinks.GameStateMethodLink.GET_SPRITES_METHOD.doReturnAlways(state, sprites);

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_SPRITES_METHOD.invoke(board, graphicsContext);
        saveImage("GameBoardTest_" + name, getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas())));

        try {
            final FileOutputStream outputStream = new FileOutputStream("DrawSpritesBits.json");
            final byte[] strToBytes = json.getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
