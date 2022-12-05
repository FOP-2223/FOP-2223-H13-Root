package h13.util;

import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;

/**
 * An Enum containing a {@link FieldLink} that links a field to a {@link Class}.
 */
public interface ClassFieldLink extends LinkHolder {

    /**
     * Gets the {@link FieldLink} representing the specified field.
     * @return The {@link FieldLink} representing the specified field.
     */
    @Override
    FieldLink getLink();

    /**
     * <p>Sets the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param instance the instance
     * @param value    the new value
     */
    default void set(final Object instance, final Object value) {
        getLink().set(instance, value);
    }

    /**
     * <p>Returns the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param instance the instance
     * @return the value of the field
     */
    default <T> T get(final Object instance) {
        return getLink().get(instance);
    }
}
