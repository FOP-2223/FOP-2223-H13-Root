package h13;

import h13.json.JsonParameterSet;
import h13.model.gameplay.EnemyMovementTest;
import h13.model.gameplay.sprites.EnemyTest;
import h13.model.gameplay.sprites.PlayerTest;
import h13.view.gui.GameSceneTest;
import org.sourcegrade.jagr.api.rubric.*;
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
                                null
                            ),
                            criterion(
                                "Die Methode clamp() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode getNewPosition() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode update() führt die Bewegung des Sprites korrekt durch.",
                                null
                            ),
                            criterion(
                                "Die Verbindliche Anforderung der Methode update() wurde verletzt.",
                                null,
                                -1
                            )
                        )
                        .minPoints(0)
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.2 | Klasse Bullet")
                        .addChildCriteria(
                            criterion(
                                "Die Methode canHit() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode hit() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode update() ist vollständig korrekt.",
                                null
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.3 | Klasse BattleShip")
                        .addChildCriteria(
                            criterion(
                                "Die Methode shoot() erzeugt korrekt eine neue Kugel.",
                                null
                            ),
                            criterion(
                                "Die Methode shoot() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode isFriend() ist vollständig korrekt.",
                                null
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
                            criterion(
                                "Die Methode update() ist vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateShootCalledWithMaxProbability")),
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateWithMinProbability")),
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateWithDelay")),
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateWithDelayAndMinProbability")),
                                    JUnitTestRef.ofMethod(() -> EnemyTest.class.getDeclaredMethod("testUpdateWithFiftyPercentProbability"))
                                )
                            )
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
                            criterion(
                                "Die Methode update() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> EnemyMovementTest.class.getDeclaredMethod("testUpdateCompletelyCorrect", JsonParameterSet.class))
                            )
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
                                null
                            ),
                            criterion(
                                "Die Methode initGameboard() ist vollständig korrekt.",
                                null
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2 | Klasse SpriteRenderer")
                        .addChildCriteria(
                            criterion(
                                "Die Methode renderSprite() funktioniert korrekt für Sprites ohne Textur.",
                                null
                            ),
                            criterion(
                                "Die Methode renderSprite() funktioniert korrekt für Sprites mit Textur.",
                                null
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.3 | Klasse GameBoard")
                        .addChildCriteria(
                            criterion(
                                "Die Methode drawBackground() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode drawSprites() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode drawHUD() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode drawBorder() ist vollständig korrekt.",
                                null
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
                                null
                            ),
                            criterion(
                                "Die Methode updatePoints() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Methode handleKeyboardInputs() ist vollständig korrekt.",
                                null
                            ),
                            Criterion.builder()
                                .shortDescription("Die Methode lose() ist vollständig korrekt.")
                                .minPoints(0)
                                .maxPoints(1)
                                .grader(
                                    manualGrader(1)
                                )
                                .build(),
                            criterion(
                                "Die Klasse GameController ist vollständig korrekt.",
                                null
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.2 | Klasse PlayerController")
                        .addChildCriteria(
                            criterion(
                                "Die Methode playerKeyAction() ist vollständig korrekt, wenn nur eine Taste gleichzeitig gedrückt wird.",
                                null
                            ),
                            criterion(
                                "Die Methode playerKeyAction() ist vollständig korrekt, auch wenn mehrere Tasten gleichzeitig gedrückt werden.",
                                null
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.2 | Klasse EnemyController")
                        .addChildCriteria(
                            criterion(
                                "Die Methode isDefeated() ist vollständig korrekt.",
                                null
                            ),
                            criterion(
                                "Die Klasse EnemyController ist vollständig korrekt.",
                                null
                            )
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
