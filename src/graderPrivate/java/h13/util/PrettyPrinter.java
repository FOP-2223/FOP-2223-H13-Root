package h13.util;

import h13.model.gameplay.sprites.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class PrettyPrinter {

    @SuppressWarnings("rawtypes")
    private static final Map<Class<?>, Function> CONVERSION_MAP = Map.ofEntries(
        Map.entry(Enemy.class, (Function<Enemy, String>) PrettyPrinter::prettyPrintEnemy),
        Map.entry(Sprite.class, (Function<Sprite, String>) PrettyPrinter::prettyPrintSprite),
        Map.entry(IDBullet.class, (Function<IDBullet, String>) PrettyPrinter::prettyPrintIDBullet),
        Map.entry(IDEnemy.class, (Function<IDEnemy, String>) PrettyPrinter::prettyPrintIDEnemy),
        Map.entry(IDPlayer.class, (Function<IDPlayer, String>) PrettyPrinter::prettyPrintIDPlayer),
        Map.entry(Bullet.class, (Function<Sprite, String>) PrettyPrinter::prettyPrintSprite),
        Map.entry(Player.class, (Function<Sprite, String>) PrettyPrinter::prettyPrintSprite),
        Map.entry(BattleShip.class, (Function<Sprite, String>) PrettyPrinter::prettyPrintSprite)
    );

    @SuppressWarnings("unchecked")
    public static <T> String prettyPrint(final T object) {
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
            final T e = it.next();
            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(prettyPrint(e));
            if (!it.hasNext())
                return sb.append("\n]").toString();
            sb.append(',').append('\n');
        }
    }


    private static <T> String prettyPrintArray(final T object) {
        // use prettyPrintIterable
        return prettyPrintIterable(Arrays.asList((Object[]) object));
    }

    public static String prettyPrintEnemy(final Enemy enemy) {
        return String.format("{x=%s, y=%s, width=%s, height=%s}", enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
    }

    public static String prettyPrintIDEnemy(final IDEnemy enemy) {
        return String.format("{id=%s, x=%s, y=%s, width=%s, height=%s, velocity=%s, health=%s, pointsWorth=%s}", enemy.getId(), enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight(), enemy.getVelocity(), enemy.getHealth(), enemy.getPointsWorth());
    }

    public static String prettyPrintIDBullet(final IDBullet bullet) {
        return String.format("{id=%s, x=%s, y=%s, width=%s, height=%s, velocity=%s, health=%s, direction=%s}", bullet.getId(), bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight(), bullet.getVelocity(), bullet.getHealth(), bullet.getDirection());
    }

    public static String prettyPrintIDPlayer(final IDPlayer player) {
        return String.format("{id=%s, x=%s, y=%s, width=%s, height=%s, velocity=%s, health=%s, score=%s}", player.getId(), player.getX(), player.getY(), player.getWidth(), player.getHeight(), player.getVelocity(), player.getHealth(), player.getScore());
    }

    private static String prettyPrintSprite(final Sprite sprite) {
        return String.format("{x=%s, y=%s, width=%s, height=%s}", sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }
}
