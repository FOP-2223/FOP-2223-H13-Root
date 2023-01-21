package h13.util;

import com.google.common.base.CaseFormat;
import h13.controller.GameConstants;
import h13.model.gameplay.Direction;
import h13.model.gameplay.EnemyMovement;
import h13.model.gameplay.sprites.*;
import h13.shared.Utils;
import h13.view.gui.GameScene;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.util.Arrays;
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
            DIRERCTION_FIELD(BasicTypeLink.of(Sprite.class).getField(identical("direction"))),
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
            GETX_INDEX_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Enemy.class.getDeclaredMethod("getxIndex")
            ))),
            GETY_INDEX_METHOD(BasicMethodLink.of(Assertions.assertDoesNotThrow(
                () -> Enemy.class.getDeclaredMethod("getyIndex")
            ))),
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
            SHIP_PADING_FIELD(BasicTypeLink.of(GameConstants.class).getField(identical("SHIP_PADING"))),
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
        final var clazz = GameScene.class;

        final var output = collectClassLinks(clazz);
        System.out.println(output);

        // Copy to clipboard
        copyToClipboard(output);
    }
}
