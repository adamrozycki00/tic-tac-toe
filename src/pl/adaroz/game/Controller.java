package pl.adaroz.game;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Controller {

    private Token player1Token = Token.NAUGHT;
    private Token player2Token = Token.CROSS;

    private enum Token {
        NAUGHT, CROSS
    }

    private double naughtCenterX = 28.5;
    private double naughtCenterY = 29;
    private double naughtRadius = 18.81;
    private double crossPoint1X = 9.5;
    private double crossPoint1Y = 10.0;
    private double crossPoint2X = 47.5;
    private double crossPoint2Y = 10.0;
    private double crossPoint3X = 9.5;
    private double crossPoint3Y = 48.0;
    private double crossPoint4X = 47.5;
    private double crossPoint4Y = 48.0;

    @FXML
    private Button newGameButton;

    @FXML
    private RadioButton noughtRadioButton;

    @FXML
    private ToggleGroup tokenToggleGroup;

    @FXML
    private RadioButton crossRadioButton;

    @FXML
    private GridPane boardGridPane;

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

    public void initialize() {
        noughtRadioButton.setUserData(Token.NAUGHT);
        crossRadioButton.setUserData(Token.CROSS);
    }

    private void startNewGame() {
        for (Node node : boardGridPane.getChildren())
            if (node instanceof Pane)
                ((Pane) node).getChildren().clear();
        resizeTokens();
    }

    private void resizeTokens() {
        naughtCenterX = pane00.getWidth() / 2;
        naughtCenterY = pane00.getHeight() / 2;
        naughtRadius = Math.min(pane00.getWidth(), pane00.getHeight()) / 3;

        crossPoint1X = Math.max(pane00.getWidth() / 6, pane00.getWidth() / 2 - naughtRadius);
        crossPoint1Y = Math.max(pane00.getHeight() / 6, pane00.getHeight() / 2 - naughtRadius);
        crossPoint2X = Math.min(pane00.getWidth() * 5 / 6, pane00.getWidth() / 2 + naughtRadius);
        crossPoint2Y = Math.max(pane00.getHeight() / 6, pane00.getHeight() / 2 - naughtRadius);
        crossPoint3X = Math.max(pane00.getWidth() / 6, pane00.getWidth() / 2 - naughtRadius);
        crossPoint3Y = Math.min(pane00.getHeight() * 5 / 6, pane00.getHeight() / 2 + naughtRadius);
        crossPoint4X = Math.min(pane00.getWidth() * 5 / 6, pane00.getWidth() / 2 + naughtRadius);
        crossPoint4Y = Math.min(pane00.getHeight() * 5 / 6, pane00.getHeight() / 2 + naughtRadius);
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
        addToken(player1Token, pane00);
    }

    @FXML
    private void pane01Clicked(MouseEvent event) {
        addToken(player1Token, pane01);
    }

    @FXML
    private void pane02Clicked(MouseEvent event) {
        addToken(player1Token, pane02);
    }

    @FXML
    private void pane10Clicked(MouseEvent event) {
        addToken(player1Token, pane10);
    }

    @FXML
    private void pane11Clicked(MouseEvent event) {
        addToken(player1Token, pane11);
    }

    @FXML
    private void pane12Clicked(MouseEvent event) {
        addToken(player1Token, pane12);
    }

    @FXML
    private void pane20Clicked(MouseEvent event) {
        addToken(player1Token, pane20);
    }

    @FXML
    private void pane21Clicked(MouseEvent event) {
        addToken(player1Token, pane21);
    }

    @FXML
    private void pane22Clicked(MouseEvent event) {
        addToken(player1Token, pane22);
    }

    private void addToken(Token player, Pane pane) {
        if (player == Token.NAUGHT)
            addNought(pane);
        else
            addCross(pane);
    }

    private void addNought(Pane pane) {
        Circle nought = new Circle(naughtCenterX, naughtCenterY, naughtRadius);
        nought.setFill(Color.TRANSPARENT);
        nought.strokeProperty().set(Color.BLACK);
        pane.getChildren().add(nought);
    }

    private void addCross(Pane pane) {
        pane.getChildren().add(new Line(crossPoint1X, crossPoint1Y, crossPoint4X, crossPoint4Y));
        pane.getChildren().add(new Line(crossPoint2X, crossPoint2Y, crossPoint3X, crossPoint3Y));
    }

}
