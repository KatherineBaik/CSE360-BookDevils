package AdminView;

import Data.User;

import LoginPage.LoginPage;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Very small Admin dashboard so LoginPage can launch it.
 * Flesh this out later with tables & charts.
 */
public class AdminPage extends Application {
    double width, height;

    private final User admin;          // logged-in admin

    /* Called by LoginPage */
    public AdminPage(User admin) throws IOException {
        this.admin = admin;

        width = 800;
        height = 500;

        //load data

        // TransactionLog.loadData(); TODO: CURRENTLY DOES NOT WORK!!!
        UserManager.loadData();
    }

    /* No-arg constructor for JavaFX launcher – never used directly */
    public AdminPage() { this.admin = null; }

    @Override
    public void start(Stage stage){
        //Create the layout
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.TOP_RIGHT);

        //------------------
        //Create the tabs
        Tab dashboardTab = createDashboardTab();
        Tab analysisTab = createAnalysisTab();
        Tab transactionsTab = createTransactionTab();


        //------------------
        //Create tabPane and add all the tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabMinWidth(60);
        tabPane.setMinWidth(width - 80);

        tabPane.getTabs().addAll(dashboardTab, analysisTab, transactionsTab);
        mainLayout.getChildren().add(tabPane);

        //------------------
        //logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setPadding(new Insets(10,20,10,20));

        mainLayout.getChildren().add(logoutButton);

        logoutButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.start(new Stage());
            stage.close();
        });

        //------------------
        //set and show scene
        Scene scene = new Scene(mainLayout, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /** To make it easier to create popups */
    private void createPopupMessage(String message){
        Stage stage = new Stage();

        Label label = new Label(message);

        VBox layout = new VBox(label);
        layout.setMinSize(200,150);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }

    //------------------------------------
    // Creating the tabs
    //------------------------------------

    private Tab createDashboardTab(){
        Tab dashboardTab = new Tab("Dashboard");
        dashboardTab.setClosable(false);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));

        //overviews at the top

        //table of users
        TableView<User> userTable = new TableView<>();
        ObservableList<User> users = FXCollections.observableArrayList(UserManager.getUserList().values());
        userTable.setItems(users);

        TableColumn<User, String> asuIDCol= new TableColumn<>("ID");
        asuIDCol.setCellValueFactory(new PropertyValueFactory<>("asuId"));
        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> {
            if(d.getValue().isSuspended()) return new SimpleStringProperty("Suspended");
            else return new SimpleStringProperty("Active");
        });
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getRole().toString())
        );

        userTable.getColumns().addAll(asuIDCol, statusCol, roleCol);
        layout.getChildren().add(userTable);

        //table of books?


        dashboardTab.setContent(layout);
        return dashboardTab;
    }

    private Tab createAnalysisTab(){
        Tab analysisTab = new Tab("Analysis");
        analysisTab.setClosable(false);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));

        // User Overview
        Label test = new Label("This is a test");
        test.setFont(new Font(20));
        Label test2 = new Label("SSS");

        layout.getChildren().addAll(test, test2);

        // Book Overview

        // Sales Insights


        analysisTab.setContent(layout);
        return analysisTab;
    }

    private Tab createTransactionTab(){
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setClosable(false);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));

        //create the table of orders


        transactionsTab.setContent(layout);
        return transactionsTab;
    }



    public static void main(String[] args){
        launch(args);
    }
}
