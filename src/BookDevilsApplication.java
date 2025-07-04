import AdminView.TransactionLog;
import Data.BookStore;
import Data.Order;
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

             // ***CHANGED***: use TransactionLog.loadData() instead of manual loop
            TransactionLog.loadData();
        } catch (IOException ex) {
            ex.printStackTrace();      // or show an Alert dialog
        }

        launch(args);

        // ***ADDED***: persist TransactionLog after application exits
        try {
            TransactionLog.saveData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
