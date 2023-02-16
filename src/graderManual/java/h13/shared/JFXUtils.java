package h13.shared;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class JFXUtils {
    public static void initFX() {
        // This method is invoked on the EDT thread
        new JFXPanel(); // initializes JavaFX environment
    }

    public static void onJFXThread(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
//        if (javafx.application.Platform.isFxApplicationThread()) {
//            r.run();
//        } else {
        Semaphore semaphore = new Semaphore(0);
        javafx.application.Platform.runLater(() -> {
            try {
                r.run();
            } finally {
                semaphore.release();
            }
        });
        if (!semaphore.tryAcquire(timeout, unit)) {
            throw new RuntimeException("Timeout waiting for FX task to complete");
        }
//        }
    }

    public static void onJFXThread(Runnable r) throws InterruptedException {
        onJFXThread(r, 60, TimeUnit.SECONDS);
    }

    public static void waitOnJavaFxPlatformEventsDone() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(countDownLatch::countDown);
        countDownLatch.await();
    }

    public static boolean TutorAskYesNo(String question) throws InterruptedException {
        // Prompt tutor to answer a yes/no question
        final AtomicBoolean answer = new AtomicBoolean(false);
        onJFXThread(() -> {
            final Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                question,
                ButtonType.YES,
                ButtonType.NO
            );
            alert.setTitle("TUTOR Manual Grader");
            alert.setHeaderText("TUTOR Manual Grader");
            alert.getDialogPane().setStyle("-fx-background-color: #11131d;");
            alert.getDialogPane().getStylesheets().add("/h13/controller/scene/game/darkMode.css");
            alert.setWidth(500);
            alert.getDialogPane().lookup(".header-panel").setStyle("-fx-background-color: #2a3145; "
                                                                       + "-fx-font-weight: bold;"
                                                                       + "-fx-text-fill: red;");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                answer.set(true);
            }
        });
        return answer.get();
    }
}
