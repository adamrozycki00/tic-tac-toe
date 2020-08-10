package pl.adaroz.tictactoe.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import pl.adaroz.tictactoe.mark.Mark;
import pl.adaroz.tictactoe.strategy.ComputerChoice;
import pl.adaroz.tictactoe.strategy.RandomChoice;

public class GameController {

    private final BoardController boardController;
    private final SetUpController setUpController;

    public GameController(BoardController boardController) {
        this.boardController = boardController;
        this.setUpController = boardController.getSetUpController();
    }

    public void newGameButtonPressed() {
        if (setUpController.doesComputerMoveFirst())
            move(setUpController.getComputerMark(), getFieldChosenByComputer());
        else
            setUpController.startNewGame();
    }

    public void fieldClicked(Pane field) {
        if (setUpController.areTwoPlayers())
            move(setUpController.getPlayerMark(), field);
        if (!setUpController.doesComputerMoveFirst() && move(setUpController.getPlayerMark(), field))
            move(setUpController.getComputerMark(), getFieldChosenByComputer());
    }

    public void playersRadioButtonSelected() {
        setUpController.setPlayersAndMarks();
        setUpController.handleNewGameButton();
    }

    public void markRadioButtonSelected() {
        setUpController.setPlayerMark((Mark) boardController.markToggleGroup.getSelectedToggle().getUserData());
        if (setUpController.getPlayerMark() == setUpController.getOpeningMark()) {
            setUpController.setComputerMark(setUpController.getDifferentMarkThan(setUpController.getOpeningMark()));
            setUpController.handleNewGameButton();
        } else {
            setUpController.setComputerMark(setUpController.getOpeningMark());
            move(setUpController.getComputerMark(), getFieldChosenByComputer());
        }
        setUpController.disableComputerMarkChoice(true);
    }

    private boolean move(Mark mark, Pane field) {
        if (!isMovePossible(field))
            return false;
        setUpController.resizeMarks();
        putMark(mark, field);
        setUpController.getEmptyFields().remove(field);
        switchPlayerIfTwo();
        setUpController.handleNewGameButton();
        setUpController.disableOptions(true);
        endGameIfWon();
        endGameIfNoEmptyFieldLeft();
        return true;
    }

    private boolean isMovePossible(Pane field) {
        return setUpController.getEmptyFields().contains(field) && !setUpController.isEndOfGame();
    }

    private void putMark(Mark mark, Pane field) {
        if (mark == Mark.NOUGHT)
            putNought(field);
        else
            putCross(field);
    }

    private void putNought(Pane field) {
        Circle nought = new Circle(setUpController.getCenterX(), setUpController.getCenterY(), setUpController.getRadius());
        nought.setFill(Color.TRANSPARENT);
        nought.setStroke(setUpController.getMarkColor());
        nought.setStrokeWidth(setUpController.getMarkStrokeWidth());
        field.getChildren().add(nought);
        field.setUserData(Mark.NOUGHT);
    }

    private void putCross(Pane field) {
        Line line1 = new Line(setUpController.getLineStartX(), setUpController.getLineStartY(), setUpController.getLineEndX(), setUpController.getLineEndY());
        Line line2 = new Line(setUpController.getLineStartX(), setUpController.getLineEndY(), setUpController.getLineEndX(), setUpController.getLineStartY());
        line1.setStroke(setUpController.getMarkColor());
        line2.setStroke(setUpController.getMarkColor());
        line1.setStrokeWidth(setUpController.getMarkStrokeWidth());
        line2.setStrokeWidth(setUpController.getMarkStrokeWidth());
        field.getChildren().add(line1);
        field.getChildren().add(line2);
        field.setUserData(Mark.CROSS);
    }

    private void switchPlayerIfTwo() {
        if (setUpController.areTwoPlayers())
            setUpController.setPlayerMark(setUpController.getDifferentMarkThan(setUpController.getPlayerMark()));
    }

    private void endGameIfWon() {
        Pane[] winningFields = getWinningFields();
        if (winningFields != null) {
            setUpController.setEndOfGame(true);
            drawEndingLine(winningFields);
        }
    }

    private Pane[] getWinningFields() {
        Pane[] winningFields = getWinningFieldsFor(setUpController.getOpeningMark());
        if (winningFields == null)
            winningFields = getWinningFieldsFor(setUpController.getDifferentMarkThan(setUpController.getOpeningMark()));
        return winningFields;
    }

    private Pane[] getWinningFieldsFor(Mark mark) {
        NEXT_FIELD_LINE:
        for (Pane[] fieldLine : setUpController.getFieldLines()) {
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
        line.setStartX(winningFields[0].getLayoutX() + setUpController.getCenterX());
        line.setStartY(winningFields[0].getLayoutY() + setUpController.getCenterY());
        line.setEndX(winningFields[winningFields.length - 1].getLayoutX() + setUpController.getCenterX());
        line.setEndY(winningFields[winningFields.length - 1].getLayoutY() + setUpController.getCenterY());
        line.setStroke(Color.RED);
        line.setStrokeWidth(5);
        boardController.mainPanel.getChildren().add(line);
    }

    private void endGameIfNoEmptyFieldLeft() {
        if (setUpController.getEmptyFields().isEmpty())
            setUpController.setEndOfGame(true);
    }

    private Pane getFieldChosenByComputer() {
        ComputerChoice computerChoice = new RandomChoice(setUpController);
        return computerChoice.getField();
    }

}
