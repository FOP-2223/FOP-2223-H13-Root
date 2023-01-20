package h13.view.gui;

import com.fasterxml.jackson.databind.JsonSerializer;
import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpriteRendererTest extends FxTest {

    @Test
    public void testRenderSprite(String fileName, Bounds bounds, List<Sprite> sprites){
        BufferedImage generatedImage = SwingFXUtils.fromFXImage(generateImage(bounds, sprites), null);
        BufferedImage expectedImage = null;
        try {
            expectedImage = ImageIO.read(new File(fileName));
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


//    @Test
//    public void run(){
//        generateAllImages();
//    }

    private record TestData(String name, Bounds bounds, List<Sprite> sprites){}

    private static void generateAllImages(){
        List<TestData> testData = new ArrayList<>();
        testData.addAll(generateTexture());
        testData.addAll(generateNoTexture());

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
        StringBuilder builder = new StringBuilder();

        String testDataString = IntStream.range(0,testData.size()).mapToObj(i ->
            String.format("""
            {
              "Data%d":
              %s
            }""", i, generateJson(testData.get(i)))
        ).collect(Collectors.joining(",\n"));

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
              "bounds": %s,
              "image": "%s"
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

        String spritesString = IntStream.range(0,sprites.size()).mapToObj(i ->
            String.format("""
            {
              "Sprite%d":
              %s
            }""", i, generateJson(sprites.get(i)))
        ).collect(Collectors.joining(",\n"));

        return String.format(
            """
            [
            %s
              ]""", spritesString.indent(4));
    }

    private static String generateJson(Sprite sprite){
        return String.format("""
            {
              "x": "%d",
              "y": "%d",
              "color": "%s"
            }""", (int) sprite.getX(), (int) sprite.getY(), sprite.getTexture() != null ? "null" : sprite.getColor());
    }

    private static List<TestData> generateNoTexture(){
        List<TestData> testData = new ArrayList<>();

        List<Sprite> sprites = List.of(
            new BattleShip(0,0,0, Color.ORANGE, 3, mock(GameState.class)),
            new BattleShip(103,100,0, Color.RED, 3, mock(GameState.class)),
            new BattleShip(200,209,0, Color.GREEN, 3, mock(GameState.class)),
            new BattleShip(205,0,0, Color.BROWN, 3, mock(GameState.class)),
            new BattleShip(307,0,0, Color.AZURE, 3, mock(GameState.class)),
            new Player(57,50,0, mock(GameState.class)),
            new Player(152,150,0, mock(GameState.class)),
            new Player(250,259,0, mock(GameState.class)),
            new Player(250,203,0, mock(GameState.class)),
            new Bullet(100,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(200,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(100,200, mock(GameState.class), null, Direction.UP),
            new Bullet(5,47, mock(GameState.class), null, Direction.UP)
        );

        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i<sprites.size(); i++){
            Bounds bounds = new BoundingBox(0, 0, random.nextInt(300, 500), random.nextInt(300, 500));
            testData.add(new TestData("NoTextureDrawSingle" + i, bounds, List.of(sprites.get(i))));
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
            testData.add(new TestData("NoTextureDrawMultiple" + i, bounds, selSprites));
        }

        for (TestData data: testData){
            for (Sprite sprite: data.sprites){
                sprite.setTexture(null);
            }
        }

        return testData;
    }

    private static List<TestData> generateTexture(){
        List<TestData> testData = new ArrayList<>();

        List<Sprite> sprites = List.of(
            new BattleShip(0,0,0, Color.ORANGE, 3, mock(GameState.class)),
            new BattleShip(103,100,0, Color.RED, 3, mock(GameState.class)),
            new BattleShip(200,209,0, Color.GREEN, 3, mock(GameState.class)),
            new BattleShip(205,0,0, Color.BROWN, 3, mock(GameState.class)),
            new BattleShip(307,0,0, Color.AZURE, 3, mock(GameState.class)),
            new Player(57,50,0, mock(GameState.class)),
            new Player(152,150,0, mock(GameState.class)),
            new Player(250,259,0, mock(GameState.class)),
            new Player(250,203,0, mock(GameState.class)),
            new Bullet(100,5, mock(GameState.class), null, Direction.DOWN),
            new Bullet(200,5, mock(GameState.class), null, Direction.RIGHT),
            new Bullet(100,200, mock(GameState.class), null, Direction.UP),
            new Bullet(5,47, mock(GameState.class), null, Direction.UP)
        );

        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i<sprites.size(); i++){
            Bounds bounds = new BoundingBox(0, 0, random.nextInt(300, 500), random.nextInt(300, 500));
            testData.add(new TestData("TextureDrawSingle" + i, bounds, List.of(sprites.get(i))));
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
            testData.add(new TestData("TextureDrawMultiple" + i, bounds, selSprites));
        }

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
        if (expected.getWidth() != actual.getWidth() || expected.getHeight() != actual.getHeight()) {
            fail(context, r -> "Generated Image does not have the Correct size");
        }
        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                int finalX = x;
                int finalY = y;
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y), context, r -> String.format("Generated Image does not match expected. Wrong Color in Pixel (%d,%d)", finalX, finalY));
            }
        }
    }
}
