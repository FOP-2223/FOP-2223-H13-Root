package h13.util;

import org.tudalgo.algoutils.tutor.general.match.Matcher;

import java.util.function.Predicate;

/**
 * Utils class for {@linkplain Matcher object matchers}.
 */
public class MatcherUtils {
    /**
     * <p>Returns a {@link Predicate} returning true if the given matcher matches the given object.</p>
     *
     * @param matcher the matcher
     * @param <T>     the type of the object to match
     * @return the predicate
     */
    public static <T> Predicate<T> toPredicate(final Matcher<T> matcher) {
        return (o) -> matcher.match(o).matched();
    }

    /**
     * <p>Returns a {@link Matcher} matching the logical negation of the given matcher.</p>
     *
     * @param matcher the matcher
     * @param <T>     the type of the object to match
     * @return the negated matcher
     */
    public static <T> Matcher<T> not(final Matcher<T> matcher) {
        return Matcher.of(toPredicate(matcher).negate(), matcher.object());
    }

    /**
     * <p>Returns a {@link Matcher} matching the logical conjunction of the given matchers.</p>
     *
     * @param matcher1 the first matcher
     * @param matcher2 the second matcher
     * @param <T>      the type of the object to match
     * @return the conjunction matcher
     */
    public static <T> Matcher<T> and(final Matcher<T> matcher1, final Matcher<T> matcher2) {
        return Matcher.of(toPredicate(matcher1).and(toPredicate(matcher2)), matcher1.object());
    }

    /**
     * <p>Returns a {@link Matcher} matching the logical disjunction of the given matchers.</p>
     *
     * @param matcher1 the first matcher
     * @param matcher2 the second matcher
     * @param <T>      the type of the object to match
     * @return the disjunction matcher
     */
    public static <T> Matcher<T> or(final Matcher<T> matcher1, final Matcher<T> matcher2) {
        return Matcher.of(toPredicate(matcher1).or(toPredicate(matcher2)), matcher1.object());
    }
}
