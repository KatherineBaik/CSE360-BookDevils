package SellerView;

import Data.Book;
import Data.User;
import LoginPage.LoginPage;
import SellerView.BookListingManager;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

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
        BookListingManager.createListing(book);
        refreshTable();
    }

    /* ------------------------------------------------------------------
     *  Helpers
     * ------------------------------------------------------------------ */

    /** Repopulate the TableView from current data. */
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(viewListings()));
    }

    // --- Validation ---
    private boolean validateBook(Book book) {
        return book.getTitle() != null && !book.getTitle().trim().isEmpty()
            && book.getAuthor() != null && !book.getAuthor().trim().isEmpty()
            && book.getOriginalPrice() > 0
            && book.getCategory() != null
            && book.getCondition() != null;
    }

    // --- Add‐Book Dialog with unified form ---
    private Book showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("New Book");
        dialog.setHeaderText("Enter all details for your new listing:");

        // OK/Cancel buttons
        ButtonType addBtnType = new ButtonType("Add Book", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        // Create form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField     = new TextField();
        titleField.setPromptText("Title");
        TextField authorField    = new TextField();
        authorField.setPromptText("Author");
        TextField priceField     = new TextField();
        priceField.setPromptText("9.99");

        ChoiceBox<Book.Category> categoryBox =
            new ChoiceBox<>(FXCollections.observableArrayList(Book.Category.values()));
        categoryBox.setValue(Book.Category.OTHER);

        ChoiceBox<Book.Condition> conditionBox =
            new ChoiceBox<>(FXCollections.observableArrayList(Book.Condition.values()));
        conditionBox.setValue(Book.Condition.NEW);

        grid.add(new Label("Title:"),     0, 0);
        grid.add(titleField,               1, 0);
        grid.add(new Label("Author:"),    0, 1);
        grid.add(authorField,              1, 1);
        grid.add(new Label("Price:"),     0, 2);
        grid.add(priceField,               1, 2);
        grid.add(new Label("Category:"),  0, 3);
        grid.add(categoryBox,              1, 3);
        grid.add(new Label("Condition:"), 0, 4);
        grid.add(conditionBox,             1, 4);

        dialog.getDialogPane().setContent(grid);

        // Enable the Add button only when Title/Author non-empty & price > 0
        Node addButton = dialog.getDialogPane().lookupButton(addBtnType);
        BooleanBinding valid = new BooleanBinding() {
            {
                super.bind(titleField.textProperty(),
                           authorField.textProperty(),
                           priceField.textProperty());
            }
            @Override protected boolean computeValue() {
                String t = titleField.getText().trim();
                String a = authorField.getText().trim();
                String p = priceField.getText().trim();
                try {
                    return !t.isEmpty() && !a.isEmpty() && Double.parseDouble(p) > 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
        addButton.disableProperty().bind(valid.not());

        // Convert result to a Book on Add
        dialog.setResultConverter(btn -> {
            if (btn == addBtnType) {
                double cost = Double.parseDouble(priceField.getText().trim());
                return new Book(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    2025,
                    categoryBox.getValue(),
                    conditionBox.getValue(),
                    cost,
                    seller.getAsuId()
                );
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        return result.orElse(null);
    }

    // --- Edit‐Book Dialog using single form ---
    private void showEditBookDialog(Book book) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Modify any fields, then Save:");

        ButtonType saveBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField     = new TextField(book.getTitle());
        TextField authorField    = new TextField(book.getAuthor());
        TextField priceField     = new TextField(String.valueOf(book.getOriginalPrice()));

        ChoiceBox<Book.Category> categoryBox =
            new ChoiceBox<>(FXCollections.observableArrayList(Book.Category.values()));
        categoryBox.setValue(book.getCategory());

        ChoiceBox<Book.Condition> conditionBox =
            new ChoiceBox<>(FXCollections.observableArrayList(Book.Condition.values()));
        conditionBox.setValue(book.getCondition());

        grid.add(new Label("Title:"),     0, 0);
        grid.add(titleField,               1, 0);
        grid.add(new Label("Author:"),    0, 1);
        grid.add(authorField,              1, 1);
        grid.add(new Label("Price:"),     0, 2);
        grid.add(priceField,               1, 2);
        grid.add(new Label("Category:"),  0, 3);
        grid.add(categoryBox,              1, 3);
        grid.add(new Label("Condition:"), 0, 4);
        grid.add(conditionBox,             1, 4);

        dialog.getDialogPane().setContent(grid);

        // Disable Save until form is valid
        Node saveButton = dialog.getDialogPane().lookupButton(saveBtnType);
        BooleanBinding valid = new BooleanBinding() {
            {
                super.bind(titleField.textProperty(),
                           authorField.textProperty(),
                           priceField.textProperty());
            }
            @Override protected boolean computeValue() {
                String t = titleField.getText().trim();
                String a = authorField.getText().trim();
                String p = priceField.getText().trim();
                try {
                    return !t.isEmpty() && !a.isEmpty() && Double.parseDouble(p) > 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
        saveButton.disableProperty().bind(valid.not());

        dialog.setResultConverter(btn -> {
            if (btn == saveBtnType) {
                book.setTitle(titleField.getText().trim());
                book.setAuthor(authorField.getText().trim());
                book.setOriginalPrice(Double.parseDouble(priceField.getText().trim()));
                book.setCategory(categoryBox.getValue());
                book.setCondition(conditionBox.getValue());
                return book;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(b -> {
            BookListingManager.updateListing(b.getId(), b);
            refreshTable();
        });
    }
}
