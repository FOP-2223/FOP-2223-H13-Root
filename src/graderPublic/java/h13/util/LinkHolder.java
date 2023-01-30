package h13.util;

import org.tudalgo.algoutils.tutor.general.reflections.Link;

/**
 * A Wrapper that contains a {@link Link}.
 */
public interface LinkHolder {
    /**
     * Gets the {@link Link} stored in this {@link LinkHolder}.
     *
     * @return The {@link Link} stored in this {@link LinkHolder}.
     */
    Link getLink();
}
