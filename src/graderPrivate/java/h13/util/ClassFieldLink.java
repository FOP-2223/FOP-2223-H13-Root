package h13.util;

import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;

/**
 * An Enum containing a {@link FieldLink} that links a field to a {@link Class}.
 */
public interface ClassFieldLink extends LinkHolder {

    /**
     * Gets the {@link FieldLink} representing the specified field.
     *
     * @return The {@link FieldLink} representing the specified field.
     */
    @Override
    FieldLink getLink();

    /**
     * <p>Sets the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param context  The context
     * @param instance the instance
     * @param value    the new value
     */
    default void set(final Context context, final Object instance, final Object value) {
        getLink().set(instance, value);
        // TODO: context
    }

    /**
     * <p>Sets the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param instance the instance
     * @param value    the new value
     */
    default void set(final Object instance, final Object value) {
        set(Assertions2.emptyContext(), instance, value);
    }

    /**
     * <p>Returns the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param context  the context
     * @param instance the instance
     * @return the value of the field
     */
    default <T> T get(final Context context, final Object instance) {
        return getLink().get(instance);
        // TODO: context
    }

    /**
     * <p>Returns the object assigned to the given instance.</p>
     * <p>This field is required to be an instance field.</p>
     *
     * @param instance the instance
     * @return the value of the field
     */
    default <T> T get(final Object instance) {
        return get(Assertions2.emptyContext(), instance);
    }
}
