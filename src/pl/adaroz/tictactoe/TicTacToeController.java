package pl.adaroz.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeController {

    private enum Mark {NOUGHT, CROSS}

    private final Mark openingMark = Mark.NOUGHT;
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

    private int numberOfFieldsOnTheBoard;

    private boolean areTwoPlayers;
    private boolean endOfGame;

    @FXML
    private BorderPane mainPanel;
    @FXML
    private GridPane board;
    @FXML
    private TitledPane markTitledPane;

    @FXML
    private Pane field00;
    @FXML
    private Pane field01;
    @FXML
    private Pane field02;
    @FXML
    private Pane field10;
    @FXML
    private Pane field11;
    @FXML
    private Pane field12;
    @FXML
    private Pane field20;
    @FXML
    private Pane field21;
    @FXML
    private Pane field22;

    @FXML
    private ToggleGroup playersToggleGroup;
    @FXML
    private RadioButton onePlayerRadioButton;
    @FXML
    private RadioButton twoPlayersRadioButton;
    @FXML
    private ToggleGroup markToggleGroup;
    @FXML
    private RadioButton noughtRadioButton;
    @FXML
    private RadioButton crossRadioButton;
    @FXML
    private Button newGameButton;

    public void initialize() {
        setRadioButtons();
        setFieldLines();
        resetEmptyFields();
        calculateNumberOfFieldsOnTheBoard();
        assignMarksToPlayers();
    }

    private void setRadioButtons() {
        onePlayerRadioButton.setUserData(1);
        twoPlayersRadioButton.setUserData(2);
        noughtRadioButton.setUserData(Mark.NOUGHT);
        crossRadioButton.setUserData(Mark.CROSS);
        for (Toggle toggle : markToggleGroup.getToggles()) {
            if (toggle.getUserData() == openingMark)
                markToggleGroup.selectToggle(toggle);
        }
    }

    private void setFieldLines() {
        fieldLines.add(new Pane[]{field00, field01, field02});
        fieldLines.add(new Pane[]{field10, field11, field12});
        fieldLines.add(new Pane[]{field20, field21, field22});
        fieldLines.add(new Pane[]{field00, field10, field20});
        fieldLines.add(new Pane[]{field01, field11, field21});
        fieldLines.add(new Pane[]{field02, field12, field22});
        fieldLines.add(new Pane[]{field00, field11, field22});
        fieldLines.add(new Pane[]{field20, field11, field02});
    }

    private void resetEmptyFields() {
        emptyFields.clear();
        for (Node node : board.getChildren()) {
            if (node instanceof Pane)
                emptyFields.add((Pane) node);
        }
    }

    private void calculateNumberOfFieldsOnTheBoard() {
        int count = 0;
        for (Node node : board.getChildren())
            if (node instanceof Pane) ++count;
        numberOfFieldsOnTheBoard = count;
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
        for (Node node : board.getChildren()) {
            if (node instanceof Pane) {
                Pane field = (Pane) node;
                field.getChildren().clear();
                field.setUserData(null);
            }
        }
        mainPanel.getChildren().removeIf(node -> node instanceof Line);
    }

    private void disableOptions(boolean disable) {
        disableUnselectedPlayersChoice(disable);
        disableComputerMarkChoice(disable);
    }

    private void disableUnselectedPlayersChoice(boolean disable) {
        for (Toggle toggle : playersToggleGroup.getToggles()) {
            RadioButton button = (RadioButton) toggle;
            if (!button.isSelected())
                button.setDisable(disable);
            else
                button.setDisable(false);
        }
    }

    private void disableComputerMarkChoice(boolean disable) {
        for (Toggle toggle : markToggleGroup.getToggles()) {
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
        newGameButton.setText(text);
    }

    private boolean doesComputerMoveFirst() {
        return !areTwoPlayers
                && computerMark == openingMark
                && emptyFields.size() == numberOfFieldsOnTheBoard;
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
        double width = field00.getWidth();
        double height = field00.getHeight();

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
        Pane[] winningNoughts = getWinningFieldsFor(Mark.NOUGHT);
        return (winningNoughts != null) ?
                winningNoughts
                : getWinningFieldsFor(Mark.CROSS);
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
        line.setStrokeWidth(5);
        line.setStroke(Color.RED);
        mainPanel.getChildren().add(line);
    }

    private void endGameIfNoEmptyFieldLeft() {
        if (emptyFields.isEmpty())
            endOfGame = true;
    }

    private void fieldClicked(Pane field) {
        if (areTwoPlayers)
            move(playerMark, field);
        if (!doesComputerMoveFirst() && move(playerMark, field))
            move(computerMark, getFieldChosenByComputer());
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

    @FXML
    private void playersRadioButtonSelected() {
        areTwoPlayers = ((int) playersToggleGroup.getSelectedToggle().getUserData() == 2);
        markTitledPane.setDisable(areTwoPlayers);
        playerMark = areTwoPlayers ?
                openingMark
                : (Mark) markToggleGroup.getSelectedToggle().getUserData();
        handleNewGameButton();
    }

    @FXML
    private void newGameButtonPressed() {
        if (doesComputerMoveFirst())
            move(computerMark, getFieldChosenByComputer());
        else
            startNewGame();
    }

    @FXML
    private void markRadioButtonSelected() {
        playerMark = (Mark) markToggleGroup.getSelectedToggle().getUserData();
        if (playerMark == openingMark) {
            computerMark = getDifferentMarkThan(openingMark);
            handleNewGameButton();
        } else {
            computerMark = openingMark;
            move(computerMark, getFieldChosenByComputer());
        }
        disableComputerMarkChoice(true);
    }

    @FXML
    private void field00Clicked() {
        fieldClicked(field00);
    }

    @FXML
    private void field01Clicked() {
        fieldClicked(field01);
    }

    @FXML
    private void field02Clicked() {
        fieldClicked(field02);
    }

    @FXML
    private void field10Clicked() {
        fieldClicked(field10);
    }

    @FXML
    private void field11Clicked() {
        fieldClicked(field11);
    }

    @FXML
    private void field12Clicked() {
        fieldClicked(field12);
    }

    @FXML
    private void field20Clicked() {
        fieldClicked(field20);
    }

    @FXML
    private void field21Clicked() {
        fieldClicked(field21);
    }

    @FXML
    private void field22Clicked() {
        fieldClicked(field22);
    }

}
