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

import static h13.rubric.RubricUtils.criterion;
import static h13.rubric.RubricUtils.manualGrader;

public class H13_RubricProvider implements RubricProvider {
    public static final Rubric RUBRIC = Rubric.builder()
        .title("H13 | Space Invaders")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H3 | Spiellogik (Controller)")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H3.1 | Klasse GameController")
                        .addChildCriteria(
                            criterion(
                                "Die Methode handleKeyboardInputs() ist vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsFullScreenF11", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeLose", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeResume", JsonParameterSet.class))
                                )
                            ),
                            criterion(
                                "Die Methode lose() ist vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseHighscore", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseReset", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseMainMenu", JsonParameterSet.class))
                                )
                            ),
                            criterion(
                                "Die getesteten Methoden sind vollständig korrekt.",
                                JUnitTestRef.and(
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsFullScreenF11", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeLose", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeResume", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseHighscore", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseReset", JsonParameterSet.class)),
                                    JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseMainMenu", JsonParameterSet.class))
                                )
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
