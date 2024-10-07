package edu.sdccd.cisc191.library.view;

import edu.sdccd.cisc191.library.Client;
import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckOutBookView extends BaseView {
    private Label statusLabel;
    private ChoiceBox<String> userChoiceBox;
    private ChoiceBox<String> bookChoiceBox;
    private Map<String, String> userMap = new HashMap<>();
    private Map<String, String> bookMap = new HashMap<>();

    public CheckOutBookView(Stage stage) {
        super(stage);
    }

    @Override
    protected String getTitleText() {
        return "Check Out Book";
    }

    @Override
    protected VBox createForm() {
        userMap = getUserMap();
        bookMap = getBookMap();

        userChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(userMap.keySet()));
        bookChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(bookMap.keySet()));

        HBox userBox = new HBox(10, new Label("Select User:"), userChoiceBox);
        userBox.setAlignment(Pos.CENTER);

        HBox bookBox = new HBox(10, new Label("Select Book:"), bookChoiceBox);
        bookBox.setAlignment(Pos.CENTER);

        Button submitButton = createCheckOutButton();
        statusLabel = new Label("Status: Waiting for action...");

        VBox checkOutForm = new VBox(10);
        checkOutForm.getChildren().addAll(userBox, bookBox, createSubmitButtonBox(submitButton), statusLabel);
        checkOutForm.setAlignment(Pos.CENTER);

        return checkOutForm;
    }

    private Button createCheckOutButton() {
        Button submitButton = new Button("Check Out Book");
        submitButton.setOnAction(event -> handleCheckOutAction());
        return submitButton;
    }

    private void handleCheckOutAction() {
        String selectedUserName = userChoiceBox.getValue();
        String selectedBookName = bookChoiceBox.getValue();

        if (selectedUserName.isEmpty() || selectedBookName.isEmpty()) {
            statusLabel.setText("User and Book are required!");
            return;
        }

        String userId = userMap.get(selectedUserName);
        String bookId = bookMap.get(selectedBookName);
        String[] loanData = {userId, bookId};

        ResponseWrapper<?> response = Client.sendRequest(RequestType.ADD_LOAN, loanData);

        if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
            String successMessage = selectedBookName + " successfully checked out to " + selectedUserName + "!";
            statusLabel.setText(successMessage);
        } else {
            statusLabel.setText("Failed to check out book!");
        }
    }

    private Map<String, String> getBookMap() {
        ResponseWrapper<?> response = Client.sendRequest(RequestType.GET_ALL_BOOKS, null);

        if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
            Object data = response.getData();
            if (data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Book> books = (Map<String, Book>) data;
                return books.values().stream()
                        .collect(Collectors.toMap(Book::getTitle, Book::getItemId));
            }
        }
        return Collections.emptyMap();
    }

    private Map<String, String> getUserMap() {
        ResponseWrapper<?> response = Client.sendRequest(RequestType.GET_ALL_USERS, null);

        if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
            Object data = response.getData();
            if (data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, User> users = (Map<String, User>) data;
                return users.values().stream()
                        .collect(Collectors.toMap(User::getName, User::getUserId));
            }
        }
        return Collections.emptyMap();
    }
}
