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
        NAUGHT, CROSS
    }

    private Token player1Token = Token.NAUGHT;
    private Token player2Token = Token.CROSS;

    private final List<Pane[]> winningLines = new ArrayList<>();

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
    private BorderPane gamePane;

    @FXML
    private GridPane boardPane;

    @FXML
    private Pane pane00;

    @FXML
    private Pane pane01;

    @FXML
    private Pane pane02;

    @FXML
    private Pane pane10;

    @FXML
    private Pane pane11;

    @FXML
    private Pane pane12;

    @FXML
    private Pane pane20;

    @FXML
    private Pane pane21;

    @FXML
    private Pane pane22;

    @FXML
    private RadioButton noughtRadioButton;

    @FXML
    private RadioButton crossRadioButton;

    @FXML
    private ToggleGroup tokenToggleGroup;

    public void initialize() {
        noughtRadioButton.setUserData(Token.NAUGHT);
        crossRadioButton.setUserData(Token.CROSS);
        winningLines.add(new Pane[]{pane00, pane01, pane02});
        winningLines.add(new Pane[]{pane10, pane11, pane12});
        winningLines.add(new Pane[]{pane20, pane21, pane22});
        winningLines.add(new Pane[]{pane00, pane10, pane20});
        winningLines.add(new Pane[]{pane01, pane11, pane21});
        winningLines.add(new Pane[]{pane02, pane12, pane22});
        winningLines.add(new Pane[]{pane00, pane11, pane22});
        winningLines.add(new Pane[]{pane20, pane11, pane02});
    }

    private void startNewGame() {
        clearBoard();
        resizeTokens();
        endOfGame = false;
    }

    private void clearBoard() {
        gamePane.getChildren().removeIf(node -> node instanceof Line);
        for (Node node : boardPane.getChildren())
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                pane.getChildren().clear();
                pane.setUserData(null);
            }
    }

    private void resizeTokens() {
        centerX = pane00.getWidth() / 2;
        centerY = pane00.getHeight() / 2;
        radius = Math.min(pane00.getWidth(), pane00.getHeight()) / 3;

        line1StartX = Math.max(pane00.getWidth() / 6, pane00.getWidth() / 2 - radius);
        line1StartY = Math.max(pane00.getHeight() / 6, pane00.getHeight() / 2 - radius);
        line1EndX = Math.min(pane00.getWidth() * 5 / 6, pane00.getWidth() / 2 + radius);
        line1EndY = Math.min(pane00.getHeight() * 5 / 6, pane00.getHeight() / 2 + radius);
        line2StartX = Math.min(pane00.getWidth() * 5 / 6, pane00.getWidth() / 2 + radius);
        line2StartY = Math.max(pane00.getHeight() / 6, pane00.getHeight() / 2 - radius);
        line2EndX = Math.max(pane00.getWidth() / 6, pane00.getWidth() / 2 - radius);
        line2EndY = Math.min(pane00.getHeight() * 5 / 6, pane00.getHeight() / 2 + radius);
    }

    private void move(Token token, Pane pane) {
        if (endOfGame)
            return;
        if (pane.getChildren().size() == 0)
            addToken(token, pane);
        Pane[] winningLine = getWinningLine();
        if (winningLine != null) {
            endOfGame = true;
            drawLine(winningLine);
        }
    }

    private void addToken(Token token, Pane pane) {
        if (token == Token.NAUGHT)
            addNought(pane);
        else
            addCross(pane);
    }

    private void addNought(Pane pane) {
        Circle nought = new Circle(centerX, centerY, radius);
        nought.setFill(Color.TRANSPARENT);
        nought.strokeProperty().set(Color.BLACK);
        pane.getChildren().add(nought);
        pane.setUserData(Token.NAUGHT);
    }

    private void addCross(Pane pane) {
        pane.getChildren().add(new Line(line1StartX, line1StartY, line1EndX, line1EndY));
        pane.getChildren().add(new Line(line2StartX, line2StartY, line2EndX, line2EndY));
        pane.setUserData(Token.CROSS);
    }

    private Pane[] getWinningLine() {
        Pane[] winningLine = getWinningLineForToken(Token.NAUGHT);
        if (winningLine == null)
            winningLine = getWinningLineForToken(Token.CROSS);
        return winningLine;
    }

    private Pane[] getWinningLineForToken(Token token) {
        NEXT_LINE:
        for (Pane[] lines : winningLines) {
            for (Pane pane : lines) {
                if (pane.getUserData() != token) {
                    continue NEXT_LINE;
                }
            }
            return lines;
        }
        return null;
    }

    private void drawLine(Pane[] winningLine) {
        Line line = new Line();
        line.setStartX(winningLine[0].getLayoutX() + centerX);
        line.setStartY(winningLine[0].getLayoutY() + centerY);
        line.setEndX(winningLine[2].getLayoutX() + centerX);
        line.setEndY(winningLine[2].getLayoutY() + centerY);
        line.setStrokeWidth(5);
        line.setStroke(Color.RED);
        gamePane.getChildren().add(line);
    }

    @FXML
    private void newGameButtonPressed(ActionEvent event) {
        startNewGame();
    }

    @FXML
    private void tokenRadioButtonSelected(ActionEvent event) {
        player1Token = (Token) tokenToggleGroup.getSelectedToggle().getUserData();
        player2Token =
                player1Token == Token.NAUGHT ? Token.CROSS : Token.NAUGHT;
    }

    @FXML
    private void pane00Clicked(MouseEvent event) {
        move(player1Token, pane00);
    }

    @FXML
    private void pane01Clicked(MouseEvent event) {
        move(player1Token, pane01);
    }

    @FXML
    private void pane02Clicked(MouseEvent event) {
        move(player1Token, pane02);
    }

    @FXML
    private void pane10Clicked(MouseEvent event) {
        move(player1Token, pane10);
    }

    @FXML
    private void pane11Clicked(MouseEvent event) {
        move(player1Token, pane11);
    }

    @FXML
    private void pane12Clicked(MouseEvent event) {
        move(player1Token, pane12);
    }

    @FXML
    private void pane20Clicked(MouseEvent event) {
        move(player1Token, pane20);
    }

    @FXML
    private void pane21Clicked(MouseEvent event) {
        move(player1Token, pane21);
    }

    @FXML
    private void pane22Clicked(MouseEvent event) {
        move(player1Token, pane22);
    }

}
