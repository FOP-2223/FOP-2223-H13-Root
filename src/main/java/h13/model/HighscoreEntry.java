package h13.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class HighscoreEntry {
    SimpleStringProperty playerName;
    SimpleStringProperty date;
    SimpleIntegerProperty score;

    public HighscoreEntry(String playerName, String date, int score) {
        this.playerName = new SimpleStringProperty(playerName);
        this.date = new SimpleStringProperty(date);
        this.score = new SimpleIntegerProperty(score);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public void setPlayerName(final String playerName) {
        this.playerName.set(playerName);
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(final int score) {
        this.score.set(score);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(final String date) {
        this.date.set(date);
    }
}
