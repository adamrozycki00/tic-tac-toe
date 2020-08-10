package pl.adaroz.tictactoe;

import javafx.scene.layout.Pane;

import java.util.Random;

public class RandomChoice implements ComputerChoice {

    private SetUpController setUpController;

    public RandomChoice(SetUpController setUpController) {
        this.setUpController = setUpController;
    }

    @Override
    public Pane getField() {
        if (setUpController.getEmptyFields().isEmpty())
            return null;
        int randomIndex = new Random().nextInt(setUpController.getEmptyFields().size());
        return setUpController.getEmptyFields().get(randomIndex);
    }

}
