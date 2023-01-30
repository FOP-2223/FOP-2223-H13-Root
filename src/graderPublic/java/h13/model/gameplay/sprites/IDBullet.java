package h13.model.gameplay.sprites;

import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;

public class IDBullet extends Bullet implements WithID {
    private final int id;

    /**
     * Creates a new Bullet.
     *
     * @param x         The initial x-coordinate of the Bullet.
     * @param y         The initial y-coordinate of the Bullet.
     * @param gameState The game state.
     * @param owner     The owner of the Bullet.
     * @param direction The direction the Bullet is travelling towards.
     */
    public IDBullet(
        final int id,
        final double x,
        final double y,
        final GameState gameState,
        final BattleShip owner,
        final Direction direction
    ) {
        super(x, y, gameState, owner, direction);
        this.id = id;
    }


    @Override
    public int getId() {
        return id;
    }
}
