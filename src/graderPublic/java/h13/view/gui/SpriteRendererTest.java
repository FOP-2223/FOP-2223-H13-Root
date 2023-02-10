package h13.view.gui;

import com.fasterxml.jackson.databind.JsonNode;
import h13.controller.ApplicationSettings;
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
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@TestForSubmission
public class SpriteRendererTest extends FxTest {

    @SuppressWarnings("unused")
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
            testScale * 256,
            testScale * 224
        ));
        StudentLinks.GameConstantsLinks.GameConstantsFieldLink.SHIP_SIZE_FIELD.setStatic(testScale * 15.825454545454544);
        ApplicationSettings.loadTexturesProperty().set(false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RenderSpriteTestTexture.json", customConverters = "customConverters")
    public void testRenderSprite_Texture(final JsonParameterSet params){
        runTest(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RenderSpriteTestNoTexture.json", customConverters = "customConverters")
    public void testRenderSprite_NoTexture(final JsonParameterSet params){
        runTest(params);
    }

    private void runTest(final JsonParameterSet params){
        final String fileName = params.getString("image");
        final Bounds bounds = params.get("GAME_BOUNDS");
        final List<Sprite> sprites = params.get("sprites");

        final BufferedImage generatedImage = SwingFXUtils.fromFXImage(generateImage(bounds, sprites), null);
        final BufferedImage expectedImage = FxTest.loadImage(fileName);

        final Context context = contextBuilder()
            .add("Loaded Image", fileName)
            .add("Sprites", PrettyPrinter.prettyPrint(sprites))
            .add("bounds", bounds)
            .build();

        assertEqualsImage(expectedImage, generatedImage, context);
    }

    private static Image generateImage(final Bounds bounds, final List<Sprite> spritesToRender){
        final Canvas canvas = new Canvas(bounds.getWidth(), bounds.getHeight());
        final var gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(true);
        gc.setFontSmoothingType(FontSmoothingType.GRAY);

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, bounds.getWidth(), bounds.getHeight());

        for (final Sprite sprite: spritesToRender){
            SpriteRenderer.renderSprite(gc, sprite);
        }

        return renderImage(bounds, canvas);
    }

    //Everything following this Line is Used to generate Test Cases.

//    @Test
//    public void run(){
//        generateAllImages();
//    }

    private record TestData(String name, Bounds bounds, List<Sprite> sprites){}

    private static void generateAllImages(){
        final List<TestData> testData = new ArrayList<>();
        final List<TestData> texture = generateTexture(generateSprites());
        final List<TestData> noTexture = generateNoTexture(generateSprites());
        testData.addAll(texture);
        testData.addAll(noTexture);

        for (final TestData data: testData){
            saveImage(data.name, getBufferedImage(generateImage(data.bounds, data.sprites)));
        }
        writeJsonFile(texture, "RenderSpriteTestTexture.json");
        writeJsonFile(noTexture, "RenderSpriteTestNoTexture.json");
    }

    private static void writeJsonFile(final List<TestData> testData, final String fileName){
        try {
            final FileOutputStream outputStream = new FileOutputStream(fileName);
            final byte[] strToBytes = generateJson(testData).getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateJson(final List<TestData> testData){

        final String testDataString = testData.stream().map(testDatum -> String.format("%s", generateJson(testDatum))).collect(Collectors.joining(",\n"));

        return String.format(
            """
            [
            %s
            ]
            """, testDataString.indent(2));
    }

    private static String generateJson(final TestData data){
        return String.format(
            """
            {
              "sprites": %s,
              "GAME_BOUNDS": %s,
              "image": "/h13/view/gui/image/%s.png"
            }""", generateJsonFromSprites(data.sprites), generateJsonFromBounds(data.bounds), data.name);
    }

    private static List<Sprite> generateSprites(){
        return List.of(
            new EnemyC(0,0,0, 0, mock(GameState.class)),
            new EnemyC(103,100,0, 3, mock(GameState.class)),
            new EnemyC(200,209,0, 3, mock(GameState.class)),
            new EnemyC(205,0,0, 3, mock(GameState.class)),
            new EnemyC(307,0,0, 3, mock(GameState.class)),
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

    private static List<TestData> generateData(final List<Sprite> sprites, final String baseName){
        final List<TestData> testData = new ArrayList<>();

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i<sprites.size(); i++){
            final Bounds bounds = new BoundingBox(0, 0, random.nextInt(300, 500), random.nextInt(300, 500));
            testData.add(new TestData(baseName + "Single" + i, bounds, List.of(sprites.get(i))));
        }

        for (int i = 0; i<10; i++){
            final Bounds bounds = new BoundingBox(0, 0, random.nextInt(300, 500), random.nextInt(300, 500));
            final List<Sprite> selSprites = new ArrayList<>();
            while (selSprites.size() < 3){
                final Sprite test = sprites.get(random.nextInt(sprites.size()));
                if (!selSprites.contains(test)){
                    selSprites.add(test);
                }
            }
            testData.add(new TestData(baseName + "Multiple" + i, bounds, selSprites));
        }
        return testData;
    }

    private static List<TestData> generateNoTexture(final List<Sprite> sprites){
        final List<TestData> testData = generateData(sprites, "NoTextureDraw");

        for (final TestData data: testData){
            for (final Sprite sprite: data.sprites){
                sprite.setTexture(null);
            }
        }

        return testData;
    }

    private static List<TestData> generateTexture(final List<Sprite> sprites) {
        return generateData(sprites, "TextureDraw");
    }
}
