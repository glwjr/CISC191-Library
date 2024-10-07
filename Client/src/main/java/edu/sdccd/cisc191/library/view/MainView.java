package edu.sdccd.cisc191.library.view;

import edu.sdccd.cisc191.library.controller.ViewController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView {
    private final Stage stage;

    public MainView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        ViewController viewController = new ViewController(stage);

        Text title = new Text("Library");
        title.setFont(new Font(30));

        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(e -> viewController.handleOnPressAddUserButton());

        Button addBookButton = new Button("Add Book");
        addBookButton.setOnAction(e -> viewController.handleOnPressAddBookButton());

        Button checkOutButton = new Button("Check Out Book");
        checkOutButton.setOnAction(e -> viewController.handleOnPressCheckOutBookButton());

        Button exitButton = new Button("Exit");
        exitButton.setOnMousePressed(e -> stage.close());

        VBox layout = new VBox(10);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.getChildren().addAll(title, addUserButton, addBookButton, checkOutButton, exitButton);

        return new Scene(layout, 400, 300);
    }
}
