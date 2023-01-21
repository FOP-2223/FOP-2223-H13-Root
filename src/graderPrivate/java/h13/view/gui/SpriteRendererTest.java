package h13.view.gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import h13.controller.GameConstants;
import h13.json.JsonConverter;
import h13.json.JsonParameterSet;
import h13.json.JsonParameterSetTest;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import h13.util.StudentLinks;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpriteRendererTest extends FxTest {

    public final static Map<String, Function<JsonNode, ?>> customConverters = new HashMap<>() {
        {
            put("sprites", JsonConverter::toSpriteList);

            putAll(JsonConverter.DEFAULT_CONVERTERS);
        }
    };

    @BeforeEach
    public void setupGameVariables(){
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.ORIGINAL_GAME_BOUNDS_FIELD.setStatic(new BoundingBox(
            0,
            0,
            256,
            224
        ));
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.SHIP_SIZE_FIELD.setStatic(15.825454545454544);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RenderSpriteTest.json", customConverters = "customConverters")
    public void testRenderSprite(JsonParameterSet params){
        String fileName = params.getString("image");
        Bounds bounds = params.get("GAME_BOUNDS");
        List<Sprite> sprites = params.get("sprites");

        BufferedImage generatedImage = SwingFXUtils.fromFXImage(generateImage(bounds, sprites), null);
        BufferedImage expectedImage = null;
        try {
//            System.out.println(new File(new URL(fileName).getFile()).exists());
//            System.out.println(new File(new URL(fileName).getFile()).toPath().toAbsolutePath());

            expectedImage = ImageIO.read(new File(getClass().getResource(fileName).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Context context = contextBuilder()
            .add("Loaded Image", fileName)
            .add("Sprites", sprites)
            .add("bounds", bounds)
            .build();

        assertEqualsImage(expectedImage, generatedImage, context);
    }


    @Test
    public void run(){
        generateAllImages();
    }

    private record TestData(String name, Bounds bounds, List<Sprite> sprites){}

    private static void generateAllImages(){
        List<TestData> testData = new ArrayList<>();
        testData.addAll(generateTexture(generateSprites()));
        testData.addAll(generateNoTexture(generateSprites()));

        for (TestData data: testData){
            saveImage(data.name, generateImage(data.bounds, data.sprites));
        }
        try {
            FileOutputStream outputStream = new FileOutputStream("RenderSpriteTest.json");
            byte[] strToBytes = generateJson(testData, null).getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateJson(List<TestData> testData, String s){

        String testDataString = testData.stream().map(testDatum -> String.format("%s", generateJson(testDatum))).collect(Collectors.joining(",\n"));

        return String.format(
            """
            [
            %s
            ]
            """, testDataString.indent(2));
    }

    private static String generateJson(TestData data){
        return String.format(
            """
            {
              "sprites": %s,
              "GAME_BOUNDS": %s,
              "image": "/h13/view/gui/image/%s.png"
            }""", generateJson(data.sprites), generateJson(data.bounds), data.name);
    }

    private static String generateJson(Bounds bounds){
        return String.format("""
            {
                "x": "%d",
                "y": "%d",
                "width": "%d",
                "height": "%d"
              }""", (int) bounds.getMinX(), (int) bounds.getMinY(), (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    private static String generateJson(List<Sprite> sprites){
        StringBuilder builder = new StringBuilder();

        String spritesString = sprites.stream().map(sprite -> String.format("%s", generateJson(sprite))).collect(Collectors.joining(",\n"));

        return String.format(
            """
            [
            %s
              ]""", spritesString.indent(4));
    }

    private static String generateJson(Sprite sprite){
        String type;
        if (sprite instanceof Bullet){
            type = "bullet";
        } else if (sprite instanceof Player){
            type = "player";
        } else {
            type = "enemy";
        }

        return String.format("""
            {
              "x": "%d",
              "y": "%d",
              "color": "%s",
              "type": "%s"
            }""", (int) sprite.getX(), (int) sprite.getY(), sprite.getTexture() != null ? "null" : sprite.getColor(), type);
    }

    private static List<Sprite> generateSprites(){
        return List.of(
            new Enemy(0,0,0, 0, mock(GameState.class)), //TODO copy
            new Enemy(103,100,0, 3, mock(GameState.class)),
            new Enemy(200,209,0, 3, mock(GameState.class)),
            new Enemy(205,0,0, 3, mock(GameState.class)),
            new Enemy(307,0,0, 3, mock(GameState.class)),
            new Player(57,50,0, mock(GameState.class)),
            new Player(152,150,0, mock(GameState.class)),
            new Player(250,259,0, mock(GameState.class)),
            new Player(250,203,0, mock(GameState.class)),
            new Bullet(100,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(200,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(100,200, mock(GameState.class), null, Direction.UP),
            new Bullet(5,47, mock(GameState.class), null, Direction.UP)
        );
    }

    private static List<TestData> generateData(List<Sprite> sprites, String baseName){
        List<TestData> testData = new ArrayList<>();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i<sprites.size(); i++){
            Bounds bounds = new BoundingBox(0, 0, random.nextInt(300, 500), random.nextInt(300, 500));
            testData.add(new TestData(baseName + "Single" + i, bounds, List.of(sprites.get(i))));
        }

        for (int i = 0; i<10; i++){
            Bounds bounds = new BoundingBox(0, 0, random.nextInt(300, 500), random.nextInt(300, 500));
            List<Sprite> selSprites = new ArrayList<>();
            while (selSprites.size() < 3){
                Sprite test = sprites.get(random.nextInt(sprites.size()));
                if (!selSprites.contains(test)){
                    selSprites.add(test);
                }
            }
            testData.add(new TestData(baseName + "Multiple" + i, bounds, selSprites));
        }
        return testData;
    }

    private static List<TestData> generateNoTexture(List<Sprite> sprites){
        List<TestData> testData = generateData(sprites, "NoTextureDraw");

        for (TestData data: testData){
            for (Sprite sprite: data.sprites){
                sprite.setTexture(null);
            }
        }

        return testData;
    }

    private static List<TestData> generateTexture(List<Sprite> sprites){
        List<TestData> testData = generateData(sprites, "TextureDraw");

        return testData;
    }

    private static Image generateImage(Bounds bounds, List<Sprite> spritesToRender){
        final Canvas canvas = new Canvas(bounds.getWidth(), bounds.getHeight());
        final var gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, bounds.getWidth(), bounds.getHeight());

        for (Sprite sprite: spritesToRender){
            SpriteRenderer.renderSprite(gc, sprite);
        }
        CompletableFuture<Image> fut = new CompletableFuture<>();

        final var writableImage = new WritableImage((int) bounds.getWidth(), (int) bounds.getHeight());
        Platform.runLater(() -> fut.complete(canvas.snapshot(null, writableImage)));

        try {
            return fut.get(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveImage(String name, Image image){
        final var outputFile = new File(name + ".png");
        try {
            final var bImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bImage, "png", outputFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertEqualsImage(BufferedImage expected, BufferedImage actual, Context context){
        assertEquals(expected.getWidth(), actual.getWidth(), context, r -> "Generated Image does not have the Correct width");
        assertEquals(expected.getHeight(), actual.getHeight(), context, r -> "Generated Image does not have the Correct width");
        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                int finalX = x;
                int finalY = y;
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y), context, r -> String.format("Generated Image does not match expected. Wrong Color in Pixel (%d,%d)", finalX, finalY));
            }
        }
    }
}
