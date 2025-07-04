package BuyerView;

import Data.Book;
import Data.User;
import LoginPage.LoginPage;
import Data.Order;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Minimal but fully‑functional Buyer dashboard.
 * <p>
 *     ‑ Uses canonical Data.Book/Cart/Order classes.<br>
 *     ‑ No demo inventory: listings are injected via ctor.<br>
 *     ‑ Filtering supports Search, Category, Condition, Max‑Price.<br>
 *     ‑ TableView acts as quick UI until the design team styles it with FXML/CSS.
 */
public class BuyerPage extends Application {

    /* ------------------------------------------------------------------
     *  State
     * ------------------------------------------------------------------ */
    private final User currentUser;
    private final ObservableList<Book> master;      // all listings
    private final ObservableList<Book> filtered;    // current view

    private final Cart cart;
    private final FilterEngine filter = new FilterEngine();
    private final CheckoutHandler checkout = new CheckoutHandler();
    private final List<Book> wishlist = new ArrayList<>();

    /* ------------------------------------------------------------------
     *  UI widgets we must touch from code
     * ------------------------------------------------------------------ */
    private TableView<Book> table;
    private TextField searchField;
    private ComboBox<Book.Category> categoryBox;
    private ComboBox<Book.Condition> conditionBox;
    private Slider priceSlider;
    private Button cartBtn, wishlistBtn;

    /* ------------------------------------------------------------------
     *  Constructors
     * ------------------------------------------------------------------ */
    public BuyerPage(User user, List<Book> listings) {
        this.currentUser = user;
        this.master      = FXCollections.observableArrayList(listings);
        this.filtered    = FXCollections.observableArrayList(listings);
        this.cart        = new Cart(user);
    }

    // JavaFX launcher requires a no‑arg constructor. NEVER call manually.
    public BuyerPage() {
        this.currentUser = null;
        this.master   = FXCollections.observableArrayList();
        this.filtered = FXCollections.observableArrayList();
        this.cart     = new Cart(null);
    }

    /* ------------------------------------------------------------------
     *  JavaFX entry point
     * ------------------------------------------------------------------ */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Book Devils – Buyer");

        BorderPane root = new BorderPane();
        root.setTop(buildTopBar(stage));
        root.setLeft(buildFilterPane());
        root.setCenter(buildTable());

