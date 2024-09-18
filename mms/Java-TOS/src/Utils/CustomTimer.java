package Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class CustomTimer {
    Timeline timeline;

    int hours = 0, mins = 0, secs = 0, millis = 0;
    boolean isRunning = false;

    public void start() {

        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    countTime();
                }
            }));

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(false);
            timeline.play();

            isRunning = true;
        }
    }

    private void countTime() {
        millis++; // increases milliseconds that get changed to min:sec format
    }

    public void stop() { // Stops timer and resets flashing teach buttons
        if (timeline != null) {
            timeline.pause();
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline = null;

            isRunning = false;
        }
    }

    public void reset() {
        stop();

        millis = 0;
    }

    public double getTimeInSeconds() {
        double passSecs = millis * 0.1;
        return passSecs;
    }
}
