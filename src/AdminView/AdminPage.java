package AdminView;

import Data.Order;
import Data.User;
import Data.Book;
import Data.BookStore;
import Data.OrderStore;

import LoginPage.LoginPage;
import SellerView.BookListingManager;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Map;

import java.io.IOException;
import java.util.Comparator;

public class AdminPage extends Application {
    double width, height;
    private final User admin;

    ObservableList<User> users;

    public AdminPage(User admin) throws IOException {
        this.admin = admin;
        width = 1000;
        height = 650;
        UserManager.loadData();
        TransactionLog.loadData();

        users = FXCollections.observableArrayList(UserManager.getUserList().values());
    }

    public AdminPage() {
        this.admin = null;
    }

    @Override
    public void start(Stage stage){
        BorderPane root = new BorderPane();

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #750029;");

        ImageView logo = new ImageView(new Image(getClass().getResource("/LoginPage/logo(2).png").toExternalForm()));
        logo.setFitHeight(70);
        logo.setPreserveRatio(true);

        Label appName = new Label("Book Devils");
        appName.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        appName.setTextFill(Paint.valueOf("white"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("LOGOUT");
        logoutButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #750029; " +
                        "-fx-font-weight: bold;"
        );
        logoutButton.setOnAction(e -> {
            new LoginPage().start(new Stage());
            stage.close();
        });

        HBox leftHeader = new HBox(10, logo, appName);
        leftHeader.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(leftHeader, spacer, logoutButton);

        TabPane tabPane = new TabPane();
        tabPane.setTabMinWidth(120);
        tabPane.getTabs().addAll(
                createDashboardTab(),
                createAnalysisTab(),
                createTransactionTab()
        );

        root.setTop(topBar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle("BookDevils - Admin");
        stage.show();
    }

    private Tab createDashboardTab(){
        Tab tab = new Tab("Dashboard");
        tab.setClosable(false);

        // Add a listener to update content when the tab is selected
        tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Tab is selected
                updateDashboardTabContent(tab);
            }
        });

        // Initial content (can be empty or a loading message)
        updateDashboardTabContent(tab); // Call once to set initial content

