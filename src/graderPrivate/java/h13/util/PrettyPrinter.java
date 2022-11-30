package h13.util;

import h13.model.gameplay.sprites.Enemy;
import org.tudalgo.algoutils.tutor.general.conversion.AbstractArrayNodeConversion;
import org.tudalgo.algoutils.tutor.general.conversion.ArrayConverter;

import java.util.*;
import java.util.function.Function;

public class PrettyPrinter {

    private static final Map<Class<?>, Function> CONVERSION_MAP = Map.ofEntries(
        Map.entry(Enemy.class, (Function<Enemy, String>) PrettyPrinter::prettyPrintEnemy)
    );

    public static <T> String prettyPrint(T object) {
        if (object == null) {
            return "null";
        }
        if (CONVERSION_MAP.containsKey(object.getClass())) {
            return (String) CONVERSION_MAP.get(object.getClass()).apply(object);
        }
        // array
        if (object.getClass().isArray()) {
            return prettyPrintArray(object);
        }
        // iterable
        if (object instanceof Iterable) {
            return prettyPrintIterable((Iterable<?>) object);
        }
        return object.toString();
    }


    public static <T> String prettyPrintIterable(final Iterable<T> iterable) {
        final Iterator<T> it = iterable.iterator();
        if (!it.hasNext())
            return "[]";

        final StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (; ; ) {
            T e = it.next();
            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;"+prettyPrint(e));
            if (!it.hasNext())
                return sb.append("\n]").toString();
            sb.append(',').append('\n');
        }
    }


    private static <T> String prettyPrintArray(final T object) {
        // use prettyPrintIterable
        return prettyPrintIterable(Arrays.asList((Object[]) object));
    }

    public static String prettyPrintEnemy(Enemy enemy) {
        return String.format("[x=%s, y=%s, width=%s, height=%s]", enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
    }
}
