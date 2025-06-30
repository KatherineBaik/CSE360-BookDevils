package com.example.demo1.BuyerView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.ArrayList;

public class BuyerPage extends Application {
    private User currentUser;
    private Cart cart;
    private FilterEngine filterEngine;
    private CheckoutHandler checkoutHandler;
    private ArrayList<Book> books;
    private ArrayList<Book> wishlist;

    private GridPane bookGrid;
    private TextField searchField;
    private ComboBox<String> categoryCombo;
    private ComboBox<String> conditionCombo;
    private Slider priceSlider;
    private Label priceLabel;
    private Button cartButton;
    private Button wishlistButton;

    public BuyerPage() {
        this.books = new ArrayList<>();
        this.currentUser = new User("test", "123", "pass", "buyer");
        this.cart = new Cart(currentUser);
        this.filterEngine = new FilterEngine();
        this.checkoutHandler = new CheckoutHandler();
        this.wishlist = new ArrayList<>();

        initBooks();
    }

    public BuyerPage(User user, ArrayList<Book> bookList) {
        this.currentUser = user;
        this.books = bookList;
        this.cart = new Cart(user);
        this.filterEngine = new FilterEngine();
        this.checkoutHandler = new CheckoutHandler();
        this.wishlist = new ArrayList<>();

        if (books.isEmpty()) {
            initBooks();
        }
    }

    private void initBooks() {
        books.add(new Book("Introduction to Algorithms", "Thomas H. Cormen", 2022, "Computer Sciences", "New", 89.99, 74.99, 15));
        books.add(new Book("Clean Code", "Robert C. Martin", 2021, "Computer Sciences", "Like New", 45.00, 38.50, 8));
        books.add(new Book("Design Patterns", "Gang of Four", 2020, "Computer Sciences", "Good", 54.95, 42.00, 12));
        books.add(new Book("The Art of Computer Programming", "Donald Knuth", 2019, "Computer Sciences", "Fair", 199.99, 145.00, 3));
        books.add(new Book("Artificial Intelligence: A Modern Approach", "Stuart Russell", 2023, "Computer Sciences", "New", 125.00, 105.00, 10));
        books.add(new Book("Calculus: Early Transcendentals", "James Stewart", 2022, "Mathematics", "New", 140.00, 120.00, 20));
        books.add(new Book("Linear Algebra and Its Applications", "David C. Lay", 2021, "Mathematics", "Like New", 110.00, 85.00, 7));
        books.add(new Book("Discrete Mathematics", "Kenneth Rosen", 2020, "Mathematics", "Good", 95.00, 70.00, 15));
        books.add(new Book("Engineering Mechanics: Statics", "J.L. Meriam", 2023, "Engineering", "New", 155.00, 130.00, 12));
        books.add(new Book("Fundamentals of Electric Circuits", "Charles Alexander", 2022, "Engineering", "Like New", 120.00, 95.00, 9));
        books.add(new Book("Thermodynamics: An Engineering Approach", "Yunus Cengel", 2021, "Engineering", "Good", 135.00, 100.00, 6));
        books.add(new Book("Psychology", "David Myers", 2023, "Psychology", "New", 180.00, 150.00, 18));
        books.add(new Book("Social Psychology", "Elliot Aronson", 2022, "Psychology", "Like New", 140.00, 110.00, 10));
        books.add(new Book("Principles of Economics", "N. Gregory Mankiw", 2023, "Business & Economics", "New", 250.00, 200.00, 25));
        books.add(new Book("Financial Accounting", "Jerry Weygandt", 2022, "Business & Economics", "Good", 180.00, 140.00, 14));
        books.add(new Book("The Norton Anthology of English Literature", "Stephen Greenblatt", 2021, "English & Literature", "New", 85.00, 65.00, 11));
        books.add(new Book("MLA Handbook", "Modern Language Association", 2023, "English & Literature", "New", 25.00, 20.00, 30));
        books.add(new Book("Genki: Integrated Course in Elementary Japanese", "Eri Banno", 2022, "Foreign Languages", "Like New", 75.00, 60.00, 8));
        books.add(new Book("Art History", "Marilyn Stokstad", 2021, "Art & Architecture", "Good", 195.00, 150.00, 5));
        books.add(new Book("The Story of Art", "E.H. Gombrich", 2020, "Art & Architecture", "Fair", 45.00, 30.00, 4));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BOOK DEVILS");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        HBox topBar = createTopBar();
        root.setTop(topBar);

        BorderPane contentPane = new BorderPane();
        contentPane.setStyle("-fx-background-color: white;");

        VBox filterPanel = createFilterPanel();
        contentPane.setLeft(filterPanel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        bookGrid = new GridPane();
        bookGrid.setHgap(20);
        bookGrid.setVgap(20);
        bookGrid.setPadding(new Insets(20));
        bookGrid.setStyle("-fx-background-color: white;");

        scrollPane.setContent(bookGrid);
        contentPane.setCenter(scrollPane);

        root.setCenter(contentPane);

        displayBooks(books);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #8C1D40;");
        topBar.setPrefHeight(60);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label logo = new Label("BOOK DEVILS");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("LOGOUT");
        logoutButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> System.exit(0));

        topBar.getChildren().addAll(logo, spacer, logoutButton);

        return topBar;
    }

