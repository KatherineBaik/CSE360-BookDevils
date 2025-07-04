import Data.BookStore;
import Data.OrderStore;
import LoginPage.LoginPage;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class BookDevilsApplication extends Application {
    @Override
    public void start(Stage stage) {
        LoginPage loginPage = new LoginPage();
        loginPage.start(stage);
    }

    public static void main(String[] args) {
        try {
            BookStore.load();    // touch-create books.txt
            OrderStore.load();   // touch-create orders.txt

        } catch (IOException ex) {
            ex.printStackTrace();      // or show an Alert dialog
        }
        launch(args);
    }
}


