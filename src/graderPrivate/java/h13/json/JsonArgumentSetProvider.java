package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class JsonArgumentSetProvider implements ArgumentsProvider, AnnotationConsumer<JsonParameterSetTest> {

    private String JsonPath;
    private String customConvertersFieldName;

    @Override
    public void accept(final JsonParameterSetTest jsonParameterSetTest) {
        JsonPath = jsonParameterSetTest.value();
        customConvertersFieldName = jsonParameterSetTest.customConverters();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stream<? extends Arguments> provideArguments(final ExtensionContext context) throws Exception {
        final var testClass = context.getRequiredTestClass();
        Map<String, Function<JsonNode, ?>> customConverters = null;
        if (!customConvertersFieldName.isBlank()) {
            customConverters = (Map<String, Function<JsonNode, ?>>) testClass.getField(customConvertersFieldName).get(context.getTestInstance().orElse(null));
        }
        final var mapper = new ObjectMapper();
        final var url = testClass.getResource(JsonPath);
        Assertions.assertNotNull(url, "Could not find JSON file: " + JsonPath);
        final var rootNode = mapper.readTree(url);
        Assertions.assertTrue(rootNode.isArray(), "The root node of the JSON file should be an array.");
        final var argumentSets = new ArrayList<Arguments>();
        for (final var jsonNode : rootNode) {
            argumentSets.add(Arguments.of(new JsonParameterSet(jsonNode, mapper, customConverters)));
        }
        return argumentSets.stream();
    }
}
