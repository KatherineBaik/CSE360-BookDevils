package SellerView;

import Data.Book;
import Data.User;
import LoginPage.LoginPage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextInputDialog;   // ***ADDED***
import javafx.scene.control.ChoiceDialog; // ***ADDED*** 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;



import java.util.List;
import java.util.Optional;    // ***ADDED***
import SellerView.BookListingManager;         // ***ADDED***
import javafx.beans.property.SimpleStringProperty;  // ***ADDED***
/**
 * Minimal, functional Seller dashboard.
 * <p>
 * Keeps the original controller logic (viewListings / listBook) but now
 * also launches its own JavaFX window so LoginPage can call
 *   new SellerPage(user).start(new Stage());
 */
public class SellerPage extends Application {

    /* ------------------------------------------------------------------
     *  State
     * ------------------------------------------------------------------ */
    private final User seller;                       // logged-in user
    private TableView<Book> table;                   // listing view

    /* ------------------------------------------------------------------
     *  Constructors
     * ------------------------------------------------------------------ */

    /** Called by LoginPage when the SELLER logs in. */
    public SellerPage(User seller) {
        this.seller   = seller;
    }

    // no-arg constructor required by JavaFX launcher, never used here
    public SellerPage() {
        this.seller  = null;
    }

    /* ------------------------------------------------------------------
     *  JavaFX entry point
     * ------------------------------------------------------------------ */
    @Override
    public void start(Stage stage) {
        // ---------------- Top Bar ----------------
        ImageView logo = new ImageView(new Image(getClass().getResource("/LoginPage/logo(2).png").toExternalForm()));
        logo.setFitHeight(70);
        logo.setPreserveRatio(true);

        Label title = new Label("Book Devils");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Paint.valueOf("white"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("LOGOUT");
        logoutButton.setStyle("-fx-background-color: white; -fx-text-fill: #750029; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> {
            try {
                new LoginPage().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox topBar = new HBox(10, logo, title, spacer, logoutButton);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #750029;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // ---------------- Table ----------------
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(d -> d.getValue().titleProperty());

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(d -> d.getValue().authorProperty());

        TableColumn<Book, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(d -> d.getValue().priceProperty());

        // ***ADDED***: Category & Condition columns
        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(d ->
          new SimpleStringProperty(d.getValue().getCategory().toString())
       );

       TableColumn<Book, String> conditionCol = new TableColumn<>("Condition");
       conditionCol.setCellValueFactory(d ->
           new SimpleStringProperty(d.getValue().getCondition().toString())
       );

        // ***ADDED***: Actions column for Edit/Delete
        TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn   = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setOnAction(e -> {
                    Book b = getTableView().getItems().get(getIndex());
                    showEditBookDialog(b);
                });
                deleteBtn.setOnAction(e -> {
                    Book b = getTableView().getItems().get(getIndex());
                    BookListingManager.deleteListing(b.getId());
                    refreshTable();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editBtn, deleteBtn));
            }
        });

        // ***CHANGED***: include actionCol in the table setup
    table.getColumns().setAll(titleCol, authorCol, priceCol, actionCol);
       table.getColumns().setAll(
           titleCol, authorCol, priceCol, categoryCol, conditionCol, actionCol
       );
        refreshTable();

        // ---------------- Footer ----------------
        Button listBtn = new Button("List New Book");
        listBtn.setStyle("-fx-background-color: #750029; -fx-text-fill: white; -fx-font-weight: bold;");
        // ***CHANGED***: now launches a real Add-Book dialog instead of mockListBook()
        listBtn.setOnAction(e -> {
            Book newBook = showAddBookDialog();
            if (newBook != null) listBook(newBook);
        });

        HBox footer = new HBox(listBtn);
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER_LEFT);


        // ---------------- Layout ----------------
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(table);
        root.setBottom(footer);