        root.setPrefSize(960, 640);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /* ------------------------------------------------------------------
     *  Top bar
     * ------------------------------------------------------------------ */
    private HBox buildTopBar(Stage owner) {
        // --- Logo --- Added logo-esha
        ImageView logoImg = new ImageView(new Image(getClass().getResource("/LoginPage/logo(2).png").toExternalForm()));
        logoImg.setFitHeight(70);
        logoImg.setPreserveRatio(true);

        Label appName = new Label("Book Devils");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        appName.setTextFill(Color.WHITE);

        HBox left = new HBox(10, logoImg, appName);
        left.setAlignment(Pos.CENTER_LEFT);

        // --- Spacer ---
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // --- Buttons ---
        cartBtn = new Button();
        cartBtn.setOnAction(e -> openCart());
        updateCartLabel();

        wishlistBtn = new Button();
        wishlistBtn.setOnAction(e -> openWishlist());
        updateWishlistLabel();

        Button logout = new Button("LOGOUT");
        logout.setOnAction(e -> {
            try {
                new LoginPage().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            owner.close();
        });

        for (Button btn : List.of(cartBtn, wishlistBtn, logout)) {
            btn.setStyle("-fx-background-color: white; -fx-text-fill: #750029; -fx-font-weight: bold;");
        }

        HBox bar = new HBox(20, left, spacer, cartBtn, wishlistBtn, logout);
        bar.setPadding(new Insets(10));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #750029;");
        return bar;
    }


    /* ------------------------------------------------------------------
     *  Filter panel
     * ------------------------------------------------------------------ */
    private VBox buildFilterPane() {
        searchField = new TextField();
        searchField.setPromptText("Search title or author");

        categoryBox = new ComboBox<>();
        categoryBox.getItems().add(null); // "All"
        categoryBox.getItems().addAll(Book.Category.values());

        conditionBox = new ComboBox<>();
        conditionBox.getItems().add(null);
        conditionBox.getItems().addAll(Book.Condition.values());

        priceSlider = new Slider(0, 300, 300);
        Label priceLbl = new Label();
        priceLbl.textProperty().bind(Bindings.format("Max: $%.0f", priceSlider.valueProperty()));

        Button apply = new Button("Apply");
        apply.setMaxWidth(Double.MAX_VALUE);
        apply.setOnAction(e -> applyFilters());

        VBox pane = new VBox(10,
                new Label("Search"), searchField,
                new Separator(),
                new Label("Category"), categoryBox,
                new Label("Condition"), conditionBox,
                new Label("Price"), priceSlider, priceLbl,
                apply);
        pane.setPadding(new Insets(12));
        pane.setPrefWidth(220);
        return pane;
    }

    /* ------------------------------------------------------------------
     *  Table of books
     * ------------------------------------------------------------------ */
    private TableView<Book> buildTable() {
        table = new TableView<>(filtered);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book,String> titleCol  = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book,String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book,String> catCol    = new TableColumn<>("Category");
        catCol.setCellValueFactory(c -> Bindings.createStringBinding(() -> c.getValue().getCategory().name()));
        TableColumn<Book,String> condCol   = new TableColumn<>("Condition");
        condCol.setCellValueFactory(c -> Bindings.createStringBinding(() -> c.getValue().getCondition().name()));
        TableColumn<Book,Number> priceCol  = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        TableColumn<Book, Void> actionCol = new TableColumn<>(" ");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Details");
            {
                btn.setOnAction(e -> showDetails(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(titleCol, authorCol, catCol, condCol, priceCol, actionCol);
        return table;
    }

    /* ------------------------------------------------------------------
     *  Filtering logic
     * ------------------------------------------------------------------ */
    private void applyFilters() {
        List<Book> list = new ArrayList<>(master);

        String q = searchField.getText().trim();
        if (!q.isEmpty()) list = filter.search(list, q);

        if (categoryBox.getValue() != null)
            list = filter.filterByCategory(list, categoryBox.getValue());

        if (conditionBox.getValue() != null)
            list = filter.filterByCondition(list, conditionBox.getValue());

        list = filter.filterByPrice(list, priceSlider.getValue());

        filtered.setAll(list);
    }

    /* ------------------------------------------------------------------
     *  Details dialog
     * Made some UI changes- esha
     * ------------------------------------------------------------------ */
    private void showDetails(Book book) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Book Details");

        // --- Styled labels ---
        Label title = new Label(book.getTitle());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#750029"));

        Label author = new Label("by " + book.getAuthor());
        author.setFont(Font.font("Arial", 14));
        author.setTextFill(Color.web("#444"));

        Label price = new Label("$" + String.format("%.2f", book.getSellingPrice()));
        price.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        price.setTextFill(Color.web("#750029"));

        // --- Styled buttons ---
        Button add = new Button("Add to cart");
        add.setStyle("-fx-background-color: #750029; -fx-text-fill: white; -fx-font-weight: bold;");
        add.setOnAction(e -> {
            if (cart.add(book)) updateCartLabel();
            dlg.close();
        });

        Button wish = new Button("Wishlist");
        wish.setStyle("-fx-background-color: white; -fx-border-color: #750029; -fx-text-fill: #750029; -fx-font-weight: bold;");
        wish.setOnAction(e -> {
            if (!wishlist.contains(book)) wishlist.add(book);
            updateWishlistLabel();
            dlg.close();
        });

        HBox buttonBox = new HBox(10, add, wish);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(12, title, author, price, buttonBox);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(box, 300, 200);
        dlg.setScene(scene);
        dlg.showAndWait();
    }


    /* ------------------------------------------------------------------
     *  Cart & Wishlist
     * ------------------------------------------------------------------ */
    private void openCart() {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Cart");

        ListView<Book> list = new ListView<>(FXCollections.observableArrayList(cart.getBooks()));
        list.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Book b, boolean empty) {
                super.updateItem(b, empty);
                setText(empty ? null : b.getBookSummary());
            }
        });

        Label totalLbl = new Label();
        totalLbl.textProperty().bind(Bindings.format("Total: $%.2f", Bindings.createDoubleBinding(cart::getTotal)));

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setOnAction(e -> {
            if (cart.getBooks().isEmpty()) return;
            Order order = checkout.process(cart);
            if (order != null) {
                Alert ok = new Alert(Alert.AlertType.INFORMATION, "Order #" + order.getOrderId() + " placed!");
                ok.showAndWait();
                updateCartLabel();
                dlg.close();
            }
        });

        VBox box = new VBox(10, list, totalLbl, checkoutBtn);
        box.setPadding(new Insets(12));
        dlg.setScene(new Scene(box, 320, 400));
        dlg.showAndWait();
    }

    private void openWishlist() {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Wishlist");

        ListView<Book> list = new ListView<>(FXCollections.observableArrayList(wishlist));
        list.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Book b, boolean empty) {
                super.updateItem(b, empty);
                setText(empty ? null : b.getBookSummary());
            }
        });

        Button close = new Button("Close");
        close.setOnAction(e -> dlg.close());

        VBox box = new VBox(10, list, close);
        box.setPadding(new Insets(12));
        dlg.setScene(new Scene(box, 300, 400));
        dlg.showAndWait();
    }

    /* ------------------------------------------------------------------
     *  Helpers
     * ------------------------------------------------------------------ */
    private void updateCartLabel() {
        cartBtn.setText("Cart (" + cart.getBooks().size() + ")");
    }

    private void updateWishlistLabel() {
        wishlistBtn.setText("Wishlist (" + wishlist.size() + ")");
    }

    /* ------------------------------------------------------------------
     *  Launch helper for stand‑alone test
     * ------------------------------------------------------------------ */
    public static void main(String[] args) {
        // quick demo inventory
        List<Book> demo = List.of(
            new Book("Clean Code", "Robert Martin", 2008, Book.Category.COMPUTERS, Book.Condition.LIKE_NEW, 45, "0000"),
            new Book("Discrete Math", "Rosen", 2012, Book.Category.MATH, Book.Condition.MODERATELY_USED, 60, "0000"));
        new BuyerPage(null, demo).start(new Stage());
    }
}