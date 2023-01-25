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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import javax.swing.*;
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
        player = mock(Player.class);
        scene = mock(GameScene.class);
        controller = mock(GameController.class);
        playerController = mock(PlayerController.class);
        state = spy(new GameState());

        when(scene.getController()).thenReturn(controller);
        when(controller.getPlayer()).thenReturn(player);
        when(controller.getGameState()).thenReturn(state);
        when(controller.getPlayerController()).thenReturn(playerController);

        //init GameBoard
        board = new GameBoard(GameConstants.ORIGINAL_GAME_BOUNDS.getWidth(), GameConstants.ORIGINAL_GAME_BOUNDS.getHeight(), scene);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1000, 2000, 3000})
    public void testDrawBackground_NoImage(int seed) {
        Random random = new Random(seed);
        Context context = contextBuilder()
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
        BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));

        BufferedImage expected = loadImage("/h13/view/gui/image/GameBoardTest_DrawBackground_NoImage_" + seed + ".png");

        //saveImage("DrawBackground_NoImage_" + seed, actual);

        assertEqualsImage(expected, actual, context);
    }

    @ParameterizedTest
    @CsvSource({
        "/h13/images/wallpapers/Galaxy3.jpg,/h13/view/gui/image/GameBoardTest_DrawBackground_Image_Galaxy3.png",
        "/h13/images/wallpapers/Galaxy2.jpg,/h13/view/gui/image/GameBoardTest_DrawBackground_Image_Galaxy2.png",
        "/h13/images/wallpapers/Galaxy1.jpg,/h13/view/gui/image/GameBoardTest_DrawBackground_Image_Galaxy1.png"
    })
    public void testDrawBackground_Image(String backgroundImage, String expectedImage) {
        Context context = contextBuilder()
            .add("Background Image", backgroundImage)
            .add("Expected Image", expectedImage)
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        StudentLinks.GameBoardLinks.GameBoardFieldLink.BACKGROUND_IMAGE_FIELD.set(board, SwingFXUtils.toFXImage(loadImage(backgroundImage),null));
        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_BACKGROUND_METHOD.invoke(board, graphicsContext);

        BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));
        BufferedImage expected = loadImage(expectedImage);

        //saveImage("GameBoardTest_DrawBackground_Image_" + backgroundImage.substring(backgroundImage.lastIndexOf("/")+1), actual);

        assertEqualsImage(expected, actual, context);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "GameBoardTestDrawSprites.json", customConverters = "customConverters")
    public void testDrawSprites(JsonParameterSet params){
        String expectedImageLocation = params.getString("image");
        List<Enemy> enemyList = params.get("enemies");
        List<Bullet> bullets = params.get("bullets");
        Player player = params.get("player");

        List<Sprite> sprites = new ArrayList<>();
        sprites.addAll(enemyList);
        sprites.addAll(bullets);
        sprites.add(player);

        Collections.shuffle(sprites, new Random(0));

        when(state.getSprites()).thenReturn(new HashSet<>(sprites));
        when(state.getEnemies()).thenReturn(new HashSet<>(enemyList));
        when(state.getAliveEnemies()).thenReturn(new HashSet<>(enemyList));
        when(playerController.getPlayer()).thenReturn(player);

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_SPRITES_METHOD.invoke(board, graphicsContext);

        BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));
        BufferedImage expected = loadImage(expectedImageLocation);

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
    public void testDrawHUD(int lives, int score){
        Context context = contextBuilder()
            .add("Lives", lives)
            .add("Score", score)
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        when(player.getScore()).thenReturn(score);
        when(player.getHealth()).thenReturn(lives);

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_H_U_D_METHOD.invoke(board, graphicsContext);

        BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));
        BufferedImage expected = loadImage("/h13/view/gui/image/GameBoardTest_DrawHUD_"+score+"_"+lives+".png");

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
    public void testDrawBorder(String color, int borderWidth){
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.BORDER_COLOR_FIELD.setStatic(Color.valueOf(color));
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.BORDER_WIDTH_FIELD.setStatic((double) borderWidth);

        String expectedImage = "/h13/view/gui/image/GameBoardTest_DrawBorder_" + color + ".png";

        Context context = contextBuilder()
            .add("Color", color)
            .add("Border Width", borderWidth)
            .add("Expected Image", expectedImage)
            .add("Board Size", PrettyPrinter.prettyPrint(GameConstants.ORIGINAL_GAME_BOUNDS))
            .build();

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_BORDER_METHOD.invoke(board, graphicsContext);

        BufferedImage expected = loadImage(expectedImage);
        BufferedImage actual = getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas()));

        //saveImage("DrawBorder_" + color, actual);

        assertEqualsImage(expected, actual, context);
    }

//    @Test
//    public void run(){
//        generateDrawSprites();
//    }

    private void generateDrawSprites(){
        String name = "Random";
        List<Bullet> bullets = List.of(
            new Bullet(100,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(200,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(100,200, mock(GameState.class), null, Direction.UP),
            new Bullet(5,47, mock(GameState.class), null, Direction.UP),
            new Bullet(87,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(85,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(43,98, mock(GameState.class), null, Direction.UP),
            new Bullet(57,47, mock(GameState.class), null, Direction.UP)
        );
        List<Enemy> enemies = List.of(
            new EnemyC(100,100,0, 0, mock(GameState.class)),
            new EnemyC(75,184,0, 0, mock(GameState.class)),
            new EnemyC(276,234,0, 0, mock(GameState.class)),
            new EnemyC(45,174,0, 0, mock(GameState.class)),
            new EnemyC(8,84,0, 0, mock(GameState.class)),
            new EnemyC(23,92,0, 0, mock(GameState.class)),
            new EnemyC(98,2,0, 0, mock(GameState.class)),
            new EnemyC(340,85,0, 0, mock(GameState.class))
        );
        Player player = new Player(100, 100, 0, state);

        Set<Sprite> sprites = new HashSet<>();
        sprites.addAll(bullets);
        sprites.addAll(enemies);
        sprites.add(player);


        String json = String.format("""
            {
                "bullets" : %s,
                "enemies" : %s,
                "player" : %s,
                "image" : "/h13/view/gui/GameBoardTest_%s.png"
            },""", generateJsonFromSprites(bullets), generateJsonFromSprites(enemies), generateJsonFromSprite(player), name);

        when(state.getSprites()).thenReturn(sprites);

        StudentLinks.GameBoardLinks.GameBoardMethodLink.DRAW_SPRITES_METHOD.invoke(board, graphicsContext);
        saveImage("GameBoardTest_" + name, getBufferedImage(renderImage(GameConstants.ORIGINAL_GAME_BOUNDS, graphicsContext.getCanvas())));

        try {
            FileOutputStream outputStream = new FileOutputStream("DrawSpritesBits.json");
            byte[] strToBytes = json.getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
