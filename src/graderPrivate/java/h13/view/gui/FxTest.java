package h13.view.gui;

import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.BeforeAll;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

public class FxTest {

    protected static final double testScale = 1;

    @BeforeAll
    public static void initJFX(){
        new JFXPanel();
    }

    public static Image renderImage(Bounds bounds, Canvas canvas) {
        CompletableFuture<Image> fut = new CompletableFuture<>();

        final var writableImage = new WritableImage((int) bounds.getWidth(), (int) bounds.getHeight());
        Platform.runLater(() -> fut.complete(canvas.snapshot(null, writableImage)));
        try {
            return fut.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getBufferedImage(Image image){
        return SwingFXUtils.fromFXImage(image, null);
    }

    public static BufferedImage loadImage(String imageToLoad) {
        try (
            InputStream stream = FxTest.class.getResourceAsStream(imageToLoad)) {
            return ImageIO.read(stream);
        } catch (
            IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This Method tests if the two given Images equal each other
     * @param expected the expected Image
     * @param actual the Image to test
     * @param context thew context in which the images are tested
     */
    public static void assertEqualsImage(BufferedImage expected, BufferedImage actual, Context context){
        if (expected == null && actual == null){
            return;
        }
        if (expected == null){
            fail(context, r -> "The Expected Image is null but the Actual image is not!");
        }
        if (expected == null){
            fail(context, r -> "The Actual Image is null but the Expected image is not!");
        }
        assertEquals(expected.getWidth(), actual.getWidth(), context, r -> "Generated Image does not have the Correct width");
        assertEquals(expected.getHeight(), actual.getHeight(), context, r -> "Generated Image does not have the Correct width");
        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                int finalX = x;
                int finalY = y;
                java.awt.Color expectedColor = new java.awt.Color(expected.getRGB(x, y));
                java.awt.Color actualColor = new java.awt.Color(actual.getRGB(x, y));
                assertEquals(expectedColor.getRed(), actualColor.getRed(), context, r -> String.format("Generated Image does not match expected. Wrong R-ColorValue in Pixel (%d,%d)", finalX, finalY));
                assertEquals(expectedColor.getGreen(), actualColor.getGreen(), context, r -> String.format("Generated Image does not match expected. Wrong G-ColorValue in Pixel (%d,%d)", finalX, finalY));
                assertEquals(expectedColor.getBlue(), actualColor.getBlue(), context, r -> String.format("Generated Image does not match expected. Wrong B-ColorValue in Pixel (%d,%d)", finalX, finalY));
            }
        }
    }

    public static void saveImage(String name, BufferedImage image){
        final var outputFile = new File(name + ".png");
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateJsonFromBounds(Bounds bounds){
        return String.format("""
            {
                "x": "%d",
                "y": "%d",
                "width": "%d",
                "height": "%d"
              }""", (int) bounds.getMinX(), (int) bounds.getMinY(), (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    public static String generateJsonFromSprites(List<? extends Sprite> sprites){
        StringBuilder builder = new StringBuilder();

        String spritesString = sprites.stream().map(sprite -> String.format("%s", generateJsonFromSprite(sprite))).collect(Collectors.joining(",\n"));

        return String.format(
            """
            [
            %s
              ]""", spritesString.indent(4));
    }

    public static String generateJsonFromSprite(Sprite sprite){
        String type = "null";
        String spriteTexture = null;
        if (sprite instanceof Bullet){
            type = "bullet";
            spriteTexture = "null";
        } else if (sprite instanceof Player){
            type = "player";
            spriteTexture = "/h13/images/sprites/player.png";
        } else if (sprite instanceof EnemyC e) {
            type = "enemy";
            spriteTexture = e.getTexturePath();
        }

        return String.format("""
            {
              "x": "%d",
              "y": "%d",
              "texture": "%s",
              "type": "%s"
            }""", (int) sprite.getX(), (int) sprite.getY(), sprite.getTexture() == null ? "null" : spriteTexture, type);
    }
}
