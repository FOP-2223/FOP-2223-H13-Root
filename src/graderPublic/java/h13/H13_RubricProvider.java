package h13;

import h13.controller.gamelogic.EnemyControllerTest;
import h13.controller.gamelogic.PlayerControllerTest;
import h13.controller.scene.game.GameControllerTest;
import h13.json.JsonParameterSet;
import h13.model.gameplay.Direction;
import h13.model.gameplay.EnemyMovementTest;
import h13.model.gameplay.sprites.*;
import h13.shared.UtilsTest;
import h13.view.gui.GameBoardTest;
import h13.view.gui.GameSceneTest;
import h13.view.gui.SpriteRendererTest;
import javafx.geometry.Bounds;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.tudalgo.algoutils.transform.AccessTransformer;

import static h13.rubric.RubricUtils.*;

public class H13_RubricProvider implements RubricProvider {
    public static final Rubric RUBRIC = Rubric.builder()
        .title("H13 | Space Invaders")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H1 | Modellierung der Sprites (Modell)")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H1.1 | Klasse Sprite")
                        .addChildCriteria(
                            criterion(
                                "Die Methoden damage(), die() und isDead() sind vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> SpriteTest.IsDead.class.getDeclaredMethod("isDead_alive", int.class)),
                                    JUnitTestRef.ofMethod(() -> SpriteTest.IsDead.class.getDeclaredMethod("isDead_dead", int.class)),
                                    JUnitTestRef.ofMethod(() -> SpriteTest.class.getDeclaredMethod("damage", int.class, int.class)),
                                    JUnitTestRef.ofMethod(() -> SpriteTest.class.getDeclaredMethod("die", int.class))
                                )
                            ),
                            criterion(
                                "Die Methode clamp() ist vollständig korrekt. [public]",
                                JUnitTestRef.ofMethod(() -> UtilsTest.class.getDeclaredMethod("clamp", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode getNextPosition() ist vollständig korrekt. [public]",
                                JUnitTestRef.ofMethod(() -> UtilsTest.class.getDeclaredMethod("getNextPosition", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode update() führt die Bewegung des Sprites korrekt durch.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> SpriteTest.class.getDeclaredMethod("update_inside")),
                                    JUnitTestRef.ofMethod(() -> SpriteTest.class.getDeclaredMethod("update_outside"))
                                )
                            )
                        )
                        .minPoints(0)
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.2 | Klasse Bullet")
                        .addChildCriteria(
                            criterion(
                                "Die Methode canHit() ist vollständig korrekt. [public]",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> BulletTest.class.getDeclaredMethod("canHit", boolean.class, boolean.class, int.class, int.class)),
                                    JUnitTestRef.ofMethod(() -> BulletTest.class.getDeclaredMethod("canHit_MultiHit", int.class))
                                )
                            ),
                            criterion(
                                "Die Methode hit() ist vollständig korrekt. [public]",
                                JUnitTestRef.ofMethod(() -> BulletTest.class.getDeclaredMethod("hit"))
                            ),
                            criterion(
                                "Die Methode update() ist vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> BulletTest.class.getDeclaredMethod("update", int.class)),
                                    JUnitTestRef.ofMethod(() -> BulletTest.class.getDeclaredMethod("updateBasic"))
                                )

                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.3 | Klasse BattleShip")
                        .addChildCriteria(
                            criterion(
                                "Die Methode shoot() erzeugt korrekt eine neue Kugel.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> BattleShipTest.class.getDeclaredMethod("shoot_hasNoBullet", Direction.class)),
                                    JUnitTestRef.ofMethod(() -> BattleShipTest.class.getDeclaredMethod("shoot_hasBullet", Direction.class))
                                )
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode shoot() ist vollständig korrekt.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build(),
                            criterion(
                                "Die Methode isFriend() ist vollständig korrekt. [public]",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> BattleShipTest.class.getDeclaredMethod("isFriend", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> BattleShipTest.class.getDeclaredMethod("isFriend", BattleShip.class, BattleShip.class))
                                )
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.4 | Klasse Enemy")
                        .addChildCriteria(
                            criterion(
                                "Ein Enemy feuert beim Aufruf von update() mit der korrekten Wahrscheinlichkeit eine Kugel (ohne ein delay zu beachten).",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateShootCalledWithMaxProbability")),
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateWithMinProbability"))
                                )
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode update() ist vollständig korrekt.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build()
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.5 | Klasse Player")
                        .addChildCriteria(
                            criterion(
                                "Die Methode update() ist vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> PlayerTest.class.getDeclaredMethod("testUpdateWithKeepShooting")),
                                    JUnitTestRef.ofMethod(() -> PlayerTest.class.getDeclaredMethod("testUpdateWithoutKeepShooting"))
                                )
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.6 | Klasse EnemyMovement")
                        .addChildCriteria(
                            criterion(
                                "Die Methode getEnemyBounds() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testGetEnemyBounds", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode bottomWasReached() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testBottomWasReached", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode targetReached() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testTargetReached", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode nextMovement() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testNextMovement", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode updatePositions() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testUpdatePositions", double.class, double.class))
                            ),
                            criterion(
                                "Die Methode update() bewegt die Gegner korrekt in die gewünschte Richtung.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testUpdateRegular", JsonParameterSet.class))
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode update() ist vollständig korrekt.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build()
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H2 | Game Scene und Rendering (View)")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H2.1 | Klasse GameScene")
                        .addChildCriteria(
                            criterion(
                                "Die Größe des GameBoards ist korrekt beim originalen Seitenverhältnis.",
                                JUnitTestRef.ofMethod(() -> GameSceneTest.class.getDeclaredMethod("testSizeCorrectWithOriginalAspectRatio", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Größe des GameBoards ist stets vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameSceneTest.class.getDeclaredMethod("testSizeCorrectWithDifferentAspectRatio", JsonParameterSet.class))
                            ),
                            criterion(
                                "Das GameBoard wird korrekt zentriert.",
                                JUnitTestRef.ofMethod(() -> GameSceneTest.class.getDeclaredMethod("testCentering", JsonParameterSet.class))
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode initGameboard() ist vollständig korrekt.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build()
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2 | Klasse SpriteRenderer")
                        .addChildCriteria(
                            criterion(
                                "Die Methode renderSprite() funktioniert korrekt für Sprites ohne Textur. [public]",
                                JUnitTestRef.ofMethod(() -> SpriteRendererTest.class.getDeclaredMethod("testRenderSprite_NoTexture", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode renderSprite() funktioniert korrekt für Sprites mit Textur. [public]",
                                JUnitTestRef.ofMethod(() -> SpriteRendererTest.class.getDeclaredMethod("testRenderSprite_Texture", JsonParameterSet.class))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.3 | Klasse GameBoard")
                        .addChildCriteria(
                            criterion(
                                "Die Methode drawBackground() ist vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> GameBoardTest.class.getDeclaredMethod("testDrawBackground_NoImage", int.class)),
                                    JUnitTestRef.ofMethod(() -> GameBoardTest.class.getDeclaredMethod("testDrawBackground_Image", String.class, String.class))
                                )
                            ),
                            criterion(
                                "Die Methode drawSprites() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameBoardTest.class.getDeclaredMethod("testDrawSprites", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode drawHUD() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameBoardTest.class.getDeclaredMethod("testDrawHUD", int.class, int.class))
                            ),
                            criterion(
                                "Die Methode drawBorder() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameBoardTest.class.getDeclaredMethod("testDrawBorder", String.class, int.class))
                            )
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H3 | Spiellogik (Controller)")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H3.1 | Klasse GameController")
                        .addChildCriteria(
                            criterion(
                                "Die Methode doCollisions() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testDoCollisions", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode updatePoints() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testUpdatePoints", JsonParameterSet.class))
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode handleKeyboardInputs() ist vollständig korrekt.")
                                .minPoints(0)
                                .maxPoints(1)
                                .grader(
                                    manualGrader(1)
                                )
                                .build(),
                            Criterion.builder()
                                .shortDescription("Die Methode lose() ist vollständig korrekt.")
                                .minPoints(0)
                                .maxPoints(1)
                                .grader(
                                    manualGrader(1)
                                )
                                .build(),
                            Criterion.builder()
                                .shortDescription("Die Klasse GameController ist vollständig korrekt.")
                                .minPoints(0)
                                .maxPoints(1)
                                .grader(
                                    manualGrader(1)
                                )
                                .build()
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.2 | Klasse PlayerController")
                        .addChildCriteria(
                            criterion(
                                "Die Methode playerKeyAction() ist vollständig korrekt, wenn nur eine Taste gleichzeitig gedrückt wird.",
                                JUnitTestRef.ofMethod(() -> PlayerControllerTest.class.getDeclaredMethod("testPlayerKeyActionSingleKey", JsonParameterSet.class))
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode playerKeyAction() ist vollständig korrekt, auch wenn mehrere Tasten gleichzeitig gedrückt werden.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build()
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.2 | Klasse EnemyController")
                        .addChildCriteria(
                            Criterion.builder()
                                .shortDescription("Die Methode isDefeated() ist vollständig korrekt.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build(),
                            Criterion.builder()
                                .shortDescription("Die Klasse EnemyController ist vollständig korrekt.")
                                .grader(graderPrivateOnly())
                                .minPoints(0)
                                .maxPoints(1)
                                .build()
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H4 | Einstellungsmenü")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("Die geforderten Einstellungen sind vollständig korrekt implementiert.")
                        .minPoints(0)
                        .maxPoints(3)
                        .grader(
                            manualGrader(3)
                        )
                        .build()
                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(final RubricConfiguration configuration) {
        configuration.addTransformer(new AccessTransformer());
    }
}
