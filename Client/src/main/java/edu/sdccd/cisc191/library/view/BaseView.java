package edu.sdccd.cisc191.library.view;

import edu.sdccd.cisc191.library.controller.ViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class BaseView {
    protected final Stage stage;
    protected final ViewController viewController;

    public BaseView(Stage stage) {
        this.stage = stage;
        this.viewController = new ViewController(stage);
    }

    public Scene getScene() {
        Text title = new Text(getTitleText() + "\n");
        title.setFont(new Font(20));

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        VBox form = createForm();
        HBox buttonMenu = createButtonMenu();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(titleBox);
        root.setCenter(form);
        root.setBottom(buttonMenu);

        return new Scene(root, 400, 300);
    }

    protected abstract String getTitleText();

    protected abstract VBox createForm();

    protected HBox createSubmitButtonBox(Button submitButton) {
        HBox submitButtonBox = new HBox(submitButton);
        submitButtonBox.setAlignment(Pos.CENTER);
        return submitButtonBox;
    }

    protected HBox createButtonMenu() {
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> viewController.handleOnPressMainMenuButton());

        Button exitButton = new Button("Exit");
        exitButton.setOnMousePressed(e -> stage.close());

        HBox buttonMenu = new HBox(10);
        buttonMenu.getChildren().addAll(mainMenuButton, exitButton);
        buttonMenu.setAlignment(Pos.CENTER);
        return buttonMenu;
    }
}
