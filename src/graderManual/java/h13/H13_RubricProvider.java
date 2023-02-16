package h13;

import h13.controller.gamelogic.EnemyControllerTest;
import h13.controller.gamelogic.PlayerControllerTest;
import h13.controller.scene.game.GameControllerTest;
import h13.json.JsonParameterSet;
import h13.model.gameplay.Direction;
import h13.model.gameplay.EnemyMovementTest;
import h13.model.gameplay.sprites.*;
import h13.shared.JFXUtils;
import h13.shared.UtilsTest;
import h13.view.gui.GameBoardTest;
import h13.view.gui.GameSceneTest;
import h13.view.gui.SettingsSceneTest;
import h13.view.gui.SpriteRendererTest;
import javafx.geometry.Bounds;
import org.sourcegrade.jagr.api.rubric.*;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.tudalgo.algoutils.transform.AccessTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static h13.rubric.RubricUtils.*;

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
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsFullScreenF11", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeLose", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeResume", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode lose() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseHighscore", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseReset", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseMainMenu", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die getesteten Methoden sind vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsFullScreenF11", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeLose", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testHandleKeyboardInputsEscapeResume", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseHighscore", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseReset", JsonParameterSet.class)),
                                JUnitTestRef.ofMethod(() -> GameControllerTest.class.getDeclaredMethod("testLoseMainMenu", JsonParameterSet.class))
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
                        .minPoints(null)
                        .maxPoints(3)
                        .grader((cycle, criterion) -> {
                                    final var fakeCriterion = Criterion.builder()
                                        .shortDescription("egal")
                                        .minPoints(0)
                                        .maxPoints(1)
                                        .build();
                                    var graders = List.of(
                                        testAwareGrader(JUnitTestRef.ofMethod(() -> SettingsSceneTest.class.getDeclaredMethod("testSettingsSceneInstantShooting"))),
                                        testAwareGrader(JUnitTestRef.ofMethod(() -> SettingsSceneTest.class.getDeclaredMethod("testSettingsSceneEnemyShootingDelay"))),
                                        testAwareGrader(JUnitTestRef.ofMethod(() -> SettingsSceneTest.class.getDeclaredMethod("testSettingsSceneEnemyShootingProbability"))),
                                        testAwareGrader(JUnitTestRef.ofMethod(() -> SettingsSceneTest.class.getDeclaredMethod("testSettingsSceneFullscreen"))),
                                        testAwareGrader(JUnitTestRef.ofMethod(() -> SettingsSceneTest.class.getDeclaredMethod("testSettingsSceneLoadTextures"))),
                                        testAwareGrader(JUnitTestRef.ofMethod(() -> SettingsSceneTest.class.getDeclaredMethod("testSettingsSceneLoadBackground")))
                                    );
//                                    final var result = innerGrader.grade(cycle, fakeCriterion);
                                    final var result = graders.stream()
                                        .map(grader -> grader.grade(cycle, fakeCriterion))
                                        .peek(x -> JFXUtils.messageTutor("result: " + x.getMaxPoints()))
                                        .reduce((a, b) -> GradeResult.withComments(GradeResult.of(
                                            a.getMaxPoints() + b.getMaxPoints(),
                                            a.getMinPoints() + b.getMinPoints()
                                        ), Stream.of(a.getComments(), b.getComments()).flatMap(List::stream).toList()))
                                        .orElse(GradeResult.of(0, 6));
                                    return GradeResult.withComments(GradeResult.of(
                                        result.getMaxPoints() / 2,
                                        result.getMinPoints() / 2
                                    ), result.getComments());
                                }
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