        stage.setTitle("Book Devils – Seller Dashboard");
        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }


    /* ------------------------------------------------------------------
     *  Controller-style API (unchanged from your original file)
     * ------------------------------------------------------------------ */

    /** Returns all books currently listed by *this* seller.
     *  NOTE: Only shows books that haven't been sold. */
    public List<Book> viewListings() {
        return BookListingManager.getAllListingsNotSold().stream()
                      .filter(b -> b.getSellerId().equals(seller.getAsuId()))
                      .toList();
    }

    /** Adds a new listing on behalf of the seller. */
    public void listBook(Book book) {
        if (!validateBook(book)) {
            new Alert(Alert.AlertType.ERROR,
                "Fill Title, Author, Price (>0), Category & Condition!"
            ).showAndWait();
            return;
         }
        // ***CHANGED***: rely on Book.calculateSellingPrice() in constructor/condition setter
        BookListingManager.createListing(book);
        refreshTable();
    }



    /* ------------------------------------------------------------------
     *  Helpers
     * ------------------------------------------------------------------ */

    /** Repopulate the TableView from current data. */
   // --- Table Refresh ---
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(viewListings()));
    }

    // --- Validation ---
    private boolean validateBook(Book book) {
        return book.getTitle() != null && !book.getTitle().trim().isEmpty()
            && book.getAuthor() != null && !book.getAuthor().trim().isEmpty()
            && book.getOriginalPrice() > 0
        + **&& book.getCategory() != null**
        + **&& book.getCondition() != null;**
    }

    // --- Add‐Book Dialog with dropdowns ---
    private Book showAddBookDialog() {
        // Title
        TextInputDialog tDlg = new TextInputDialog();
        tDlg.setTitle("New Book: Title");
        tDlg.setHeaderText("Enter Title:");
        Optional<String> ot = tDlg.showAndWait();
        if (ot.isEmpty() || ot.get().trim().isEmpty()) return null;
        String title = ot.get().trim();

        // Author
        TextInputDialog aDlg = new TextInputDialog();
        aDlg.setTitle("New Book: Author");
        aDlg.setHeaderText("Enter Author:");
        Optional<String> oa = aDlg.showAndWait();
        if (oa.isEmpty() || oa.get().trim().isEmpty()) return null;
        String author = oa.get().trim();

        // Price
        TextInputDialog pDlg = new TextInputDialog();
        pDlg.setTitle("New Book: Price");
        pDlg.setHeaderText("Enter Cost Price:");
        pDlg.setContentText("e.g. 9.99");
        Optional<String> op = pDlg.showAndWait();
        double cost;
        try {
            cost = Double.parseDouble(op.orElse("").trim());
            if (cost <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Invalid price").showAndWait();
            return null;
        }

        // ***ADDED***: Category dropdown
+       **ChoiceDialog<Book.Category> catDlg = new ChoiceDialog<>(**
+       **    Book.Category.OTHER, Book.Category.values()**
+       **);**
+       **catDlg.setTitle("New Book: Category");**
+       **catDlg.setHeaderText("Select Category:");**
+       **Optional<Book.Category> oc = catDlg.showAndWait();**
+       **if (oc.isEmpty()) return null;**
+       **Book.Category category = oc.get();**

        // ***ADDED***: Condition dropdown
+       **ChoiceDialog<Book.Condition> condDlg = new ChoiceDialog<>(**
+       **    Book.Condition.NEW, Book.Condition.values()**
+       **);**
+       **condDlg.setTitle("New Book: Condition");**
+       **condDlg.setHeaderText("Select Condition:");**
+       **Optional<Book.Condition> od = condDlg.showAndWait();**
+       **if (od.isEmpty()) return null;**
+       **Book.Condition condition = od.get();**

        // Construct Book (constructor auto–calculates sellingPrice by condition)
        return new Book(
            title,
            author,
            2025,
        +   **category,    // ***CHANGED***  
        +   **condition,   // ***CHANGED***  
            cost,
            seller.getAsuId()
        );
    }

    // --- Edit‐Book Dialog for all fields ---
    private void showEditBookDialog(Book book) {
        // Title
        TextInputDialog tDlg = new TextInputDialog(book.getTitle());
        tDlg.setTitle("Edit Book: Title");
        tDlg.setHeaderText("New Title:");
        tDlg.showAndWait().ifPresent(book::setTitle);

        // Author
        TextInputDialog aDlg = new TextInputDialog(book.getAuthor());
        aDlg.setTitle("Edit Book: Author");
        aDlg.setHeaderText("New Author:");
        aDlg.showAndWait().ifPresent(book::setAuthor);

        // Price
        TextInputDialog pDlg = new TextInputDialog(
            String.valueOf(book.getOriginalPrice())
        );
        pDlg.setTitle("Edit Book: Price");
        pDlg.setHeaderText("New Cost Price:");
        pDlg.setContentText("e.g. 12.50");
        Optional<String> op = pDlg.showAndWait();
        op.ifPresent(s -> {
            try {
                double v = Double.parseDouble(s.trim());
                if (v > 0) book.setOriginalPrice(v);
                else throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid price").showAndWait();
            }
        });

        // ***ADDED***: Category
+       **ChoiceDialog<Book.Category> catDlg = new ChoiceDialog<>(**
+       **    book.getCategory(), Book.Category.values()**
+       **);**
+       **catDlg.setTitle("Edit Book: Category");**
+       **catDlg.setHeaderText("New Category:");**
+       **catDlg.showAndWait().ifPresent(book::setCategory);**

        // ***ADDED***: Condition
+       **ChoiceDialog<Book.Condition> condDlg = new ChoiceDialog<>(**
+       **    book.getCondition(), Book.Condition.values()**
+       **);**
+       **condDlg.setTitle("Edit Book: Condition");**
+       **condDlg.setHeaderText("New Condition:");**
+       **condDlg.showAndWait().ifPresent(book::setCondition);**

        // Persist changes (condition setter already recalculates sellingPrice)
        BookListingManager.updateListing(book.getId(), book);
        refreshTable();
    }
}
