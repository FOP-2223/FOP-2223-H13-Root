package h13.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.Enemy;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.util.List;
import java.util.stream.StreamSupport;

public class EnemyListConverter implements ArgumentConverter {
    static GameState gameState;

    /**
     * @param source  the source object to convert; may be {@code null}
     * @param context the parameter context where the converted object will be
     *                used; never {@code null}
     * @return
     * @throws ArgumentConversionException
     */
    @Override
    public List<JsonEnemy> convert(Object source, ParameterContext context) throws ArgumentConversionException {
        final Class<?> parameterType = context.getParameter().getType();
        if (!(source instanceof ArrayNode arrayNode)) {
            throw new ArgumentConversionException("Input type is not a JSON array");
        } else if (!(parameterType.isAssignableFrom(List.class))) {
            throw new ArgumentConversionException("Parameter type is not a List type");
        } else {
            System.out.println(source);
            // stream from the array node
            return StreamSupport.stream(arrayNode.spliterator(), false)
                    // map each element to a JsonEnemy
                    .map(JsonEnemy::fromJsonNode)
//                .map(JsonEnemy::deserialize)
                    // collect the stream into an array
                    .toList();
        }
    }
}
