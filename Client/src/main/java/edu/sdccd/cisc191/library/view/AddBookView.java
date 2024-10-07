package edu.sdccd.cisc191.library.view;

import edu.sdccd.cisc191.library.Client;
import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
import edu.sdccd.cisc191.library.model.Book;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddBookView extends BaseView {
    private Label statusLabel;

    public AddBookView(Stage stage) {
        super(stage);
    }

    @Override
    protected String getTitleText() {
        return "Add New Book";
    }

    @Override
    protected VBox createForm() {
        TextField titleTextField = new TextField();
        titleTextField.setPromptText("Book Title");

        TextField authorTextField = new TextField();
        authorTextField.setPromptText("Author");

        Button submitButton = createSubmitBookButton(titleTextField, authorTextField);

        statusLabel = new Label("Status: Waiting for action...");

        VBox addBookForm = new VBox(10);
        addBookForm.getChildren()
                .addAll(titleTextField, authorTextField, createSubmitButtonBox(submitButton), statusLabel);

        return addBookForm;
    }

    private Button createSubmitBookButton(TextField titleTextField, TextField authorTextField) {
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            String title = titleTextField.getText();
            String author = authorTextField.getText();

            if (title.isEmpty() || author.isEmpty()) {
                statusLabel.setText("Title and Author are required!");
            }

            Book book = new Book(title, author);

            ResponseWrapper<?> response = Client.sendRequest(RequestType.ADD_BOOK, book);

            if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
                String successMessage = book.getTitle() + " successfully created!";
                statusLabel.setText(successMessage);
                titleTextField.clear();
                authorTextField.clear();
            } else {
                statusLabel.setText("Failed to create book!");
            }
        });

        return submitButton;
    }
}
