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
        // ***ADDED***: Validate user input
        if (!validateBook(book)) {
            Alert alert = new Alert(
                Alert.AlertType.ERROR,
                "Please fill in Title, Author and Price (> 0)."
            );
            alert.showAndWait();
            return;
        }

        // ***ADDED***: Auto-calculate selling price (20% markup)
        double cost = book.getPrice();
        book.setPrice(Math.round(cost * 1.2 * 100.0) / 100.0);

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

    /** Quick stub – creates a dummy listing so you can see the UI update. */
    private void mockListBook() {
        Book demo = new Book(
            "Untitled Draft",
            "Anonymous",
            2025,
            Book.Category.OTHER,
            Book.Condition.NEW,
            9.99,
            seller.getAsuId()            // assumes Book has a seller-id field
        );
        listBook(demo);
    }
 // ***ADDED***: Basic input validation
    private boolean validateBook(Book book) {
        return book.getTitle()  != null
            && !book.getTitle().trim().isEmpty()
            && book.getAuthor() != null
            && !book.getAuthor().trim().isEmpty()
            && book.getPrice()  >  0;
    }

    // ***ADDED***: Edit dialog for existing listings
    private void showEditBookDialog(Book book) {
        TextInputDialog dlg =
          new TextInputDialog(book.getTitle());
        dlg.setTitle("Edit Book");
        dlg.setHeaderText("Update Title:");
        dlg.setContentText("Title:");
        dlg.showAndWait().ifPresent(newTitle -> {
            book.setTitle(newTitle);
            // TODO: repeat for author, price, etc.

            // Re-apply markup if cost changed
            double cost = book.getPrice();
            book.setPrice(
              Math.round(cost * 1.2 * 100.0) / 100.0
            );

            BookListingManager.updateListing(
              book.getId(), book);
            refreshTable();
        });
    }

    // ***ADDED***: Dialog to collect real book details
    private Book showAddBookDialog() {
        // 1) Title
        TextInputDialog titleDlg =
          new TextInputDialog();
        titleDlg.setTitle("New Book");
        titleDlg.setHeaderText("Enter Title:");
        Optional<String> ot = titleDlg.showAndWait();
        if (ot.isEmpty()
         || ot.get().trim().isEmpty()) return null;
        String title = ot.get().trim();

        // 2) Author
        TextInputDialog authDlg =
          new TextInputDialog();
        authDlg.setTitle("New Book");
        authDlg.setHeaderText("Enter Author:");
        Optional<String> oa = authDlg.showAndWait();
        if (oa.isEmpty()
         || oa.get().trim().isEmpty()) return null;
        String author = oa.get().trim();

        // 3) Cost Price
        TextInputDialog priceDlg =
          new TextInputDialog();
        priceDlg.setTitle("New Book");
        priceDlg.setHeaderText("Enter Cost Price:");
        priceDlg.setContentText("e.g. 9.99");
        Optional<String> op = priceDlg.showAndWait();
        double cost;
        try {
            cost = Double.parseDouble(
              op.orElse("").trim()
            );
            if (cost <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            new Alert(
              Alert.AlertType.ERROR,
              "Invalid price entered."
            ).showAndWait();
            return null;
        }

        // 4) Build and return the Book
        return new Book(
            title,
            author,
            2025,
            Book.Category.OTHER,
            Book.Condition.NEW,
            cost,
            seller.getAsuId()
        );
    }
}
