package h13.json;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public record JsonBounds(int x, int y, int width, int height) implements JsonDataClass<Bounds> {
    @Override
    public Bounds deserialize() {
        return new BoundingBox(x, y, width, height);
    }
}
