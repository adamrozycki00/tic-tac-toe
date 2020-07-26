package pl.adaroz.game;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeController {

    private enum Mark {NOUGHT, CROSS}

    private Mark playerMark = Mark.CROSS;
    private Mark computerMark = Mark.NOUGHT;

    private final List<Pane[]> fieldLines = new ArrayList<>();
    private final List<Pane> emptyFields = new ArrayList<>();

    private double centerX = 28.5;
    private double centerY = 29;
    private double radius = 19;
    private double lineStartX = 9.5;
    private double lineStartY = 10.0;
    private double lineEndX = 47.5;
    private double lineEndY = 48.0;

    private Boolean areTwoPlayers = false;

    private boolean endOfGame;

    private final String newGameText = "New game";

    @FXML
    private BorderPane mainPanel;
    @FXML
    private GridPane board;

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
    private TitledPane markTitledPane;

    @FXML
    private RadioButton onePlayerRadioButton;
    @FXML
    private RadioButton twoPlayersRadioButton;
    @FXML
    private ToggleGroup playersToggleGroup;
    @FXML
    private RadioButton noughtRadioButton;
    @FXML
    private RadioButton crossRadioButton;
    @FXML
    private ToggleGroup markToggleGroup;
    @FXML
    private Button newGameButton;

    public void initialize() {
        setRadioButtons();
        setFieldLines();
        resetEmptyFields();
    }

    private void setRadioButtons() {
        noughtRadioButton.setUserData(Mark.NOUGHT);
        crossRadioButton.setUserData(Mark.CROSS);
        onePlayerRadioButton.setUserData(false);
        twoPlayersRadioButton.setUserData(true);
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

    private boolean doesComputerMoveFirst() {
        return !areTwoPlayers
                && computerMark == Mark.CROSS
                && emptyFields.size() == 9;
    }

    private void startNewGame() {
        clearBoard();
        resetEmptyFields();
        resizeMarks();
        disableNumberOfPlayersChoice(false);
        disableComputerMarkChoice(false);
        endOfGame = false;
        if (doesComputerMoveFirst())
            newGameButton.setText("Click again!");
        if (areTwoPlayers)
            playerMark = Mark.CROSS;
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

    private void disableComputerMarkChoice(boolean disable) {
        for (Toggle toggle : markToggleGroup.getToggles()) {
            ((RadioButton) toggle).setDisable(false);
            if (disable)
                ((RadioButton) toggle).setDisable(toggle.getUserData() == computerMark);
        }
    }

    private void disableNumberOfPlayersChoice(boolean disable) {
        for (Toggle toggle : playersToggleGroup.getToggles()) {
            RadioButton button = (RadioButton) toggle;
            button.setDisable(false);
            if (!button.isSelected())
                button.setDisable(disable);
        }
    }

    private boolean move(Mark mark, Pane field) {
        if (!isMovePossible(field))
            return false;
        putMark(mark, field);
        emptyFields.remove(field);
        disableNumberOfPlayersChoice(true);
        if (areTwoPlayers)
            switchPlayerMark();
        else
            disableComputerMarkChoice(true);
        checkForWin();
        checkForEmptyFields();
        return true;
    }

    private void switchPlayerMark() {
        if (playerMark == Mark.CROSS)
            playerMark = Mark.NOUGHT;
        else
            playerMark = Mark.CROSS;
    }

    private boolean isMovePossible(Pane field) {
        return emptyFields.contains(field) && !endOfGame;
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
        nought.setStroke(Color.BLACK);
        nought.setStrokeWidth(3);
        field.getChildren().add(nought);
        field.setUserData(Mark.NOUGHT);
    }

    private void putCross(Pane field) {
        Line line1 = new Line(lineStartX, lineStartY, lineEndX, lineEndY);
        Line line2 = new Line(lineStartX, lineEndY, lineEndX, lineStartY);
        line1.setStrokeWidth(3);
        line2.setStrokeWidth(3);
        field.getChildren().add(line1);
        field.getChildren().add(line2);
        field.setUserData(Mark.CROSS);
    }

    private void checkForWin() {
        Pane[] winningFields = getWinningFields();
        if (winningFields != null) {
            endOfGame = true;
            drawEndingLine(winningFields);
        }
    }

    private Pane[] getWinningFields() {
        Pane[] winningNoughts = getWinningFieldsForMark(Mark.NOUGHT);
        return (winningNoughts != null) ? winningNoughts : getWinningFieldsForMark(Mark.CROSS);
    }

    private Pane[] getWinningFieldsForMark(Mark mark) {
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
        line.setEndX(winningFields[2].getLayoutX() + centerX);
        line.setEndY(winningFields[2].getLayoutY() + centerY);
        line.setStrokeWidth(5);
        line.setStroke(Color.RED);
        mainPanel.getChildren().add(line);
    }

    private void checkForEmptyFields() {
        if (emptyFields.isEmpty())
            endOfGame = true;
    }

    private Pane getComputerField() {
        if (emptyFields.isEmpty())
            return null;
        int randomIndex = new Random().nextInt(emptyFields.size());
        return emptyFields.get(randomIndex);
    }

    @FXML
    private void playersRadioButtonSelected() {
        areTwoPlayers = (Boolean) playersToggleGroup.getSelectedToggle().getUserData();
        markTitledPane.setDisable(areTwoPlayers);
        playerMark = areTwoPlayers ?
                Mark.CROSS :
                (Mark) markToggleGroup.getSelectedToggle().getUserData();
    }

    @FXML
    private void newGameButtonPressed() {
        if (doesComputerMoveFirst()) {
            move(computerMark, getComputerField());
            newGameButton.setText(newGameText);
        } else
            startNewGame();
    }

    @FXML
    private void markRadioButtonSelected() {
        playerMark = (Mark) markToggleGroup.getSelectedToggle().getUserData();
        if (playerMark == Mark.CROSS) {
            computerMark = Mark.NOUGHT;
            newGameButton.setText(newGameText);
        } else {
            computerMark = Mark.CROSS;
            move(computerMark, getComputerField());
        }
        disableComputerMarkChoice(true);
    }

    @FXML
    private void field00Clicked() {
        if (areTwoPlayers)
            move(playerMark, field00);
        else if (!doesComputerMoveFirst() && move(playerMark, field00))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field01Clicked() {
        if (areTwoPlayers)
            move(playerMark, field01);
        if (!doesComputerMoveFirst() && move(playerMark, field01))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field02Clicked() {
        if (areTwoPlayers)
            move(playerMark, field02);
        if (!doesComputerMoveFirst() && move(playerMark, field02))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field10Clicked() {
        if (areTwoPlayers)
            move(playerMark, field10);
        if (!doesComputerMoveFirst() && move(playerMark, field10))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field11Clicked() {
        if (areTwoPlayers)
            move(playerMark, field11);
        if (!doesComputerMoveFirst() && move(playerMark, field11))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field12Clicked() {
        if (areTwoPlayers)
            move(playerMark, field12);
        if (!doesComputerMoveFirst() && move(playerMark, field12))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field20Clicked() {
        if (areTwoPlayers)
            move(playerMark, field20);
        if (!doesComputerMoveFirst() && move(playerMark, field20))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field21Clicked() {
        if (areTwoPlayers)
            move(playerMark, field21);
        if (!doesComputerMoveFirst() && move(playerMark, field21))
            move(computerMark, getComputerField());
    }

    @FXML
    private void field22Clicked() {
        if (areTwoPlayers)
            move(playerMark, field22);
        if (!doesComputerMoveFirst() && move(playerMark, field22))
            move(computerMark, getComputerField());
    }

}