    private VBox createFilterPanel() {
        VBox panel = new VBox(12);
        panel.setStyle("-fx-background-color: white;");
        panel.setPrefWidth(250);
        panel.setPadding(new Insets(20));

        Label filterLabel = new Label("Filter");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label searchLabel = new Label("Search");
        searchLabel.setTextFill(Color.GRAY);
        searchLabel.setFont(Font.font("Arial", 14));

        searchField = new TextField();
        searchField.setStyle("-fx-border-color: lightgray; -fx-border-radius: 3; -fx-padding: 5 10 5 10;");

        Label categoryLabel = new Label("Category");
        categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(
            "All Categories",
            "Computer Sciences",
            "Engineering",
            "Mathematics",
            "Social Sciences",
            "Psychology",
            "Business & Economics",
            "English & Literature",
            "Foreign Languages",
            "Art & Architecture"
        );
        categoryCombo.setValue("All Categories");
        categoryCombo.setMaxWidth(Double.MAX_VALUE);
        categoryCombo.setStyle("-fx-border-color: lightgray;");

        Label conditionLabel = new Label("Condition");
        conditionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        conditionCombo = new ComboBox<>();
        conditionCombo.getItems().addAll("All Conditions", "New", "Like New", "Good", "Fair");
        conditionCombo.setValue("All Conditions");
        conditionCombo.setMaxWidth(Double.MAX_VALUE);
        conditionCombo.setStyle("-fx-border-color: lightgray;");

        Label priceRangeLabel = new Label("Filter By Price");
        priceRangeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        priceSlider = new Slider(0, 200, 200);
        priceSlider.setShowTickLabels(false);
        priceSlider.setShowTickMarks(false);

        HBox priceRangeBox = new HBox();
        priceRangeBox.setAlignment(Pos.CENTER);
        Label minLabel = new Label("$0");
        minLabel.setTextFill(Color.GRAY);
        priceLabel = new Label("$0 - $200");
        Label maxLabel = new Label("$200");
        maxLabel.setTextFill(Color.GRAY);
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        priceRangeBox.getChildren().addAll(minLabel, spacer1, priceLabel, spacer2, maxLabel);

        priceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            priceLabel.setText("$0 - $" + newVal.intValue());
        });

        Button filterButton = new Button("Filter");
        filterButton.setMaxWidth(Double.MAX_VALUE);
        filterButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15 10 15;");
        filterButton.setOnAction(e -> filterBooks());

        cartButton = new Button("View Cart (" + cart.getItemCount() + ")");
        cartButton.setMaxWidth(Double.MAX_VALUE);
        cartButton.setStyle("-fx-background-color: white; -fx-text-fill: #8C1D40; -fx-border-color: #8C1D40; -fx-border-width: 2; -fx-font-weight: bold; -fx-padding: 8 12 8 12;");
        cartButton.setOnAction(e -> showCartPage());

        wishlistButton = new Button("View Wishlist (" + wishlist.size() + ")");
        wishlistButton.setMaxWidth(Double.MAX_VALUE);
        wishlistButton.setStyle("-fx-background-color: white; -fx-text-fill: #8C1D40; -fx-border-color: #8C1D40; -fx-border-width: 2; -fx-font-weight: bold; -fx-padding: 8 12 8 12;");
        wishlistButton.setOnAction(e -> showWishlistPage());

        panel.getChildren().addAll(
            filterLabel,
            new Region(),
            searchLabel,
            searchField,
            new Region(),
            categoryLabel,
            categoryCombo,
            new Region(),
            conditionLabel,
            conditionCombo,
            new Region(),
            priceRangeLabel,
            priceSlider,
            priceRangeBox,
            filterButton,
            cartButton,
            wishlistButton
        );

        return panel;
    }
    private void displayBooks(ArrayList<Book> booksToDisplay) {
        bookGrid.getChildren().clear();

        int col = 0;
        int row = 0;

        for (Book book : booksToDisplay) {
            VBox bookCard = createBookCard(book);
            bookGrid.add(bookCard, col, row);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E6E6E6; -fx-border-width: 1; -fx-padding: 15;");
        card.setPrefWidth(280);
        card.setPrefHeight(400);

        VBox imagePanel = new VBox();
        imagePanel.setStyle("-fx-background-color: #323232; -fx-border-color: #C8C8C8; -fx-border-width: 1;");
        imagePanel.setPrefHeight(180);
        imagePanel.setAlignment(Pos.CENTER);
        imagePanel.setPadding(new Insets(10));

        Label imageLabel = new Label("BOOK MOCKUP");
        imageLabel.setTextFill(Color.WHITE);
        imageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        imagePanel.getChildren().add(imageLabel);

        Label categoryLabel = new Label(book.getCategory().toUpperCase());
        categoryLabel.setFont(Font.font("Arial", 10));
        categoryLabel.setTextFill(Color.rgb(120, 120, 120));

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(220);

        Label authorLabel = new Label(book.getAuthor());
        authorLabel.setFont(Font.font("Arial", 12));
        authorLabel.setTextFill(Color.rgb(100, 100, 100));

        Label conditionLabel = new Label("Condition: " + book.getCondition());
        conditionLabel.setFont(Font.font("Arial", 11));
        conditionLabel.setTextFill(Color.rgb(80, 80, 80));

        HBox ratingBox = new HBox(3);
        ratingBox.setAlignment(Pos.CENTER_LEFT);

        double rating = 3.0 + Math.random() * 2.0;
        rating = Math.round(rating * 10) / 10.0;

        Label starLabel = new Label("★");
        starLabel.setTextFill(Color.rgb(255, 193, 7));
        starLabel.setFont(Font.font("Arial", 16));

        Label ratingText = new Label(String.format("%.1f", rating));
        ratingText.setFont(Font.font("Arial", 11));
        ratingText.setTextFill(Color.rgb(120, 120, 120));

        ratingBox.getChildren().addAll(starLabel, ratingText);

        HBox priceBox = new HBox(5);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        Label newPrice = new Label("$" + String.format("%.2f", book.getSellingPrice()));
        newPrice.setTextFill(Color.rgb(140, 29, 64));
        newPrice.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        boolean showDiscount = book.getSellingPrice() < book.getOriginalPrice() * 0.95;
        if (showDiscount) {
            Label oldPrice = new Label("$" + String.format("%.2f", book.getOriginalPrice()));
            oldPrice.setTextFill(Color.rgb(150, 150, 150));
            oldPrice.setFont(Font.font("Arial", 12));
            oldPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: #999999;");
            priceBox.getChildren().addAll(newPrice, oldPrice);
        } else {
            priceBox.getChildren().add(newPrice);
        }


        Button learnMoreButton = new Button("Learn More");
        learnMoreButton.setMaxWidth(200);
        learnMoreButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20 10 20;");
        learnMoreButton.setOnAction(e -> showBookDetails(book));

        card.getChildren().addAll(
            imagePanel,
            categoryLabel,
            titleLabel,
            authorLabel,
            conditionLabel,
            ratingBox,
            priceBox,
            learnMoreButton
        );

        return card;
    }

    private void filterBooks() {
        ArrayList<Book> filtered = new ArrayList<>(books);

        String searchText = searchField.getText();
        if (!searchText.isEmpty()) {
            filtered = filterEngine.searchBooks(filtered, searchText);
        }

        String category = categoryCombo.getValue();
        if (!category.equals("All Categories")) {
            filtered = filterEngine.filterByCategory(filtered, category);
        }

        String condition = conditionCombo.getValue();
        if (!condition.equals("All Conditions")) {
            filtered = filterEngine.filterByCondition(filtered, condition);
        }

        double maxPrice = priceSlider.getValue();
        filtered = filterEngine.filterByPrice(filtered, maxPrice);

        displayBooks(filtered);
    }

    private void showBookDetails(Book book) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Book Details");
        detailStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #8C1D40;");
        topBar.setPrefHeight(60);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label logo = new Label("BOOK DEVILS");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("BACK");
        backButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> detailStage.close());

        topBar.getChildren().addAll(logo, spacer, backButton);
        root.setTop(topBar);

        HBox mainContent = new HBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: white;");

        VBox leftPanel = new VBox();
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPrefWidth(350);

        VBox bookImageContainer = new VBox();
        bookImageContainer.setStyle("-fx-background-color: #F8F8F8; -fx-border-color: #DCDCDC; -fx-border-width: 1;");
        bookImageContainer.setPrefSize(300, 400);
        bookImageContainer.setAlignment(Pos.CENTER);
        bookImageContainer.setPadding(new Insets(20));

        VBox bookCover = new VBox();
        bookCover.setAlignment(Pos.TOP_CENTER);
        bookCover.setPadding(new Insets(20, 15, 20, 15));
        bookCover.setPrefSize(200, 280);

        String[] colors = {"#8C1D40"};
        String coverColor = "#8C1D40";
        bookCover.setStyle("-fx-background-color: " + coverColor + "; -fx-border-color: rgba(0,0,0,0.2); -fx-border-width: 3;");

        Label coverTitle = new Label(book.getTitle());
        coverTitle.setTextFill(Color.WHITE);
        coverTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        coverTitle.setWrapText(true);
        coverTitle.setAlignment(Pos.CENTER);
        coverTitle.setMaxWidth(170);

        Region spacerCover = new Region();
        VBox.setVgrow(spacerCover, Priority.ALWAYS);

        Label coverAuthor = new Label(book.getAuthor());
        coverAuthor.setTextFill(Color.rgb(255, 255, 255, 0.8));
        coverAuthor.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        bookCover.getChildren().addAll(coverTitle, spacerCover, coverAuthor);
        bookImageContainer.getChildren().add(bookCover);
        leftPanel.getChildren().add(bookImageContainer);

        VBox rightPanel = new VBox(15);
        rightPanel.setPrefWidth(500);

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.rgb(33, 37, 41));

        Label authorLabel = new Label(book.getAuthor().toUpperCase());
        authorLabel.setFont(Font.font("Arial", 16));
        authorLabel.setTextFill(Color.rgb(108, 117, 125));

        HBox priceBox = new HBox(10);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        boolean showDiscount = book.getSellingPrice() < book.getOriginalPrice() * 0.95;

        if (showDiscount) {
            Label originalPrice = new Label("$" + String.format("%.2f", book.getOriginalPrice()));
            originalPrice.setFont(Font.font("Arial", 16));
            originalPrice.setTextFill(Color.rgb(108, 117, 125));
            originalPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: #6c757d;");
            priceBox.getChildren().add(originalPrice);
        }

        Label currentPrice = new Label("$" + String.format("%.2f", book.getSellingPrice()));
        currentPrice.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        currentPrice.setTextFill(Color.rgb(220, 53, 69));
        priceBox.getChildren().add(currentPrice);
        Label stockLabel = new Label(book.getStockQuantity() + " items left");
        stockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        stockLabel.setTextFill(Color.rgb(220, 53, 69));

        VBox quantityBox = new VBox(5);
        Label qtyLabel = new Label("Quantity");
        qtyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Spinner<Integer> qtySpinner = new Spinner<>(1, book.getStockQuantity(), 1);
        qtySpinner.setPrefWidth(100);

        quantityBox.getChildren().addAll(qtyLabel, qtySpinner);

        Button buyNowButton = new Button("Buy Now");
        buyNowButton.setPrefSize(400, 45);
        buyNowButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        buyNowButton.setOnAction(e -> {
            int qty = qtySpinner.getValue();
            cart.clearCart();
            for (int i = 0; i < qty; i++) {
                cart.addBook(book);
            }
            updateCartButton();
            detailStage.close();
            showCartPage();
        });

        HBox secondRowButtons = new HBox(10);

        Button addToCartButton = new Button("Add To Cart");
        addToCartButton.setPrefSize(195, 45);
        addToCartButton.setStyle("-fx-background-color: white; -fx-text-fill: #8C1D40; -fx-border-color: #8C1D40; -fx-border-width: 2; -fx-font-weight: bold;");
        addToCartButton.setOnAction(e -> {
            int qty = qtySpinner.getValue();
            int added = 0;
            for (int i = 0; i < qty; i++) {
                if (cart.addBook(book)) {
                    added++;
                } else {
                    break;
                }
            }
            updateCartButton();
            if (added > 0) {
                showAlert("Success", added + " item(s) added to cart!");
            } else {
                showAlert("Error", "Cannot add more items. Maximum stock reached!");
            }
        });

        Button wishlistButton = new Button("Wishlist");
        wishlistButton.setPrefSize(195, 45);
        wishlistButton.setStyle("-fx-background-color: white; -fx-text-fill: #8C1D40; -fx-border-color: #8C1D40; -fx-border-width: 2; -fx-font-weight: bold;");
        wishlistButton.setOnAction(e -> {
            if (!wishlist.contains(book)) {
                wishlist.add(book);
                updateWishlistButton();
                showAlert("Success", "Added to wishlist!");
            } else {
                showAlert("Info", "Already in wishlist!");
            }
        });

        secondRowButtons.getChildren().addAll(addToCartButton, wishlistButton);

        VBox descriptionBox = new VBox(10);
        descriptionBox.setPadding(new Insets(20, 0, 0, 0));

        Label descTitle = new Label("About This Book");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextArea descriptionArea = new TextArea(generateBookDescription(book));
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setStyle("-fx-control-inner-background: white;");

        descriptionBox.getChildren().addAll(descTitle, descriptionArea);

        rightPanel.getChildren().addAll(
            titleLabel,
            authorLabel,
            priceBox,
            stockLabel,
            quantityBox,
            buyNowButton,
            secondRowButtons,
            descriptionBox
        );

        mainContent.getChildren().addAll(leftPanel, rightPanel);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 900, 700);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private String generateBookDescription(Book book) {
        String[] descriptions = {
            "This comprehensive textbook provides an in-depth exploration of fundamental concepts and advanced theories. " +
                "Written by leading experts in the field, it offers practical insights and real-world applications that make " +
                "complex topics accessible to students and professionals alike.",

            "A thorough examination of key principles and methodologies in " + book.getCategory().toLowerCase() + ". " +
                "This edition has been updated with the latest research findings and industry best practices.",

            "An essential resource for students and practitioners seeking to master the subject matter. " +
                "This book combines theoretical foundations with practical applications."
        };

        return descriptions[Math.abs(book.getTitle().hashCode()) % descriptions.length];
    }

    private void showCartPage() {
        Stage cartStage = new Stage();
        cartStage.setTitle("Your Cart");
        cartStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #8C1D40;");
        topBar.setPrefHeight(60);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label logo = new Label("BOOK DEVILS");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeButton = new Button("CLOSE");
        closeButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> cartStage.close());

        topBar.getChildren().addAll(logo, spacer, closeButton);
        root.setTop(topBar);

        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(20));

        VBox cartItemsBox = new VBox(10);
        cartItemsBox.setPrefWidth(500);

        Label cartLabel = new Label("Your Cart");
        cartLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label itemCount = new Label(cart.getItemCount() + " items");
        itemCount.setTextFill(Color.GRAY);

        HBox headerBox = new HBox(10);
        headerBox.getChildren().addAll(cartLabel, itemCount);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);

        VBox itemsContainer = new VBox(5);

        ArrayList<Book> cartBooks = cart.getBooks();
        for (Book book : cartBooks) {
            HBox itemBox = new HBox(10);
            itemBox.setAlignment(Pos.CENTER_LEFT);
            itemBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 10;");

            VBox bookImage = new VBox();
            bookImage.setStyle("-fx-background-color: lightgray;");
            bookImage.setPrefSize(60, 80);

            VBox bookDetails = new VBox(3);
            Label bookTitle = new Label(book.getTitle());
            bookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            Label bookAuthor = new Label(book.getAuthor());
            bookAuthor.setFont(Font.font("Arial", 12));
            bookDetails.getChildren().addAll(bookTitle, bookAuthor);

            Region spacerItem = new Region();
            HBox.setHgrow(spacerItem, Priority.ALWAYS);

            Label price = new Label("$" + String.format("%.2f", book.getSellingPrice()));
            price.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Button removeButton = new Button("X");
            removeButton.setStyle("-fx-background-color: white; -fx-text-fill: red;");
            removeButton.setOnAction(e -> {
                cart.removeBook(book);
                updateCartButton();
                cartStage.close();
                showCartPage();
            });

            itemBox.getChildren().addAll(bookImage, bookDetails, spacerItem, price, removeButton);
            itemsContainer.getChildren().add(itemBox);
        }

        scrollPane.setContent(itemsContainer);
        cartItemsBox.getChildren().addAll(headerBox, scrollPane);

        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 20;");
        summaryBox.setPrefWidth(250);

        Label summaryTitle = new Label("Order Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox subtotalBox = new HBox();
        Label subtotalLabel = new Label("Subtotal");
        Region spacerSub = new Region();
        HBox.setHgrow(spacerSub, Priority.ALWAYS);
        Label subtotalValue = new Label("$" + String.format("%.2f", cart.getTotalPrice()));
        subtotalBox.getChildren().addAll(subtotalLabel, spacerSub, subtotalValue);

        HBox shippingBox = new HBox();
        Label shippingLabel = new Label("Shipping");
        Region spacerShip = new Region();
        HBox.setHgrow(spacerShip, Priority.ALWAYS);
        Label shippingValue = new Label("$10.00");
        shippingBox.getChildren().addAll(shippingLabel, spacerShip, shippingValue);

        HBox totalBox = new HBox();
        Label totalLabel = new Label("Total");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Region spacerTotal = new Region();
        HBox.setHgrow(spacerTotal, Priority.ALWAYS);
        double total = cart.getTotalPrice() + 10.00;
        Label totalValue = new Label("$" + String.format("%.2f", total));
        totalValue.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalBox.getChildren().addAll(totalLabel, spacerTotal, totalValue);

        Button checkoutButton = new Button("Proceed to Checkout");
        checkoutButton.setPrefWidth(200);
        checkoutButton.setPrefHeight(40);
        checkoutButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold;");
        checkoutButton.setOnAction(e -> processCheckout());

        summaryBox.getChildren().addAll(
            summaryTitle,
            new Region(),
            subtotalBox,
            shippingBox,
            new Region(),
            totalBox,
            new Region(),
            new Region(),
            checkoutButton
        );

        contentBox.getChildren().addAll(cartItemsBox, summaryBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        cartStage.setScene(scene);
        cartStage.show();
    }

    private void showWishlistPage() {
        Stage wishlistStage = new Stage();
        wishlistStage.setTitle("Your Wishlist");
        wishlistStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #8C1D40;");
        topBar.setPrefHeight(60);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label logo = new Label("BOOK DEVILS");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("BACK");
        backButton.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> wishlistStage.close());

        topBar.getChildren().addAll(logo, spacer, backButton);
        root.setTop(topBar);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));

        Label wishlistLabel = new Label("Your Wishlist");
        wishlistLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label itemCount = new Label(wishlist.size() + " items");
        itemCount.setTextFill(Color.GRAY);

        HBox headerBox = new HBox(10);
        headerBox.getChildren().addAll(wishlistLabel, itemCount);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);

        VBox itemsContainer = new VBox(5);

        for (Book book : wishlist) {
            HBox itemBox = new HBox(10);
            itemBox.setAlignment(Pos.CENTER_LEFT);
            itemBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-padding: 10;");
            itemBox.setPrefWidth(700);

            VBox bookImage = new VBox();
            bookImage.setStyle("-fx-background-color: lightgray;");
            bookImage.setPrefSize(60, 80);

            VBox bookDetails = new VBox(3);
            Label bookTitle = new Label(book.getTitle());
            bookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            Label bookAuthor = new Label(book.getAuthor());
            bookAuthor.setFont(Font.font("Arial", 12));
            bookDetails.getChildren().addAll(bookTitle, bookAuthor);

            Region spacerItem = new Region();
            HBox.setHgrow(spacerItem, Priority.ALWAYS);

            Label price = new Label("$" + String.format("%.2f", book.getSellingPrice()));
            price.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            HBox buttonBox = new HBox(10);

            Button addToCartBtn = new Button("Add to Cart");
            addToCartBtn.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15;");
            addToCartBtn.setOnAction(e -> {
                if (cart.addBook(book)) {
                    updateCartButton();
                    showAlert("Success", "Added to cart!");
                } else {
                    showAlert("Error", "Cannot add more. Maximum stock reached!");
                }
            });

            Button removeButton = new Button("Remove");
            removeButton.setStyle("-fx-background-color: white; -fx-text-fill: red; -fx-border-color: red; -fx-border-width: 1; -fx-padding: 8 15 8 15;");
            removeButton.setOnAction(e -> {
                wishlist.remove(book);
                updateWishlistButton();
                wishlistStage.close();
                showWishlistPage();
            });

            buttonBox.getChildren().addAll(addToCartBtn, removeButton);

            itemBox.getChildren().addAll(bookImage, bookDetails, spacerItem, price, buttonBox);
            itemsContainer.getChildren().add(itemBox);
        }

        scrollPane.setContent(itemsContainer);
        contentBox.getChildren().addAll(headerBox, scrollPane);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        wishlistStage.setScene(scene);
        wishlistStage.show();
    }

    private void processCheckout() {
        if (cart.getItemCount() == 0) {
            showAlert("Error", "Cart is empty!");
            return;
        }

        TextInputDialog cardDialog = new TextInputDialog();
        cardDialog.setTitle("Payment Information");
        cardDialog.setHeaderText("Enter 16-digit card number:");
        cardDialog.showAndWait().ifPresent(cardNumber -> {
            TextInputDialog cvvDialog = new TextInputDialog();
            cvvDialog.setTitle("Payment Information");
            cvvDialog.setHeaderText("Enter 3-digit CVV:");
            cvvDialog.showAndWait().ifPresent(cvv -> {
                if (checkoutHandler.checkCard(cardNumber) && checkoutHandler.checkCVV(cvv)) {
                    Order order = checkoutHandler.processCheckout(cart);
                    if (order != null) {
                        showAlert("Success", "Order placed successfully!\nOrder ID: " + order.getOrderId());
                        updateCartButton();
                        displayBooks(books);
                    }
                } else {
                    showAlert("Error", "Invalid payment information!");
                }
            });
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateCartButton() {
        cartButton.setText("View Cart (" + cart.getItemCount() + ")");
    }

    private void updateWishlistButton() {
        wishlistButton.setText("View Wishlist (" + wishlist.size() + ")");
    }

    public static void main(String[] args) {
        launch(args);
    }
}