package h13.model.gameplay.sprites;

import h13.model.gameplay.GameState;

public class IDPlayer extends Player implements WithID {
    private final int id;

    /**
     * Creates a new player.
     *
     * @param x          The initial x-coordinate of the player.
     * @param y          The initial y-coordinate of the player.
     * @param velocity   The velocity of the player.
     * @param gameState  The game state.
     */
    public IDPlayer(
        final int id,
        final double x,
        final double y,
        final double velocity,
        final GameState gameState
    ) {
        super(x, y, velocity, gameState);
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