        return tab;
    }

    private void updateDashboardTabContent(Tab tab) {
        VBox layout = createStyledLayout("Admin's Dashboard");

        // Dashboard Stats
        HBox displayRow = new HBox(
                createValueDisplay(String.valueOf(AnalysisTool.getTotalOrders()), "Total Orders"),
                createValueDisplay(String.valueOf(AnalysisTool.getTotalBooks(true)), "Total Sold"),
                createValueDisplay(String.valueOf(AnalysisTool.getTotalBooks()), "Total Products Listed")
        );
        displayRow.setSpacing(20);
        layout.getChildren().add(displayRow);

        // User Table Section
        Label userMgmt = createHeader("User Management");

        TableView<User> table = new TableView<>(FXCollections.observableArrayList(UserManager.getUserList().values()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No users found."));

        // --- Columns ---
        TableColumn<User, String> nameCol = new TableColumn<>("User Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("asuId"));

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> d.getValue().suspendedTextProperty());

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRole().toString()));

        TableColumn<User, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Hyperlink delete = new Hyperlink("Delete");
            private final Hyperlink suspend = new Hyperlink("Suspend/Unsuspend");

            {
                delete.setStyle("-fx-text-fill: red;");
                suspend.setStyle("-fx-text-fill: #750029;");

                //Set hyperlink actions:
                //delete:
                delete.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    try{
                        UserManager.removeUser(user.getAsuId());
                        updateDashboardTabContent(tab); // Refresh the table after deletion
                    }
                    catch (IOException ex){
                        ex.printStackTrace();
                    }
                });

                //suspend:
                suspend.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    try{
                        UserManager.setSuspended(user.getAsuId(), !user.isSuspended());
                        updateDashboardTabContent(tab); // Refresh the table after suspension
                    }
                    catch (IOException ex){
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, delete, suspend));
                }
            }
        });

        // --- Resize weights ---
        nameCol.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        statusCol.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        roleCol.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        actionCol.setMaxWidth(1f * Integer.MAX_VALUE * 30);

        table.getColumns().addAll(nameCol, statusCol, roleCol, actionCol);

        layout.getChildren().addAll(userMgmt, table);
        tab.setContent(layout);
    }


    private Tab createAnalysisTab(){
        Tab tab = new Tab("Analysis");
        tab.setClosable(false);

        // Add a listener to update content when the tab is selected
        tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Tab is selected
                updateAnalysisTabContent(tab);
            }
        });

        // Initial content (can be empty or a loading message)
        updateAnalysisTabContent(tab); // Call once to set initial content

        return tab;
    }

    private void updateAnalysisTabContent(Tab tab) {
        VBox layout = createStyledLayout("Analysis");

        Map<String, Book> bookMap;
        try {
            bookMap = BookStore.load();
        } catch (IOException e) {
            e.printStackTrace();
            bookMap = Map.of();
        }

        layout.getChildren().addAll(
                createDisplayRow("User Overview",
                        createValueDisplay(String.valueOf(AnalysisTool.getTotalUsers()), "Total Registered Users"),
                        createValueDisplay(String.valueOf(AnalysisTool.getTotalUsers(User.Role.SELLER)), "Total Sellers"),
                        createValueDisplay(String.valueOf(AnalysisTool.getTotalUsers(User.Role.BUYER)), "Total Buyers"),
                        createValueDisplay(String.valueOf(AnalysisTool.getTotalSuspendedUsers()), "Suspended Users")
                ),
                createDisplayRow("Book Overview",
                        createValueDisplay(String.valueOf(bookMap.size()), "Total Books Listed"),
                        createValueDisplay(String.valueOf(bookMap.values().stream().filter(Book::isSold).count()), "Books Sold"),
                        createValueDisplay(String.valueOf(bookMap.values().stream().filter(b -> !b.isSold()).count()), "Books Available"),
                        createValueDisplay(String.format("$%.2f", bookMap.values().stream().mapToDouble(Book::getSellingPrice).average().orElse(0)), "AVG. Selling Price")
                ),
                createDisplayRow("Sales Insights",
                        createValueDisplay(String.format("$%.1fk", OrderStore.getAll().stream().mapToDouble(Order::getTotalPrice).sum() / 1000.0), "Total Revenue"),
                        createValueDisplay(AnalysisTool.getHighestGrossingSeller(), "Highest-Grossing Seller"),
                        createValueDisplay(AnalysisTool.getBestSellingBook() != null ? AnalysisTool.getBestSellingBook().getTitle() : "n/a", "Top-Selling Book"),
                        createValueDisplay(AnalysisTool.getHighestGrossingCategory() != null ? AnalysisTool.getHighestGrossingCategory().toString() : "n/a", "Highest-Grossing Category")
                )
        );

        tab.setContent(layout);
    }


    private Tab createTransactionTab(){
        Tab tab = new Tab("Transactions");
        tab.setClosable(false);
        VBox layout = createStyledLayout("Transactions");

        TableView<Order> table = new TableView<>(FXCollections.observableArrayList(TransactionLog.getOrderList()));

        TableColumn<Order, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTimestamp().toString()));

        TableColumn<Order, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBuyer().getAsuId()));

        TableColumn<Order, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(d -> new SimpleStringProperty("Purchase"));
        actionCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #750029;");
                }
            }
        });

        TableColumn<Order, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(d -> new SimpleStringProperty("Bought books from listing"));

        table.getColumns().addAll(timeCol, userCol, actionCol, descCol);
        layout.getChildren().add(table);

        tab.setContent(layout);
        return tab;
    }

    private VBox createStyledLayout(String titleText) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));

        Label heading = new Label(titleText);
        heading.setFont(Font.font("Arial", 30));
        heading.setTextFill(Paint.valueOf("#750029"));
        layout.getChildren().add(heading);

        return layout;
    }

    private Label createHeader(String str){
        Label header = new Label(str);
        header.setFont(new Font(18));
        return header;
    }

    private VBox createValueDisplay(String val, String desc){
        Label value = new Label(val);
        value.setFont(new Font(28));
        value.setTextFill(Paint.valueOf("#750029"));
        Label description = new Label(desc);
        description.setFont(new Font(14));
        description.setTextFill(Paint.valueOf("#444"));

        VBox box = new VBox(value, description);
        box.setStyle("-fx-border-color: #ccc; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-background-color: white; -fx-padding: 20;");
        box.setMinWidth((width - 160) / 4);
        return box;
    }

    private VBox createDisplayRow(String headerStr, VBox box1, VBox box2, VBox box3, VBox box4){
        Label header = createHeader(headerStr);
        HBox row = new HBox(20, box1, box2, box3, box4);
        return new VBox(10, header, row);
    }

    public static void main(String[] args){
        launch(args);
    }
}
