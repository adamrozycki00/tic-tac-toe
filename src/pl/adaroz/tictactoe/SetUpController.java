package pl.adaroz.tictactoe;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import java.util.List;

public class SetUpController {

    private final BoardController boardController;
    private final Color markColor = Color.BLACK;
    private final Mark openingMark = Mark.CROSS;
    private final List<Pane[]> fieldLines = new ArrayList<>();
    private final List<Pane> emptyFields = new ArrayList<>();

    private Mark playerMark;
    private Mark computerMark;
    private boolean twoPlayers;
    private boolean endOfGame;

    private double centerX;
    private double centerY;
    private double radius;
    private double lineStartX;
    private double lineStartY;
    private double lineEndX;
    private double lineEndY;

    public SetUpController(BoardController boardController) {
        this.boardController = boardController;
    }

    public int getMarkStrokeWidth() { return 3; }

    public Color getMarkColor() { return markColor; }

    public Mark getOpeningMark() { return openingMark; }

    public List<Pane[]> getFieldLines() { return fieldLines; }

    public List<Pane> getEmptyFields() { return emptyFields; }

    public Mark getPlayerMark() { return playerMark; }

    public void setPlayerMark(Mark playerMark) { this.playerMark = playerMark; }

    public Mark getComputerMark() { return computerMark; }

    public void setComputerMark(Mark computerMark) { this.computerMark = computerMark; }

    public boolean areTwoPlayers() { return twoPlayers; }

    public void setTwoPlayers(boolean areTwoPlayers) { this.twoPlayers = areTwoPlayers; }

    public boolean isEndOfGame() { return endOfGame; }

    public void setEndOfGame(boolean endOfGame) { this.endOfGame = endOfGame; }

    public double getCenterX() { return centerX; }

    public double getCenterY() { return centerY; }

    public double getRadius() { return radius; }

    public double getLineStartX() { return lineStartX; }

    public double getLineStartY() { return lineStartY; }

    public double getLineEndX() { return lineEndX; }

    public double getLineEndY() { return lineEndY; }

    public void initialize() {
        setRadioButtons();
        setFieldLines();
        resetEmptyFields();
        assignMarksToPlayers();
    }

    public void startNewGame() {
        clearBoard();
        resetEmptyFields();
        disableOptions(false);
        handleNewGameButton();
        setForTwoPlayers();
        endOfGame = false;
    }

    public void setPlayersAndMarks() {
        setTwoPlayers((int) boardController.playersToggleGroup.getSelectedToggle().getUserData() == 2);
        boardController.markTitledPane.setDisable(areTwoPlayers());
        setPlayerMark(areTwoPlayers() ?
                getOpeningMark()
                : (Mark) boardController.markToggleGroup.getSelectedToggle().getUserData());
    }

    public boolean doesComputerMoveFirst() {
        return !twoPlayers
                && computerMark == openingMark
                && emptyFields.size() == 9;
    }

    public void resizeMarks() {
        double width = boardController.field00.getWidth();
        double height = boardController.field00.getHeight();

        centerX = width / 2;
        centerY = height / 2;
        radius = Math.min(width, height) / 3;

        lineStartX = Math.max(width / 6, width / 2 - radius);
        lineStartY = Math.max(height / 6, height / 2 - radius);
        lineEndX = Math.min(width * 5 / 6, width / 2 + radius);
        lineEndY = Math.min(height * 5 / 6, height / 2 + radius);
    }

    public void handleNewGameButton() {
        String text = doesComputerMoveFirst() ?
                "Click again!"
                : "New game";
        boardController.newGameButton.setText(text);
    }

    public void disableOptions(boolean disable) {
        disableUnselectedPlayersChoice(disable);
        disableComputerMarkChoice(disable);
    }

    public void disableComputerMarkChoice(boolean disable) {
        for (Toggle toggle : boardController.markToggleGroup.getToggles()) {
            RadioButton button = (RadioButton) toggle;
            if (toggle.getUserData() == computerMark)
                button.setDisable(disable);
            else
                button.setDisable(false);
        }
    }

    public Mark getDifferentMarkThan(Mark mark) {
        return (mark == Mark.CROSS) ? Mark.NOUGHT : Mark.CROSS;
    }

    private void setRadioButtons() {
        boardController.onePlayerRadioButton.setUserData(1);
        boardController.twoPlayersRadioButton.setUserData(2);
        boardController.noughtRadioButton.setUserData(Mark.NOUGHT);
        boardController.crossRadioButton.setUserData(Mark.CROSS);
        for (Toggle toggle : boardController.markToggleGroup.getToggles()) {
            if (toggle.getUserData() == openingMark)
                boardController.markToggleGroup.selectToggle(toggle);
        }
    }

    private void setFieldLines() {
        fieldLines.add(new Pane[]{boardController.field00, boardController.field01, boardController.field02});
        fieldLines.add(new Pane[]{boardController.field10, boardController.field11, boardController.field12});
        fieldLines.add(new Pane[]{boardController.field20, boardController.field21, boardController.field22});
        fieldLines.add(new Pane[]{boardController.field00, boardController.field10, boardController.field20});
        fieldLines.add(new Pane[]{boardController.field01, boardController.field11, boardController.field21});
        fieldLines.add(new Pane[]{boardController.field02, boardController.field12, boardController.field22});
        fieldLines.add(new Pane[]{boardController.field00, boardController.field11, boardController.field22});
        fieldLines.add(new Pane[]{boardController.field20, boardController.field11, boardController.field02});
    }

    private void resetEmptyFields() {
        emptyFields.clear();
        for (Node node : boardController.board.getChildren()) {
            if (node instanceof Pane)
                emptyFields.add((Pane) node);
        }
    }

    private void assignMarksToPlayers() {
        playerMark = openingMark;
        computerMark = getDifferentMarkThan(playerMark);
    }

    private void clearBoard() {
        for (Node node : boardController.board.getChildren()) {
            if (node instanceof Pane) {
                Pane field = (Pane) node;
                field.getChildren().clear();
                field.setUserData(null);
            }
        }
        boardController.mainPanel.getChildren().removeIf(node -> node instanceof Line);
    }

    private void disableUnselectedPlayersChoice(boolean disable) {
        for (Toggle toggle : boardController.playersToggleGroup.getToggles()) {
            RadioButton button = (RadioButton) toggle;
            if (!button.isSelected())
                button.setDisable(disable);
            else
                button.setDisable(false);
        }
    }

    private void setForTwoPlayers() {
        if (twoPlayers) playerMark = openingMark;
    }

}
