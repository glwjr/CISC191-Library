package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.Client;
import edu.sdccd.cisc191.library.model.SceneName;
import javafx.stage.Stage;

public class ViewController {
    private final Stage stage;

    public ViewController(Stage stage) {
        this.stage = stage;
    }

    public void handleOnPressAddUserButton() {
        stage.setScene(Client.getScenes().get(SceneName.ADD_USER));
    }

    public void handleOnPressAddBookButton() {
        stage.setScene(Client.getScenes().get(SceneName.ADD_BOOK));
    }

    public void handleOnPressCheckOutBookButton() {
        stage.setScene(Client.getScenes().get(SceneName.CHECK_OUT_BOOK));
    }

    public void handleOnPressMainMenuButton() {
        stage.setScene(Client.getScenes().get(SceneName.MAIN));
    }
}
