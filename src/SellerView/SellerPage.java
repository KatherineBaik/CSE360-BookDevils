package SellerView;

import Data.Book;
import Data.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

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
        /* ---------- simple table ---------- */
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book,String> titleCol  = new TableColumn<>("Title");
        titleCol.setCellValueFactory(d -> d.getValue().titleProperty());
        TableColumn<Book,String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(d -> d.getValue().authorProperty());
        TableColumn<Book,Number> priceCol  = new TableColumn<>("Price");
        priceCol.setCellValueFactory(d -> d.getValue().priceProperty());

        table.getColumns().addAll(titleCol, authorCol, priceCol);
        refreshTable();                       // load current listings

        /* ---------- quick “List Book” stub ---------- */
        Button listBtn = new Button("List New Book");
        listBtn.setOnAction(e -> mockListBook());     // demo only

        HBox footer = new HBox(listBtn);
        footer.setPadding(new Insets(10));

        BorderPane root = new BorderPane(table);
        root.setBottom(footer);

        stage.setTitle("Book Devils – Seller Dashboard");
        stage.setScene(new Scene(root, 600, 400));
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
}
