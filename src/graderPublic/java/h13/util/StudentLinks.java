package h13.util;

import com.google.common.base.CaseFormat;
import h13.controller.GameConstants;
import h13.controller.gamelogic.EnemyController;
import h13.controller.gamelogic.GameInputHandler;
import h13.controller.gamelogic.PlayerController;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import h13.model.gameplay.EnemyMovement;
import h13.model.gameplay.GameState;
import h13.model.gameplay.sprites.*;
import h13.shared.Utils;
import h13.view.gui.GameScene;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import h13.view.gui.GameBoard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;

public class StudentLinks {
    public static class EnemyMovementLinks {
        public enum EnemyMovementFieldLink implements ClassFieldLink {
            Y_TARGET_FIELD(BasicTypeLink.of(EnemyMovement.class).getField(identical("yTarget"))),
            VELOCITY_FIELD(BasicTypeLink.of(EnemyMovement.class).getField(identical("velocity"))),
            DIRECTION_FIELD(BasicTypeLink.of(EnemyMovement.class).getField(identical("direction"))),
            ;
            private final FieldLink link;

            EnemyMovementFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum EnemyMovementMethodLink implements ClassMethodLink {
            GET_ENEMY_BOUNDS_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("getEnemyBounds"))),
            TARGET_REACHED_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("targetReached"))),
            UPDATE_POSITIONS_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("updatePositions"))),
            NEXT_MOVEMENT_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("nextMovement"))),
            NEXT_ROUND_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("nextRound"))),
            UPDATE_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("update"))),
            BOTTOM_WAS_REACHED_METHOD(BasicTypeLink.of(EnemyMovement.class).getMethod(identical("bottomWasReached"))),
            ;
            private final MethodLink link;

            EnemyMovementMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class SpriteLinks {
        public enum SpriteFieldLink implements ClassFieldLink {
            X_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("x"))),
            Y_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("y"))),
            WIDTH_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("width"))),
            HEIGHT_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("height"))),
            VELOCITY_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("velocity"))),
            HEALTH_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("health"))),
            DIRECTION_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("direction"))),
            COLOR_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("color"))),
            TEXTURE_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("texture"))),
            GAME_STATE_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("gameState"))),
            ;
            private final FieldLink link;

            SpriteFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum SpriteMethodLink implements ClassMethodLink {
            GET_X_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getX"))),
            SET_X_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("setX"))),
            GET_Y_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getY"))),
            SET_Y_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("setY"))),
            GET_WIDTH_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getWidth"))),
            GET_HEIGHT_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getHeight"))),
            GET_VELOCITY_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getVelocity"))),
            GET_HEALTH_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getHealth"))),
            SET_HEALTH_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("setHealth"))),
            GET_DIRECTION_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getDirection"))),
            SET_DIRECTION_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("setDirection"))),
            IS_DEAD_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("isDead"))),
            IS_ALIVE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("isAlive"))),
            GET_COLOR_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getColor"))),
            GET_TEXTURE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getTexture"))),
            SET_TEXTURE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("setTexture"))),
            LOAD_TEXTURE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("loadTexture"))),
            GET_GAME_STATE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getGameState"))),
            GET_BOUNDS_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("getBounds"))),
            MOVE_UP_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("moveUp"))),
            MOVE_DOWN_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("moveDown"))),
            MOVE_LEFT_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("moveLeft"))),
            MOVE_RIGHT_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("moveRight"))),
            STOP_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("stop"))),
            DAMAGE_METHOD_WITH_AMOUNT(BasicMethodLink.of(Assertions.assertDoesNotThrow(() -> Sprite.class.getDeclaredMethod("damage", int.class)))),
            DAMAGE_METHOD_WITHOUT_AMOUNT(BasicMethodLink.of(Assertions.assertDoesNotThrow(() -> Sprite.class.getDeclaredMethod("damage")))),
            DIE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("die"))),
            UPDATE_METHOD(BasicTypeLink.of(Sprite.class).getMethod(identical("update"))),
            ;

            private final MethodLink link;

            SpriteMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class BattleShipLinks {
        public enum BattleShipFieldLink implements ClassFieldLink {
            BULLET_FIELD(BasicTypeLink.of(BattleShip.class).getField(identical("bullet"))),
            ;
            private final FieldLink link;

            BattleShipFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum BattleShipMethodLink implements ClassMethodLink {
            GET_BULLET_METHOD(BasicTypeLink.of(BattleShip.class).getMethod(identical("getBullet"))),
            HAS_BULLET_METHOD(BasicTypeLink.of(BattleShip.class).getMethod(identical("hasBullet"))),
            SET_BULLET_METHOD(BasicTypeLink.of(BattleShip.class).getMethod(identical("setBullet"))),
            IS_FRIEND_METHOD(BasicTypeLink.of(BattleShip.class).getMethod(identical("isFriend"))),
            IS_ENEMY_METHOD(BasicTypeLink.of(BattleShip.class).getMethod(identical("isEnemy"))),
            SHOOT_METHOD(BasicTypeLink.of(BattleShip.class).getMethod(identical("shoot"))),
            ;

            private final MethodLink link;

            BattleShipMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class EnemyLinks {
        public enum EnemyFieldLink implements ClassFieldLink {
            X_INDEX_FIELD(BasicTypeLink.of(Enemy.class).getField(identical("xIndex"))),
            Y_INDEX_FIELD(BasicTypeLink.of(Enemy.class).getField(identical("yIndex"))),
            POINTS_WORTH_FIELD(BasicTypeLink.of(Enemy.class).getField(identical("pointsWorth"))),
            TIME_TILL_NEXT_SHOT_FIELD(BasicTypeLink.of(Enemy.class).getField(identical("timeTillNextShot"))),
            ;
            private final FieldLink link;

            EnemyFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum EnemyMethodLink implements ClassMethodLink {
            UPDATE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Enemy.class.getDeclaredMethod("update", double.class)
            ))),
            SHOOT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Enemy.class.getDeclaredMethod("shoot")
            ))),
            GET_X_INDEX_METHOD(Assertions3.assertMethodExists(
                BasicTypeLink.of(Enemy.class),
                MatcherUtils.or(
                    identical("getXIndex"),
                    identical("getxIndex")
                )
            )),
            GET_Y_INDEX_METHOD(Assertions3.assertMethodExists(
                BasicTypeLink.of(Enemy.class),
                MatcherUtils.or(
                    identical("getYIndex"),
                    identical("getyIndex")
                )
            )),
            GET_POINTS_WORTH_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Enemy.class.getDeclaredMethod("getPointsWorth")
            ))),
            ;
            private final MethodLink link;

            EnemyMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class PlayerLinks {
        public enum PlayerFieldLink implements ClassFieldLink {
            SCORE_FIELD(BasicTypeLink.of(Player.class).getField(identical("score"))),
            NAME_FIELD(BasicTypeLink.of(Player.class).getField(identical("name"))),
            KEEP_SHOOTING_FIELD(BasicTypeLink.of(Player.class).getField(identical("keepShooting"))),
            ;
            private final FieldLink link;

            PlayerFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum PlayerMethodLink implements ClassMethodLink {
            GET_NAME_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("getName")
            ))),
            UPDATE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("update", double.class)
            ))),
            SET_NAME_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("setName", String.class)
            ))),
            GET_SCORE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("getScore")
            ))),
            SET_SCORE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("setScore", int.class)
            ))),
            ADD_POINTS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("addPoints", int.class)
            ))),
            SET_KEEP_SHOOTING_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("setKeepShooting", boolean.class)
            ))),
            MOVE_DOWN_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("moveDown")
            ))),
            MOVE_UP_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("moveUp")
            ))),
            SHOOT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("shoot")
            ))),
            IS_KEEP_SHOOTING_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Player.class.getDeclaredMethod("isKeepShooting")
            ))),
            ;
            private final MethodLink link;

            PlayerMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class BulletLinks {
        public enum BulletFieldLink implements ClassFieldLink {
            OWNER_FIELD(BasicTypeLink.of(Bullet.class).getField(identical("owner"))),
            HITS_FIELD(BasicTypeLink.of(Bullet.class).getField(identical("hits"))),
            ;
            private final FieldLink link;

            BulletFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum BulletMethodLink implements ClassMethodLink {
            GET_OWNER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Bullet.class.getDeclaredMethod("getOwner")
            ))),
            HIT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Bullet.class.getDeclaredMethod("hit", BattleShip.class)
            ))),
            GET_HITS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Bullet.class.getDeclaredMethod("getHits")
            ))),
            ON_OUT_OF_BOUNDS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Bullet.class.getDeclaredMethod("onOutOfBounds", Bounds.class)
            ))),
            CAN_HIT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Bullet.class.getDeclaredMethod("canHit", BattleShip.class)
            ))),
            ;
            private final MethodLink link;

            BulletMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class GameConstantsLinks {
        public enum GameConstantsFieldLink implements ClassFieldLink {
            ORIGINAL_GAME_BOUNDS_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("ORIGINAL_GAME_BOUNDS"))),
            ASPECT_RATIO_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("ASPECT_RATIO"))),
            ENEMY_ROWS_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("ENEMY_ROWS"))),
            ENEMY_COLS_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("ENEMY_COLS"))),
            INITIAL_ENEMY_MOVEMENT_DIRECTION_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("INITIAL_ENEMY_MOVEMENT_DIRECTION"))),
            INITIAL_ENEMY_MOVEMENT_VELOCITY_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("INITIAL_ENEMY_MOVEMENT_VELOCITY"))),
            ENEMY_MOVEMENT_SPEED_INCREASE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("ENEMY_MOVEMENT_SPEED_INCREASE"))),
            SHIP_PADDING_FIELD(BasicTypeLink.of(GameConstants.class).getField(MatcherUtils.or(
                identical("SHIP_PADDING"),
                // Typo in initial student template
                identical("SHIP_PADING")
            ))),
            HORIZONTAL_ENEMY_MOVE_DISTANCE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HORIZONTAL_ENEMY_MOVE_DISTANCE"))),
            VERTICAL_ENEMY_MOVE_DISTANCE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("VERTICAL_ENEMY_MOVE_DISTANCE"))),
            HORIZONTAL_ENEMY_MOVE_SPACE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HORIZONTAL_ENEMY_MOVE_SPACE"))),
            CHUNK_SIZE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("CHUNK_SIZE"))),
            RELATIVE_SHIP_SIZE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("RELATIVE_SHIP_SIZE"))),
            SHIP_SIZE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("SHIP_SIZE"))),
            BULLET_WIDTH_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("BULLET_WIDTH"))),
            BULLET_HEIGHT_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("BULLET_HEIGHT"))),
            ENEMY_SHOOTING_PROBABILITY_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("ENEMY_SHOOTING_PROBABILITY"))),
            PLAYER_VELOCITY_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("PLAYER_VELOCITY"))),
            BULLET_VELOCITY_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("BULLET_VELOCITY"))),
            HUD_FONT_PATH_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HUD_FONT_PATH"))),
            HUD_FONT_SIZE_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HUD_FONT_SIZE"))),
            HUD_PADDING_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HUD_PADDING"))),
            HUD_HEIGHT_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HUD_HEIGHT"))),
            HUD_TEXT_COLOR_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HUD_TEXT_COLOR"))),
            HUD_FONT_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("HUD_FONT"))),
            TITLE_FONT_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("TITLE_FONT"))),
            BORDER_WIDTH_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("BORDER_WIDTH"))),
            BORDER_COLOR_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("BORDER_COLOR"))),
            ;
            private final FieldLink link;

            GameConstantsFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum GameConstantsMethodLink implements ClassMethodLink {
            ,
            ;
            private final MethodLink link;

            GameConstantsMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class UtilsLinks {
        public enum UtilsFieldLink implements ClassFieldLink {
            ,
            ;
            private final FieldLink link;

            UtilsFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum UtilsMethodLink implements ClassMethodLink {
            CLAMP_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Utils.class.getDeclaredMethod("clamp", Bounds.class)
            ))),
            GET_NEXT_POSITION_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Utils.class.getDeclaredMethod("getNextPosition", Bounds.class, double.class, Direction.class, double.class)
            ))),
            ;
            private final MethodLink link;

            UtilsMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class GameSceneLinks {
        public enum GameSceneFieldLink implements ClassFieldLink {
            ROOT_FIELD(BasicTypeLink.of(GameScene.class).getField(identical("root"))),
            GAME_BOARD_FIELD(BasicTypeLink.of(GameScene.class).getField(identical("gameBoard"))),
            GAME_CONTROLLER_FIELD(BasicTypeLink.of(GameScene.class).getField(identical("gameController"))),
            ;
            private final FieldLink link;

            GameSceneFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum GameSceneMethodLink implements ClassMethodLink {
            INIT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameScene.class.getDeclaredMethod("init")
            ))),
            INIT_GAMEBOARD_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameScene.class.getDeclaredMethod("initGameboard")
            ))),
            GET_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameScene.class.getDeclaredMethod("getController")
            ))),
            GET_GAME_BOARD_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameScene.class.getDeclaredMethod("getGameBoard")
            ))),
            SET_WIDTH_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Scene.class.getDeclaredMethod("setWidth", double.class)
            ))),
            SET_HEIGHT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Scene.class.getDeclaredMethod("setHeight", double.class)
            ))),
            ;
            private final MethodLink link;

            GameSceneMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class GameControllerLinks {
        public enum GameControllerFieldLink implements ClassFieldLink {
            GAME_STATE_FIELD(BasicTypeLink.of(GameController.class).getField(identical("gameState"))),
            GAME_SCENE_FIELD(BasicTypeLink.of(GameController.class).getField(identical("gameScene"))),
            LAST_UPDATE_FIELD(BasicTypeLink.of(GameController.class).getField(identical("lastUpdate"))),
            PAUSED_FIELD(BasicTypeLink.of(GameController.class).getField(identical("paused"))),
            PLAYER_CONTROLLER_FIELD(BasicTypeLink.of(GameController.class).getField(identical("playerController"))),
            ENEMY_CONTROLLER_FIELD(BasicTypeLink.of(GameController.class).getField(identical("enemyController"))),
            GAME_INPUT_HANDLER_FIELD(BasicTypeLink.of(GameController.class).getField(identical("gameInputHandler"))),
            GAME_LOOP_FIELD(BasicTypeLink.of(GameController.class).getField(identical("gameLoop"))),
            ;
            private final FieldLink link;

            GameControllerFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum GameControllerMethodLink implements ClassMethodLink {
            UPDATE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("update", double.class)
            ))),
            INIT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("init")
            ))),
            RESUME_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("resume")
            ))),
            RESET_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("reset")
            ))),
            GET_GAME_SCENE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getGameScene")
            ))),
            GET_GAME_BOARD_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getGameBoard")
            ))),
            INIT_STAGE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("initStage", Stage.class)
            ))),
            SET_GAME_INPUT_HANDLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("setGameInputHandler", GameInputHandler.class)
            ))),
            SET_PLAYER_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("setPlayerController", PlayerController.class)
            ))),
            SET_ENEMY_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("setEnemyController", EnemyController.class)
            ))),
            HANDLE_KEYBOARD_INPUTS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("handleKeyboardInputs")
            ))),
            GET_GAME_LOOP_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getGameLoop")
            ))),
            GET_ENEMY_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getEnemyController")
            ))),
            PAUSE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("pause")
            ))),
            GET_GAME_STATE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getGameState")
            ))),
            GET_GAME_INPUT_HANDLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getGameInputHandler")
            ))),
            UPDATE_OTHERS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("updateOthers", double.class)
            ))),
            DO_COLLISIONS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("doCollisions")
            ))),
            UPDATE_POINTS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("updatePoints", List.class)
            ))),
            LOSE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("lose")
            ))),
            REFILL_ENEMIES_IF_NECESSARY_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("refillEnemiesIfNecessary")
            ))),
            GET_PLAYER_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getPlayerController")
            ))),
            GET_PLAYER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getPlayer")
            ))),
            IS_PAUSED_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("isPaused")
            ))),
            GET_TITLE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameController.class.getDeclaredMethod("getTitle")
            ))),
            ;
            private final MethodLink link;

            GameControllerMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class PlayerControllerLinks {
        public enum PlayerControllerFieldLink implements ClassFieldLink {
            PLAYER_FIELD(BasicTypeLink.of(PlayerController.class).getField(identical("player"))),
            GAME_CONTROLLER_FIELD(BasicTypeLink.of(PlayerController.class).getField(identical("gameController"))),
            ;
            private final FieldLink link;

            PlayerControllerFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum PlayerControllerMethodLink implements ClassMethodLink {
            GET_GAME_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> PlayerController.class.getDeclaredMethod("getGameController")
            ))),
            HANDLE_KEYBOARD_INPUTS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> PlayerController.class.getDeclaredMethod("handleKeyboardInputs")
            ))),
            GET_PLAYER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> PlayerController.class.getDeclaredMethod("getPlayer")
            ))),
            PLAYER_KEY_ACTION_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> PlayerController.class.getDeclaredMethod("playerKeyAction", KeyEvent.class)
            ))),
            ;
            private final MethodLink link;

            PlayerControllerMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class EnemyControllerLinks {
        public enum EnemyControllerFieldLink implements ClassFieldLink {
            GAME_CONTROLLER_FIELD(BasicTypeLink.of(EnemyController.class).getField(identical("gameController"))),
            ;
            private final FieldLink link;

            EnemyControllerFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum EnemyControllerMethodLink implements ClassMethodLink {
            NEXT_LEVEL_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> EnemyController.class.getDeclaredMethod("nextLevel")
            ))),
            GET_GAME_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> EnemyController.class.getDeclaredMethod("getGameController")
            ))),
            IS_DEFEATED_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> EnemyController.class.getDeclaredMethod("isDefeated")
            ))),
            ;
            private final MethodLink link;

            EnemyControllerMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class GameInputHandlerLinks {
        public enum GameInputHandlerFieldLink implements ClassFieldLink {
            KEYS_PRESSED_FIELD(BasicTypeLink.of(GameInputHandler.class).getField(identical("keysPressed"))),
            ON_KEY_PRESSED_FIELD(BasicTypeLink.of(GameInputHandler.class).getField(identical("onKeyPressed"))),
            ON_KEY_RELEASED_FIELD(BasicTypeLink.of(GameInputHandler.class).getField(identical("onKeyReleased"))),
            ON_KEY_TYPED_FIELD(BasicTypeLink.of(GameInputHandler.class).getField(identical("onKeyTyped"))),
            ;
            private final FieldLink link;

            GameInputHandlerFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum GameInputHandlerMethodLink implements ClassMethodLink {
            HANDLE_KEYBOARD_INPUTS_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameInputHandler.class.getDeclaredMethod("handleKeyboardInputs", GameScene.class)
            ))),
            GET_KEYS_PRESSED_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameInputHandler.class.getDeclaredMethod("getKeysPressed")
            ))),
            ADD_ON_KEY_PRESSED_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameInputHandler.class.getDeclaredMethod("addOnKeyPressed", EventHandler.class)
            ))),
            ADD_ON_KEY_RELEASED_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameInputHandler.class.getDeclaredMethod("addOnKeyReleased", EventHandler.class)
            ))),
            ADD_ON_KEY_TYPED_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameInputHandler.class.getDeclaredMethod("addOnKeyTyped", EventHandler.class)
            ))),
            ;
            private final MethodLink link;

            GameInputHandlerMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class GameBoardLinks {
        public enum GameBoardFieldLink implements ClassFieldLink {
            GAME_SCENE_FIELD(BasicTypeLink.of(GameBoard.class).getField(identical("gameScene"))),
            BACKGROUND_IMAGE_FIELD(BasicTypeLink.of(GameBoard.class).getField(identical("backgroundImage"))),
            ;
            private final FieldLink link;

            GameBoardFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum GameBoardMethodLink implements ClassMethodLink {
            UPDATE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("update", double.class)
            ))),
            GET_SCALE_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("getScale")
            ))),
            DRAW_BACKGROUND_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("drawBackground", GraphicsContext.class)
            ))),
            DRAW_SPRITES_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("drawSprites", GraphicsContext.class)
            ))),
            DRAW_H_U_D_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("drawHUD", GraphicsContext.class)
            ))),
            DRAW_BORDER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("drawBorder", GraphicsContext.class)
            ))),
            GET_GAME_CONTROLLER_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameBoard.class.getDeclaredMethod("getGameController")
            ))),
            ;
            private final MethodLink link;

            GameBoardMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    public static class GameStateLinks {
        public enum GameStateFieldLink implements ClassFieldLink {
            SPRITES_FIELD(BasicTypeLink.of(GameState.class).getField(identical("sprites"))),
            TO_ADD_FIELD(BasicTypeLink.of(GameState.class).getField(identical("toAdd"))),
            ENEMY_MOVEMENT_FIELD(BasicTypeLink.of(GameState.class).getField(identical("enemyMovement"))),
            ;
            private final FieldLink link;

            GameStateFieldLink(final FieldLink link) {
                this.link = link;
            }

            @Override
            public FieldLink getLink() {
                return link;
            }
        }

        public enum GameStateMethodLink implements ClassMethodLink {
            GET_SPRITES_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameState.class.getDeclaredMethod("getSprites")
            ))),
            GET_TO_ADD_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameState.class.getDeclaredMethod("getToAdd")
            ))),
            GET_ENEMIES_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameState.class.getDeclaredMethod("getEnemies")
            ))),
            GET_ALIVE_ENEMIES_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameState.class.getDeclaredMethod("getAliveEnemies")
            ))),
            GET_ENEMY_MOVEMENT_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> GameState.class.getDeclaredMethod("getEnemyMovement")
            ))),
            ;
            private final MethodLink link;

            GameStateMethodLink(final MethodLink link) {
                this.link = link;
            }

            @Override
            public MethodLink getLink() {
                return link;
            }
        }
    }

    @Test
    public void testLinks() {
        // get all the fields and methods in the class
        Arrays.stream(getClass().getDeclaredClasses()).forEach(subclass -> {
            Arrays.stream(subclass.getDeclaredClasses()).forEach(enums -> {
                // Get enum constants
                Arrays.stream(enums.getEnumConstants()).forEach(link -> {
                    // Test the link
                    if (link instanceof ClassMethodLink cfl) {
                        Assertions.assertNotNull(cfl.getLink().reflection());
                    } else if (link instanceof ClassFieldLink cfl) {
                        Assertions.assertNotNull(cfl.getLink().reflection());
                    } else {
                        Assertions.fail("Unknown link type: " + link.getClass());
                    }
                });
            });
        });
    }

    public String collectClassLinks(final Class<?> studentClass) {
        return String.format("public static class %sLinks {%n", studentClass.getSimpleName()) +
            // Field Links
            String.format(
                """
                        public enum %sFieldLink implements ClassFieldLink {
                    %s
                                ;
                            private final FieldLink link;
                            %sFieldLink(final FieldLink link) {
                                this.link = link;
                            }
                            @Override
                            public FieldLink getLink() {
                                return link;
                            }
                        }
                    """,
                studentClass.getSimpleName(),
                Arrays.stream(studentClass.getDeclaredFields())
                    .map(field -> String.format(
                        "\t\t%s_FIELD(BasicTypeLink.of(%s.class).getField(identical(\"%s\")))",
                        // Convert pascal case to snake case, if needed
                        field.getName().contains("_") ? CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, field.getName()) : CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName()),
                        studentClass.getSimpleName(),
                        field.getName()
                    ))
                    .collect(Collectors.joining(",\n", "", ",")),
                studentClass.getSimpleName()
            ) +
            // Method Links
            String.format(
                """
                        public enum %sMethodLink implements ClassMethodLink {
                    %s
                                ;
                            private final MethodLink link;
                            %sMethodLink(final MethodLink link) {
                                this.link = link;
                            }
                            @Override
                            public MethodLink getLink() {
                                return link;
                            }
                        }
                    """,
                studentClass.getSimpleName(),
                Arrays.stream(studentClass.getDeclaredMethods())
                    .filter(
                        // filter out lambda methods
                        method -> !method.getName().contains("lambda$")
                    )
                    .map(method -> String.format(
                        """
                            \t\t%s_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                            \t\t\t() -> %s.class.getDeclaredMethod("%s"%s)
                            \t\t)))""",
                        // Convert pascal case to snake case
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, method.getName()),
                        studentClass.getSimpleName(),
                        method.getName(),
                        Arrays.stream(method.getParameterTypes())
                            .map(Class::getSimpleName)
                            .map(name -> String.format(", %s.class", name))
                            .collect(Collectors.joining())
                    ))
                    .collect(Collectors.joining(",\n", "", ",")),
                studentClass.getSimpleName()
            ) +
            "}";
    }

    static void copyToClipboard(final String text) {
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .setContents(new java.awt.datatransfer.StringSelection(text), null);
    }

    @Test
    public void collectStudentLinks() {
        final var clazz = GameState.class;

        final var output = collectClassLinks(clazz);
        System.out.println(output);

        // Copy to clipboard
        copyToClipboard(output);
    }
}
