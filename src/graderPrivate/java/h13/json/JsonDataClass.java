package h13.json;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A class that can be deserialized from JSON.
 *
 * @param <T> The type of the deserialized object.
 */
public interface JsonDataClass<T> {
    /**
     * Deserializes the data class to the specified type.
     *
     * @return The deserialized data class.
     */
    public T deserialize();
}
