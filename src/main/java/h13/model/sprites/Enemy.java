package h13.model.sprites;

import h13.controller.EnemyController;
import h13.controller.GameController;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;

import static h13.model.GameConstants.*;

public class Enemy extends BattleShip {
    private double timeTillNextShot = 2; // 2 seconds
    private final int xIndex;
    private final int yIndex;


    public Enemy(int xIndex, int yIndex, double velocity, GameController gameController) {
        super(
            0,
            0,
            velocity,
            Color.YELLOW,
            1,
            gameController);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
//        moveRight();


        // act
//        TranslateTransition movement = new TranslateTransition();
//        movement.setNode(this);
//        movement.setDuration(Duration.seconds(1));
//        movement.setByX(HORIZONTAL_ENEMY_MOVE_SPACE * getGameBoard().getMaxWidth());
//        movement.setCycleCount(TranslateTransition.INDEFINITE);
//        movement.setAutoReverse(true);
////                System.out.println("movement.byXProperty().get(): " + movement.byXProperty().get());
////                System.out.println(getGameBoard().widthProperty().get());
//        movement.play();
    }

    @Override
    protected void gameTick(GameTickParameters tick) {
//        if (coordinatesInBounds(tick.newX(), tick.newY(), getGameBoard().getBorder().getInsets().getLeft())) {
//        super.gameTick(tick);

//        } else {
        setX(tick.newX());
        setY(tick.newY());
        if (health <= 0) {
            setVisible(false);
            getGameBoard().getChildren().remove(this);
//            tick.movementTimer.stop();
        }
//        if (!coordinatesInBounds(tick.newX(), tick.newY(), getGameBoard().getBorder().getInsets().getLeft())) {
//            System.out.println("Enemy out of bounds");
//            if (tick.newX() < getGameBoard().getBorder().getInsets().getLeft()) {
//                getEnemyController().horizontalMovementDirectionProperty().set(HorizontalDirection.RIGHT);
//            } else {
//                getEnemyController().horizontalMovementDirectionProperty().set(HorizontalDirection.LEFT);
//            }
//            System.out.println("new Movemt Direction: " + getEnemyController().horizontalMovementDirectionProperty().get());
//        }

//        if (getEnemyController().getHorizontalMovementDirection() == HorizontalDirection.LEFT) {
//            setVelocityX(-getVelocity() * getGameBoard().getWidth());
//        } else {
//            setVelocityX(getVelocity() * getGameBoard().getWidth());
//        }

//        System.out.println(getVelocityX());
//        System.out.println(getEnemyController().getHorizontalMovementDirection());
        var insets = getGameBoard().getBorder().getInsets();
        var horizontalSpace = getGameBoard().getWidth() - insets.getLeft() - insets.getRight();
        var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_DISTANCE);
        var chunkSize = horizontalEnemySpace / ENEMY_COLS;
        var padding = chunkSize / 2 - getWidth() / 2;

        timeTillNextShot -= tick.elapsedTime();
        if (timeTillNextShot <= 0) {
            // Shoot at random intervals
            if (Math.random() < ENEMY_SHOOTING_PROBABILITY) {
                shoot();
                timeTillNextShot = 2;
            }
        }
    }

    public void shoot() {
        shoot(VerticalDirection.DOWN);
    }

    public EnemyController getEnemyController() {
        return getGameController().getEnemyController();
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }
}
