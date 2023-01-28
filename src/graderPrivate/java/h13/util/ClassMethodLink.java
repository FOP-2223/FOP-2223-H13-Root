package h13.util;

import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

/**
 * An Enum containing a {@link MethodLink} that links a method to a {@link Class}.
 */
public interface ClassMethodLink extends LinkHolder {

    boolean MOCK_STUDENT_CODE = true;


    /**
     * Gets the {@link MethodLink} representing the specified method.
     *
     * @return The {@link MethodLink} representing the specified method.
     */
    @Override
    MethodLink getLink();

    default <T> T invoke(final Object instance, final Object... args) {
        return Assertions2.callObject(
            () -> getLink().invoke(instance, args),
            Assertions2.emptyContext(),
            result -> "The method " + getLink().name() + " should not throw any exceptions"
        );
    }

    default <T> T invoke(final @NotNull Context context, final Object instance, final Object... args) {
        return Assertions2.callObject(
            () -> getLink().invoke(instance, args),
            context,
            result -> "The method " + getLink().name() + " should not throw any exceptions"
        );
    }

    default Method getReflection() {
        return getLink().reflection();
    }

    default Class<?>[] getParameterTypes() {
        return getLink().reflection().getParameterTypes();
    }

    default Class<?> getReturnType() {
        return getLink().reflection().getReturnType();
    }

    default int getParameterCount() {
        return getLink().reflection().getParameterCount();
    }

    default Object[] getAnyArgumentMatchers() {
        return IntStream.range(0, getParameterCount()).mapToObj(i -> Mockito.any(getParameterTypes()[i])).toArray();
    }


    private void assertIsSpy(final @NotNull Context context, final Object instance) {
        Assertions2.assertTrue(
            Mockito.mockingDetails(instance).isSpy(),
            context,
            r -> "The given object should be a Spy."
        );
    }

    private void assertIsSpy(final Object instance) {
        assertIsSpy(Assertions2.emptyContext(), instance);
    }


    default void verify(
        final @NotNull Context context,
        final Object instance,
        final org.mockito.verification.VerificationMode mode,
        final Object... args
    ) {
        assertIsSpy(context, instance);
        invoke(
            context,
            Mockito.verify(
                instance,
                mode
            ),
            args
        );
    }

    default void verify(
        final Object instance,
        final org.mockito.verification.VerificationMode mode,
        final Object... args
    ) {
        verify(Assertions2.emptyContext(), instance, mode, args);
    }

    default void assertNeverInvokedWithParams(
        final @NotNull Context context,
        final Object instance,
        final Object... args
    ) {
        verify(context, instance, Mockito.never().description(context.toString()), args);
    }

    default void assertNeverInvoked(final @NotNull Context context, final Object instance) {
        assertNeverInvokedWithParams(context, instance, getAnyArgumentMatchers());
    }

    default void assertNeverInvoked(final Object instance) {
        assertNeverInvoked(Assertions2.emptyContext(), instance);
    }

    default void assertInvokedNTimesWithParams(
        final @NotNull Context context,
        final Object instance,
        final int times,
        final Object... args
    ) {
        verify(context, instance, Mockito.times(times).description(context.toString()), args);
    }

    default void assertInvokedNTimes(
        final @NotNull Context context,
        final Object instance,
        final int times
    ) {
        verify(context, instance, Mockito.times(times).description(context.toString()), getAnyArgumentMatchers());
    }

    default void doReturn(final Context context, final Object instance, final Object value, final Object... args) {
        if (MOCK_STUDENT_CODE) {
            invoke(
                context,
                Mockito.doReturn(value).when(instance),
                args
            );
        }
    }

    default void doReturnAlways(final Context context, final Object instance, final Object value) {
        doReturn(context, instance, value, getAnyArgumentMatchers());
    }

    default void doReturn(final Object instance, final Object value, final Object... args) {
        doReturn(Assertions2.emptyContext(), instance, value, args);
    }

    default void doReturnAlways(final Object instance, final Object value) {
        doReturn(instance, value, getAnyArgumentMatchers());
    }

    default <T> void doAnswer(final Context context, final Object instance, final Answer<T> answer, final Object... args) {
        if (MOCK_STUDENT_CODE) {
            invoke(
                context,
                Mockito.doAnswer(answer).when(instance),
                args
            );
        }
    }

    default <T> void doAnswer(final Object instance, final Answer<T> answer, final Object... args) {
        doAnswer(Assertions2.emptyContext(), instance, answer, args);
    }
    default <T> void doAnswerAlways(final Context context, final Object instance, final Answer<T> answer) {
        doAnswer(context, instance, answer, getAnyArgumentMatchers());
    }

    default <T> void doAnswer(final Object instance, final Answer<T> answer) {
        doAnswer(instance, answer, getAnyArgumentMatchers());
    }

    default void doReturnNull(final Object instance) {
        doReturnAlways(instance, null);
    }

    default void doNothing(final Context context, final Object instance, final Object... args) {
        if (MOCK_STUDENT_CODE) {
            invoke(
                context,
                Mockito.doNothing().when(instance),
                args
            );
        }
    }

    default void alwaysDoNothing(final Context context, final Object instance) {
        if (MOCK_STUDENT_CODE) {
            invoke(
                context,
                Mockito.doNothing().when(instance),
                getAnyArgumentMatchers()
            );
        }
    }

    default void doNothing(final Object instance, final Object... args) {
        doNothing(Assertions2.emptyContext(), instance, args);
    }

    default void doNothing(final Object instance) {
        doNothing(instance, getAnyArgumentMatchers());
    }
}
