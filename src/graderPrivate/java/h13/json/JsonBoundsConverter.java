package h13.json;

import javafx.geometry.Bounds;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class JsonBoundsConverter implements ArgumentConverter {
    @Override
    public Object convert(final Object source, final ParameterContext context) throws ArgumentConversionException {
        if (source instanceof JsonBounds bounds) {
            return ((JsonBounds) source).deserialize();
//            return bounds;
        }
        throw new ArgumentConversionException("Could not convert " + source + " to Bounds");
    }
}
