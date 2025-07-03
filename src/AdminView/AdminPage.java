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
import javafx.scene.paint.Paint;
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
        logoutButton.setMinSize(70, 40);

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
        stage.setTitle("BookDevils - Admin Page");
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

        Label mainHeader = new Label("Admin's Dashboard");
        mainHeader.setFont(new Font(30));
        layout.getChildren().add(mainHeader);

        //overviews at the top
        VBox display1 = createValueDisplay(Integer.toString(AnalysisTool.getTotalOrders()), "Total Orders");
        VBox display2 = createValueDisplay(Integer.toString(AnalysisTool.getTotalBooks(true)), "Total Sold");
        VBox display3 = createValueDisplay(Integer.toString(AnalysisTool.getTotalBooks()), "Total Products Listed");

        display1.setMinWidth((width-160) / 3);
        display2.setMinWidth((width-160) / 3);
        display3.setMinWidth((width-160) / 3);

        HBox displayRow = new HBox(display1, display2, display3);
        displayRow.setSpacing(14);
        displayRow.setPadding(new Insets(5));

        layout.getChildren().add(displayRow);

        //table of users
        Label header1 = createHeader("User Management");
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
        layout.getChildren().addAll(header1, userTable);

        //table of books?


        dashboardTab.setContent(layout);
        return dashboardTab;
    }

    private Tab createAnalysisTab(){
        Tab analysisTab = new Tab("Analysis");
        analysisTab.setClosable(false);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));

        Label mainHeader = new Label("Analysis");
        mainHeader.setFont(new Font(30));
        layout.getChildren().add(mainHeader);

        // User Overview
        VBox userOverviewRow = createDisplayRow("User Overview",
                createValueDisplay(Integer.toString(AnalysisTool.getTotalUsers()), "Total Registered Users"),
                createValueDisplay(Integer.toString(AnalysisTool.getTotalUsers(User.Role.SELLER)), "Total sellers"),
                createValueDisplay(Integer.toString(AnalysisTool.getTotalUsers(User.Role.BUYER)), "Total buyers"),
                createValueDisplay(Integer.toString(AnalysisTool.getTotalSuspendedUsers()), "Suspended users"));

        userOverviewRow.setPadding(new Insets(10));

        layout.getChildren().addAll(userOverviewRow);

        // Book Overview
        VBox bookOverviewRow = createDisplayRow("Book Overview",
                createValueDisplay(Integer.toString(AnalysisTool.getTotalBooks()), "Total Books Listed"),
                createValueDisplay(Integer.toString(AnalysisTool.getTotalBooks(true)), "Books Sold"),
                createValueDisplay(Integer.toString(AnalysisTool.getTotalBooks(false)), "Books Available"),
                createValueDisplay(String.format("%.2f", AnalysisTool.getAvgBookPrice()), "AVG. Selling Price"));

        bookOverviewRow.setPadding(new Insets(10));

        layout.getChildren().addAll(bookOverviewRow);

        // Sales Insights
        double revenue = AnalysisTool.getTotalRevenue(); // What happens when the revenue gets too large?

        VBox salesInsightsRow = createDisplayRow("Sales Insights",
                createValueDisplay(String.format("%.2f", revenue),"Total Revenue"),
                createValueDisplay("n/a","Highest Grossing Seller"),
                //TODO: CHANGE WHEN FUNCTIONALITY GETS ADDED
                createValueDisplay(/*AnalysisTool.getBestSellingBook().getTitle()*/ "n/a", "Top-Selling Book"),
                createValueDisplay(/*AnalysisTool.getHighestGrossingCategory().toString()*/ "n/a","Highest-Grossing Category"));

        salesInsightsRow.setPadding(new Insets(10));

        layout.getChildren().addAll(salesInsightsRow);

        analysisTab.setContent(layout);
        return analysisTab;
    }

    private Tab createTransactionTab(){
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setClosable(false);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));

        Label mainHeader = new Label("Transactions");
        mainHeader.setFont(new Font(30));
        layout.getChildren().add(mainHeader);

        //create the table of orders


        transactionsTab.setContent(layout);
        return transactionsTab;
    }

    // OTHER UI DETAILS
    private Label createHeader(String str){
        Label header = new Label(str);
        header.setFont(new Font(18));
        return header;
    }

    private VBox createValueDisplay(String val, String desc){
        Label value = new Label(val);
        value.setFont(new Font(24));
        value.setTextFill(Paint.valueOf("MAROON"));
        Label description = new Label(desc);

        VBox layout = new VBox(value, description);
        layout.setMinWidth((width - 140) / 4);
        return layout;
    }

    private VBox createDisplayRow(String headerStr, VBox box1, VBox box2, VBox box3, VBox box4){
        Label header = createHeader(headerStr);

        HBox displayRow = new HBox(box1, box2, box3, box4);
        displayRow.setSpacing(14);

        return new VBox(header, displayRow);
    }

    public static void main(String[] args){
        launch(args);
    }
}
