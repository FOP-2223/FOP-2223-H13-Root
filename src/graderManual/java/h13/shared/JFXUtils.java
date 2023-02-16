package h13.shared;

import javafx.embed.swing.JFXPanel;

import java.util.concurrent.CountDownLatch;

public class JFXUtils {
    public static void initFX() {
        // This method is invoked on the EDT thread
        new JFXPanel(); // initializes JavaFX environment
    }

    public static void onJFXThread(Runnable r) throws InterruptedException {
        if (javafx.application.Platform.isFxApplicationThread()) {
            r.run();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            javafx.application.Platform.runLater(() -> {
                try {
                    r.run();
                } finally {
                    latch.countDown();
                }
            });
            latch.await();
        }
    }
}
