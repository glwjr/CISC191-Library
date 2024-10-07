package edu.sdccd.cisc191.library;

import edu.sdccd.cisc191.library.message.RequestType;
import edu.sdccd.cisc191.library.message.RequestWrapper;
import edu.sdccd.cisc191.library.message.ResponseStatus;
import edu.sdccd.cisc191.library.message.ResponseWrapper;
import edu.sdccd.cisc191.library.model.SceneName;
import edu.sdccd.cisc191.library.view.AddBookView;
import edu.sdccd.cisc191.library.view.AddUserView;
import edu.sdccd.cisc191.library.view.CheckOutBookView;
import edu.sdccd.cisc191.library.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client extends Application {
    private static final Map<SceneName, Scene> scenes = new HashMap<>();

    public static Map<SceneName, Scene> getScenes() {
        return scenes;
    }

    public static <T> ResponseWrapper<?> sendRequest(RequestType requestType, T payload) {
        String HOST = "localhost";
        int PORT = 8080;

        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            RequestWrapper<T> request = new RequestWrapper<>(requestType, payload);
            out.writeObject(request);

            return (ResponseWrapper<?>) in.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseWrapper<>(ResponseStatus.ERROR, e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        scenes.put(SceneName.MAIN, new MainView(stage).getScene());
        scenes.put(SceneName.ADD_USER, new AddUserView(stage).getScene());
        scenes.put(SceneName.ADD_BOOK, new AddBookView(stage).getScene());
        scenes.put(SceneName.CHECK_OUT_BOOK, new CheckOutBookView(stage).getScene());

        stage.setTitle("Library Management System");
        stage.setScene(scenes.get(SceneName.MAIN));
        stage.show();
    }
}
