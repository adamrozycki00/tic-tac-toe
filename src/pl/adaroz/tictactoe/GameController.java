package pl.adaroz.tictactoe;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private enum Mark {NOUGHT, CROSS}

    public GameController(BoardController boardController) {
        this.boardController = boardController;
    }

    private final BoardController boardController;
    private final Mark openingMark = Mark.CROSS;
    private final List<Pane[]> fieldLines = new ArrayList<>();
    private final List<Pane> emptyFields = new ArrayList<>();
    private final Color markColor = Color.BLACK;
    private final int markStrokeWidth = 3;

    private Mark playerMark;
    private Mark computerMark;

    private double centerX;
    private double centerY;
    private double radius;
    private double lineStartX;
    private double lineStartY;
    private double lineEndX;
    private double lineEndY;

    private boolean areTwoPlayers;
    private boolean endOfGame;

    public void initialize() {
        setRadioButtons();
        setFieldLines();
        resetEmptyFields();
        assignMarksToPlayers();
    }

    public void fieldClicked(Pane field) {
        if (areTwoPlayers)
            move(playerMark, field);
        if (!doesComputerMoveFirst() && move(playerMark, field))
            move(computerMark, getFieldChosenByComputer());
    }

    public void playersRadioButtonSelected() {
        areTwoPlayers = ((int) boardController.playersToggleGroup.getSelectedToggle().getUserData() == 2);
        boardController.markTitledPane.setDisable(areTwoPlayers);
        playerMark = areTwoPlayers ?
                openingMark
                : (Mark) boardController.markToggleGroup.getSelectedToggle().getUserData();
        handleNewGameButton();
    }

    public void newGameButtonPressed() {
        if (doesComputerMoveFirst())
            move(computerMark, getFieldChosenByComputer());
        else
            startNewGame();
    }

    public void markRadioButtonSelected() {
        playerMark = (Mark) boardController.markToggleGroup.getSelectedToggle().getUserData();
        if (playerMark == openingMark) {
            computerMark = getDifferentMarkThan(openingMark);
            handleNewGameButton();
        } else {
            computerMark = openingMark;
            move(computerMark, getFieldChosenByComputer());
        }
        disableComputerMarkChoice(true);
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

    private Mark getDifferentMarkThan(Mark mark) {
        return (mark == Mark.CROSS) ?
                Mark.NOUGHT
                : Mark.CROSS;
    }

    private void startNewGame() {
        clearBoard();
        resetEmptyFields();
        disableOptions(false);
        handleNewGameButton();
        setForTwoPlayers();
        endOfGame = false;
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

    private void disableOptions(boolean disable) {
        disableUnselectedPlayersChoice(disable);
        disableComputerMarkChoice(disable);
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

    private void disableComputerMarkChoice(boolean disable) {
        for (Toggle toggle : boardController.markToggleGroup.getToggles()) {
            RadioButton button = (RadioButton) toggle;
            if (toggle.getUserData() == computerMark)
                button.setDisable(disable);
            else
                button.setDisable(false);
        }
    }

    private void handleNewGameButton() {
        String text = doesComputerMoveFirst() ?
                "Click again!"
                : "New game";
        boardController.newGameButton.setText(text);
    }

    private boolean doesComputerMoveFirst() {
        return !areTwoPlayers
                && computerMark == openingMark
                && emptyFields.size() == 9;
    }

    private void setForTwoPlayers() {
        if (areTwoPlayers)
            playerMark = openingMark;
    }

    private boolean move(Mark mark, Pane field) {
        if (!isMovePossible(field))
            return false;
        resizeMarks();
        putMark(mark, field);
        emptyFields.remove(field);
        switchPlayerIfTwo();
        handleNewGameButton();
        disableOptions(true);
        endGameIfWon();
        endGameIfNoEmptyFieldLeft();
        return true;
    }

    private boolean isMovePossible(Pane field) {
        return emptyFields.contains(field) && !endOfGame;
    }

    private void resizeMarks() {
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

    private void putMark(Mark mark, Pane field) {
        if (mark == Mark.NOUGHT)
            putNought(field);
        else
            putCross(field);
    }

    private void putNought(Pane field) {
        Circle nought = new Circle(centerX, centerY, radius);
        nought.setFill(Color.TRANSPARENT);
        nought.setStroke(markColor);
        nought.setStrokeWidth(markStrokeWidth);
        field.getChildren().add(nought);
        field.setUserData(Mark.NOUGHT);
    }

    private void putCross(Pane field) {
        Line line1 = new Line(lineStartX, lineStartY, lineEndX, lineEndY);
        Line line2 = new Line(lineStartX, lineEndY, lineEndX, lineStartY);
        line1.setStroke(markColor);
        line2.setStroke(markColor);
        line1.setStrokeWidth(markStrokeWidth);
        line2.setStrokeWidth(markStrokeWidth);
        field.getChildren().add(line1);
        field.getChildren().add(line2);
        field.setUserData(Mark.CROSS);
    }

    private void switchPlayerIfTwo() {
        if (areTwoPlayers)
            playerMark = getDifferentMarkThan(playerMark);
    }

    private void endGameIfWon() {
        Pane[] winningFields = getWinningFields();
        if (winningFields != null) {
            endOfGame = true;
            drawEndingLine(winningFields);
        }
    }

    private Pane[] getWinningFields() {
        Pane[] winningFields = getWinningFieldsFor(openingMark);
        if (winningFields == null)
            winningFields = getWinningFieldsFor(getDifferentMarkThan(openingMark));
        return winningFields;
    }

    private Pane[] getWinningFieldsFor(Mark mark) {
        NEXT_FIELD_LINE:
        for (Pane[] fieldLine : fieldLines) {
            for (Pane field : fieldLine) {
                if (field.getUserData() != mark)
                    continue NEXT_FIELD_LINE;
            }
            return fieldLine;
        }
        return null;
    }

    private void drawEndingLine(Pane[] winningFields) {
        Line line = new Line();
        line.setStartX(winningFields[0].getLayoutX() + centerX);
        line.setStartY(winningFields[0].getLayoutY() + centerY);
        line.setEndX(winningFields[winningFields.length - 1].getLayoutX() + centerX);
        line.setEndY(winningFields[winningFields.length - 1].getLayoutY() + centerY);
        line.setStroke(Color.RED);
        line.setStrokeWidth(5);
        boardController.mainPanel.getChildren().add(line);
    }

    private void endGameIfNoEmptyFieldLeft() {
        if (emptyFields.isEmpty())
            endOfGame = true;
    }

    private Pane getFieldChosenByComputer() {
        return getRandomField();
    }

    private Pane getRandomField() {
        if (emptyFields.isEmpty())
            return null;
        int randomIndex = new Random().nextInt(emptyFields.size());
        return emptyFields.get(randomIndex);
    }

}
