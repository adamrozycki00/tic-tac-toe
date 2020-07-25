package pl.adaroz.game;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private enum Token {
        NOUGHT, CROSS
    }

    private Token player1Token = Token.NOUGHT;
    private Token player2Token = Token.CROSS;

    private final List<Pane[]> fieldLines = new ArrayList<>();

    private double centerX = 28.5;
    private double centerY = 29;
    private double radius = 18.81;
    private double line1StartX = 9.5;
    private double line1StartY = 10.0;
    private double line2StartX = 47.5;
    private double line2StartY = 10.0;
    private double line2EndX = 9.5;
    private double line2EndY = 48.0;
    private double line1EndX = 47.5;
    private double line1EndY = 48.0;

    private boolean endOfGame;

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
    private RadioButton noughtRadioButton;

    @FXML
    private RadioButton crossRadioButton;

    @FXML
    private ToggleGroup tokenToggleGroup;

    public void initialize() {
        noughtRadioButton.setUserData(Token.NOUGHT);
        crossRadioButton.setUserData(Token.CROSS);
        fieldLines.add(new Pane[]{field00, field01, field02});
        fieldLines.add(new Pane[]{field10, field11, field12});
        fieldLines.add(new Pane[]{field20, field21, field22});
        fieldLines.add(new Pane[]{field00, field10, field20});
        fieldLines.add(new Pane[]{field01, field11, field21});
        fieldLines.add(new Pane[]{field02, field12, field22});
        fieldLines.add(new Pane[]{field00, field11, field22});
        fieldLines.add(new Pane[]{field20, field11, field02});
    }

    private void startNewGame() {
        clearBoard();
        resizeTokens();
        endOfGame = false;
    }

    private void clearBoard() {
        mainPanel.getChildren().removeIf(node -> node instanceof Line);
        for (Node node : board.getChildren()) {
            if (node instanceof Pane) {
                Pane field = (Pane) node;
                field.getChildren().clear();
                field.setUserData(null);
            }
        }
    }

    private void resizeTokens() {
        centerX = field00.getWidth() / 2;
        centerY = field00.getHeight() / 2;
        radius = Math.min(field00.getWidth(), field00.getHeight()) / 3;

        line1StartX = Math.max(field00.getWidth() / 6, field00.getWidth() / 2 - radius);
        line1StartY = Math.max(field00.getHeight() / 6, field00.getHeight() / 2 - radius);
        line1EndX = Math.min(field00.getWidth() * 5 / 6, field00.getWidth() / 2 + radius);
        line1EndY = Math.min(field00.getHeight() * 5 / 6, field00.getHeight() / 2 + radius);
        line2StartX = Math.min(field00.getWidth() * 5 / 6, field00.getWidth() / 2 + radius);
        line2StartY = Math.max(field00.getHeight() / 6, field00.getHeight() / 2 - radius);
        line2EndX = Math.max(field00.getWidth() / 6, field00.getWidth() / 2 - radius);
        line2EndY = Math.min(field00.getHeight() * 5 / 6, field00.getHeight() / 2 + radius);
    }

    private Pane[] getWinningFields() {
        Pane[] winningNoughts = getWinningFieldsForToken(Token.NOUGHT);
        return (winningNoughts != null) ? winningNoughts : getWinningFieldsForToken(Token.CROSS);
    }

    private Pane[] getWinningFieldsForToken(Token token) {
        NEXT_FIELD_LINE:
        for (Pane[] fieldLine : fieldLines) {
            for (Pane field : fieldLine) {
                if (field.getUserData() != token)
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

    private void move(Token token, Pane field) {
        if (endOfGame || field.getChildren().size() > 0)
            return;
        addToken(token, field);
        Pane[] winningFields = getWinningFields();
        if (winningFields != null) {
            endOfGame = true;
            drawEndingLine(winningFields);
        }
    }

    private void addToken(Token token, Pane field) {
        if (token == Token.NOUGHT)
            addNought(field);
        else
            addCross(field);
    }

    private void addNought(Pane field) {
        Circle nought = new Circle(centerX, centerY, radius);
        nought.setFill(Color.TRANSPARENT);
        nought.setStroke(Color.BLACK);
        nought.setStrokeWidth(3);
        field.getChildren().add(nought);
        field.setUserData(Token.NOUGHT);
    }

    private void addCross(Pane field) {
        Line line1 = new Line(line1StartX, line1StartY, line1EndX, line1EndY);
        Line line2 = new Line(line2StartX, line2StartY, line2EndX, line2EndY);
        line1.setStrokeWidth(3);
        line2.setStrokeWidth(3);
        field.getChildren().add(line1);
        field.getChildren().add(line2);
        field.setUserData(Token.CROSS);
    }

    @FXML
    private void newGameButtonPressed(ActionEvent event) {
        startNewGame();
    }

    @FXML
    private void tokenRadioButtonSelected(ActionEvent event) {
        player1Token = (Token) tokenToggleGroup.getSelectedToggle().getUserData();
        player2Token =
                (player1Token == Token.NOUGHT) ? Token.CROSS : Token.NOUGHT;
    }

    @FXML
    private void field00Clicked(MouseEvent event) {
        move(player1Token, field00);
    }

    @FXML
    private void field01Clicked(MouseEvent event) {
        move(player1Token, field01);
    }

    @FXML
    private void field02Clicked(MouseEvent event) {
        move(player1Token, field02);
    }

    @FXML
    private void field10Clicked(MouseEvent event) {
        move(player1Token, field10);
    }

    @FXML
    private void field11Clicked(MouseEvent event) {
        move(player1Token, field11);
    }

    @FXML
    private void field12Clicked(MouseEvent event) {
        move(player1Token, field12);
    }

    @FXML
    private void field20Clicked(MouseEvent event) {
        move(player1Token, field20);
    }

    @FXML
    private void field21Clicked(MouseEvent event) {
        move(player1Token, field21);
    }

    @FXML
    private void field22Clicked(MouseEvent event) {
        move(player1Token, field22);
    }

}
