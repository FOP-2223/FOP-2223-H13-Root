package h13.model.gameplay.sprites;

public class MathRandomTester {
    private static boolean doReplaceRandom = false;
    private static double replaceRandomValue = 0;

    /**
     * Intercepts {@link Math#random()} and replaces it with a fixed value for testing purposes.
     * @return The value to replace {@link Math#random()} with.
     * @see Math#random()
     * @see #setReplaceRandomValue(double)
     * @see #setDoReplaceRandom(boolean)
     */
    public static double random() {
        System.out.println("Random intercepted!");
        if (doReplaceRandom) {
            return replaceRandomValue;
        } else {
            return Math.random();
        }
    }

    /**
     * Sets the value to replace {@link Math#random()} with.
     * @param value The value to replace {@link Math#random()} with.
     * @see Math#random()
     * @see #random()
     * @see #setDoReplaceRandom(boolean)
     */
    public static void setReplaceRandomValue(final double value) {
        replaceRandomValue = value;
    }

    /**
     * Sets whether to replace {@link Math#random()} with a fixed value for testing purposes.
     * @param doReplace Whether to replace {@link Math#random()} with a fixed value for testing purposes.
     * @see Math#random()
     * @see #random()
     * @see #setReplaceRandomValue(double)
     */
    public static void setDoReplaceRandom(final boolean doReplace) {
        doReplaceRandom = doReplace;
    }

    public static void replaceRandom(final double value) {
        doReplaceRandom = true;
        replaceRandomValue = value;
    }

    public static void resetRandom() {
        doReplaceRandom = false;
    }

    public static void withRandom(final double value, final Runnable runnable) {
        replaceRandom(value);
        runnable.run();
        resetRandom();
    }
}
