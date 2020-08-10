package pl.adaroz.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardController {

    private final GameController gameController = new GameController(this);

    @FXML
    public BorderPane mainPanel;
    @FXML
    public GridPane board;
    @FXML
    public TitledPane markTitledPane;

    @FXML
    public Pane field00;
    @FXML
    public Pane field01;
    @FXML
    public Pane field02;
    @FXML
    public Pane field10;
    @FXML
    public Pane field11;
    @FXML
    public Pane field12;
    @FXML
    public Pane field20;
    @FXML
    public Pane field21;
    @FXML
    public Pane field22;

    @FXML
    public ToggleGroup playersToggleGroup;
    @FXML
    public RadioButton onePlayerRadioButton;
    @FXML
    public RadioButton twoPlayersRadioButton;
    @FXML
    public ToggleGroup markToggleGroup;
    @FXML
    public RadioButton noughtRadioButton;
    @FXML
    public RadioButton crossRadioButton;
    @FXML
    public Button newGameButton;

    public void initialize() {
        gameController.initialize();
    }

    @FXML
    private void playersRadioButtonSelected() {
        gameController.playersRadioButtonSelected();
    }

    @FXML
    private void newGameButtonPressed() {
        gameController.newGameButtonPressed();
    }

    @FXML
    private void markRadioButtonSelected() {
        gameController.markRadioButtonSelected();
    }

    @FXML
    private void field00Clicked() {
        gameController.fieldClicked(field00);
    }

    @FXML
    private void field01Clicked() {
        gameController.fieldClicked(field01);
    }

    @FXML
    private void field02Clicked() {
        gameController.fieldClicked(field02);
    }

    @FXML
    private void field10Clicked() {
        gameController.fieldClicked(field10);
    }

    @FXML
    private void field11Clicked() {
        gameController.fieldClicked(field11);
    }

    @FXML
    private void field12Clicked() {
        gameController.fieldClicked(field12);
    }

    @FXML
    private void field20Clicked() {
        gameController.fieldClicked(field20);
    }

    @FXML
    private void field21Clicked() {
       gameController.fieldClicked(field21);
    }

    @FXML
    private void field22Clicked() {
        gameController.fieldClicked(field22);
    }

}
