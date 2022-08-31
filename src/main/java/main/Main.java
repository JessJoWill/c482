package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/** Javadoc can be found at <code>src/main/javadoc/index.html</code>. */

/**
 * Creates an app that manages parts and products inventory. FUTURE ENHANCEMENT: When an inventory level for a part or product reaches a pre-determined low, the app would automatically trigger a re-order from the parts department or the external manufacturer.
 * @author Jessica Williams
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads the Main Screen.
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/main-form-view.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Scene scene = new Scene(root, 1100, 475);
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
