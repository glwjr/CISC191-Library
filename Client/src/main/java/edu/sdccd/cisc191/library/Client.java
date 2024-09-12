package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.RequestWrapper;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
import edu.sdccd.cisc191.library.model.Book;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Application {
    private TextField bookTitleField;
    private TextField authorField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Book Title:");
        bookTitleField = new TextField();

        Label authorLabel = new Label("Author:");
        authorField = new TextField();

        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> onAddBook());

        statusLabel = new Label("Status: Waiting for action...");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, bookTitleField, authorLabel, authorField, addButton, statusLabel);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }

    private void onAddBook() {
        String bookTitle = bookTitleField.getText();
        String author = authorField.getText();

        if (bookTitle.isEmpty() || author.isEmpty()) {
            statusLabel.setText("Status: Please fill in all fields.");
            return;
        }

        Book book = new Book(bookTitle, author);

        RequestWrapper<Book> request = new RequestWrapper<>(RequestType.ADD_BOOK, book);

        try (Socket socket = new Socket("localhost", 3000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();

            ResponseWrapper<?> response = (ResponseWrapper<?>) in.readObject();

            if (response.getResponseStatus() == ResponseStatus.SUCCESS) {
                statusLabel.setText("Status: Book added successfully!");
            } else {
                statusLabel.setText("Status: Failed to add book. " + response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Status: Error connecting to server.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
