package h13.model.gameplay.sprites;

import h13.model.gameplay.GameState;

public class EnemyC extends Enemy {

    private String texturePath;

    public EnemyC(int xIndex, int yIndex, double velocity, int pointsWorth, GameState gameState) {
        super(xIndex, yIndex, velocity, pointsWorth, gameState);
        final var random = new java.util.Random().nextInt(1, 3);
        // choose one randomly
        texturePath = "/h13/images/sprites/enemy" + random + ".png";
        loadTexture(texturePath);

        setX(xIndex);
        setY(yIndex);
    }

    public String getTexturePath() {
        return texturePath;
    }
}
