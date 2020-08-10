package pl.adaroz.tictactoe.strategy;

import javafx.scene.layout.Pane;
import pl.adaroz.tictactoe.game.SetUpController;

import java.util.Random;

public class RandomChoice implements ComputerChoice {

    private final SetUpController setUpController;

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
