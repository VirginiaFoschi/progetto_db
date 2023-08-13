package app;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends Application {

    Stage stage;

    public JavaFXApp(){}

    private static void run(final String[] args) {
        launch(args);
    }

    /**
     * Entry point class.
     */
    public static final class Main {
        private Main() {
            // the constructor will never be called directly.
        }

        /**
         * Program's entry point.
         * @param args
         */
        public static void main(final String... args) {
            JavaFXApp.run(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Controller(this);
        this.stage=primaryStage;
        view();
    }

    public void insertFilm() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("insertFilm2.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void view() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("view.fxml"));
        Scene scene = new Scene(root);
        Stage st = new Stage();
        st.setScene(scene);
        st.show();
    }

    public void showRates() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("prices.fxml"));
        Scene scene = new Scene(root);
        Stage st = new Stage();
        st.setScene(scene);
        st.show();
    }

    
}