package edu.sdccd.cisc191.library.view;

import edu.sdccd.cisc191.library.Client;
import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.model.UserRole;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddUserView extends BaseView {
    private Label statusLabel;

    public AddUserView(Stage stage) {
        super(stage);
    }

    @Override
    protected String getTitleText() {
        return "Add New User";
    }

    @Override
    protected VBox createForm() {
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("User's Name");

        ChoiceBox<UserRole> userRoleChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(UserRole.MEMBER, UserRole.LIBRARIAN));
        userRoleChoiceBox.setValue(UserRole.MEMBER);

        HBox userRoleBox = new HBox(userRoleChoiceBox);
        userRoleBox.setAlignment(Pos.CENTER);

        Button submitButton = createSubmitUserButton(nameTextField, userRoleChoiceBox);

        statusLabel = new Label("Status: Waiting for action...");

        VBox addUserForm = new VBox(10);
        addUserForm.getChildren().addAll(nameTextField, userRoleBox, createSubmitButtonBox(submitButton), statusLabel);

        return addUserForm;
    }

    private Button createSubmitUserButton(TextField nameTextField, ChoiceBox<UserRole> userRoleChoiceBox) {
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            String name = nameTextField.getText();
            UserRole role = userRoleChoiceBox.getValue();

            if (name.isEmpty() || role.toString().isEmpty()) {
                statusLabel.setText("Name and Role are required!");
            }

            User user = new User(name, role);

            ResponseWrapper<?> response = Client.sendRequest(RequestType.ADD_USER, user);

            if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
                String successMessage = user.getName() + " successfully created!";
                statusLabel.setText(successMessage);
                nameTextField.clear();
                userRoleChoiceBox.setValue(UserRole.MEMBER);
            } else {
                statusLabel.setText("Failed to create user!");
            }
        });

        return submitButton;
    }
}
