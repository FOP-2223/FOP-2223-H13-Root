package h13.model.gameplay.sprites;

import h13.model.gameplay.GameState;

public class IDEnemy extends Enemy implements WithID {
    private final int id;
    /**
     * Creates a new enemy.
     *
     * @param xIndex      The enemy's X-index of the enemy grid.
     * @param yIndex      The enemy's Y-index of the enemy grid.
     * @param velocity    The enemy's velocity.
     * @param pointsWorth The amount of points the enemy is worth when it is destroyed.
     * @param gameState   The game state.
     */
    public IDEnemy(
        final int id,
        final int xIndex,
        final int yIndex,
        final double velocity,
        final int pointsWorth,
        final GameState gameState
    ) {
        super(xIndex, yIndex, velocity, pointsWorth, gameState);
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
