package h13.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import h13.util.PrettyPrinter;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class JsonParameterSet {
    private final JsonNode rootNode;
    private final ObjectMapper objectMapper;

    private final Map<String, Function<JsonNode, ?>> customConverters;


    public JsonParameterSet(final JsonNode rootNode, final ObjectMapper objectMapper, final Map<String, Function<JsonNode, ?>> customConverters) {
        this.rootNode = rootNode;
        this.objectMapper = objectMapper;
        this.customConverters = customConverters;
    }

    public JsonNode getRootNode() {
        return rootNode;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public <T> T get(final String key, final Class<T> type) {
        final var jsonNode = getRootNode().get(key);
        if (jsonNode == null) {
            throw new IllegalArgumentException("The given key does not exist in the json node. The Key: " + key);
        }
        return JsonConverter.convert(
            jsonNode,
            key,
            type,
            getObjectMapper(),
            Objects.requireNonNullElseGet(customConverters, () -> JsonConverter.DEFAULT_CONVERTERS)
        );
    }

    public <T> T get(final String key) {
        return get(key, null);
    }

    public boolean getBoolean(final String key) {
        return get(key, Boolean.class);
    }

    public byte getByte(final String key) {
        return get(key, Byte.class);
    }

    public short getShort(final String key) {
        return get(key, Short.class);
    }

    public int getInt(final String key) {
        return get(key, Integer.class);
    }

    public long getLong(final String key) {
        return get(key, Long.class);
    }

    public float getFloat(final String key) {
        return get(key, Float.class);
    }

    public double getDouble(final String key) {
        return get(key, Double.class);
    }

    public char getChar(final String key) {
        return get(key, Character.class);
    }

    public String getString(final String key) {
        return get(key, String.class);
    }

    public List<String> availableKeys() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(rootNode.fields(), Spliterator.ORDERED), false)
            .map(Map.Entry::getKey)
            .toList();
    }

    public Context toContext(final String... ignoreKeys) {
        final var builder = Assertions2.contextBuilder();
        for (final var key : availableKeys()) {
            if (Arrays.asList(ignoreKeys).contains(key)) {
                continue;
            }
            builder.add(key, PrettyPrinter.prettyPrint(get(key)));
        }
        return builder.build();
    }
}
