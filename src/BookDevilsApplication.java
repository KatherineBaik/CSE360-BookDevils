import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookDevilsApplication extends Application {
    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(200,200);
        VBox mainLayout = new VBox(canvas);

        Scene mainScene = new Scene(mainLayout);

        stage.setScene(mainScene);
        stage.setTitle("Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
