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

public class SellerPage extends Application {

    private final User seller;
    private TableView<Book> table;

    public SellerPage(User seller) { this.seller = seller; }
    public SellerPage() { this.seller = null; }

    @Override
    public void start(Stage stage) {
        ImageView logo = new ImageView(new Image(getClass().getResource("/LoginPage/logo(2).png").toExternalForm()));
        logo.setFitHeight(70); logo.setPreserveRatio(true);

        Label title = new Label("Book Devils");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Paint.valueOf("white"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logout = new Button("LOGOUT");
        logout.setStyle("-fx-background-color:white;-fx-text-fill:#750029;-fx-font-weight:bold;");
        logout.setOnAction(e -> {
            try {
                new LoginPage().start(new Stage());
                stage.close();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        HBox topBar = new HBox(10, logo, title, spacer, logout);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color:#750029;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(d -> d.getValue().titleProperty());

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(d -> d.getValue().authorProperty());

        TableColumn<Book, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(d -> d.getValue().priceProperty());
        priceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number val, boolean empty) {
                super.updateItem(val, empty);
                setText(empty || val == null ? null : String.format("%.2f", val.doubleValue()));
            }
        });

        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategory().name()));

        TableColumn<Book, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCondition().name()));

        TableColumn<Book, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button edit = new Button("Edit");
            private final Button del = new Button("Delete");
            {
                edit.setStyle("-fx-background-color:#750029;-fx-text-fill:white;-fx-font-weight:bold;");
                del.setStyle("-fx-background-color:#750029;-fx-text-fill:white;-fx-font-weight:bold;");
                edit.setOnAction(e -> {
                    Book b = getTableView().getItems().get(getIndex());
                    showEditBookDialog(b);
                });
                del.setOnAction(e -> {
                    Book b = getTableView().getItems().get(getIndex());
                    BookListingManager.deleteListing(b.getId());
                    refreshTable();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, edit, del));
            }
        });

        table.getColumns().setAll(titleCol, authorCol, priceCol, categoryCol, conditionCol, actionCol);
        refreshTable();

        Button listBtn = new Button("List New Book");
        listBtn.setStyle("-fx-background-color:#750029;-fx-text-fill:white;-fx-font-weight:bold;");
        listBtn.setOnAction(e -> {
            Book nb = showAddBookDialog();
            if (nb != null) listBook(nb);
        });

        HBox footer = new HBox(listBtn);
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER_LEFT);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(table);
        root.setBottom(footer);

        stage.setTitle("Book Devils – Seller Dashboard");
        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }

    public List<Book> viewListings() {
        return BookListingManager.getAllListingsNotSold().stream()
            .filter(b -> b.getSellerId().equals(seller.getAsuId()))
            .toList();
    }

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

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(viewListings()));
    }

    private boolean validateBook(Book book) {
        return book.getTitle() != null && !book.getTitle().trim().isEmpty()
            && book.getAuthor() != null && !book.getAuthor().trim().isEmpty()
            && book.getOriginalPrice() > 0
            && book.getCategory() != null
            && book.getCondition() != null;
    }

    private Book showAddBookDialog() {
        Dialog<Book> dlg = new Dialog<>();
        dlg.setTitle("New Book");
        dlg.setHeaderText("Enter all details for your new listing:");

        DialogPane dialogPane = dlg.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #fefefe;");

        ButtonType addType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(addType, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(12);
        g.setPadding(new Insets(24));

        TextField tF = new TextField();
        tF.setPromptText("Title");
        TextField aF = new TextField();
        aF.setPromptText("Author");
        TextField pF = new TextField();
        pF.setPromptText("9.99");

        ChoiceBox<Book.Category> cB = new ChoiceBox<>(FXCollections.observableArrayList(Book.Category.values()));
        cB.setValue(Book.Category.OTHER);
        ChoiceBox<Book.Condition> dB = new ChoiceBox<>(FXCollections.observableArrayList(Book.Condition.values()));
        dB.setValue(Book.Condition.NEW);

        Label titleLbl = new Label("Title:");
        titleLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label authorLbl = new Label("Author:");
        authorLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label priceLbl = new Label("Price:");
        priceLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label catLbl = new Label("Category:");
        catLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label condLbl = new Label("Condition:");
        condLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");

        g.add(titleLbl, 0, 0);  g.add(tF, 1, 0);
        g.add(authorLbl, 0, 1); g.add(aF, 1, 1);
        g.add(priceLbl, 0, 2);  g.add(pF, 1, 2);
        g.add(catLbl, 0, 3);    g.add(cB, 1, 3);
        g.add(condLbl, 0, 4);   g.add(dB, 1, 4);

        dialogPane.setContent(g);

        Node addBtn = dialogPane.lookupButton(addType);
        BooleanBinding validAdd = new BooleanBinding() {
            {
                bind(tF.textProperty(), aF.textProperty(), pF.textProperty());
            }

            @Override
            protected boolean computeValue() {
                try {
                    return !tF.getText().trim().isEmpty()
                            && !aF.getText().trim().isEmpty()
                            && Double.parseDouble(pF.getText().trim()) > 0;
                } catch (Exception e) {
                    return false;
                }
            }
        };
        addBtn.disableProperty().bind(validAdd.not());

        dlg.setResultConverter(btn -> {
            if (btn == addType) {
                double cost = Double.parseDouble(pF.getText().trim());
                return new Book(
                        tF.getText().trim(),
                        aF.getText().trim(),
                        2025,
                        cB.getValue(),
                        dB.getValue(),
                        cost,
                        seller.getAsuId()
                );
            }
            return null;
        });

        Optional<Book> res = dlg.showAndWait();
        return res.orElse(null);
    }

    private void showEditBookDialog(Book book) {
        Dialog<Book> dlg = new Dialog<>();
        dlg.setTitle("Edit Book");
        dlg.setHeaderText("Modify any fields, then Save:");

        DialogPane dialogPane = dlg.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #fefefe;");

        ButtonType saveT = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(saveT, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(12);
        g.setPadding(new Insets(24));

        TextField tF = new TextField(book.getTitle());
        TextField aF = new TextField(book.getAuthor());
        TextField pF = new TextField(String.valueOf(book.getOriginalPrice()));

        ChoiceBox<Book.Category> cB = new ChoiceBox<>(FXCollections.observableArrayList(Book.Category.values()));
        cB.setValue(book.getCategory());
        ChoiceBox<Book.Condition> dB = new ChoiceBox<>(FXCollections.observableArrayList(Book.Condition.values()));
        dB.setValue(book.getCondition());

        Label titleLbl = new Label("Title:");
        titleLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label authorLbl = new Label("Author:");
        authorLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label priceLbl = new Label("Price:");
        priceLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label catLbl = new Label("Category:");
        catLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");
        Label condLbl = new Label("Condition:");
        condLbl.setStyle("-fx-text-fill: #750029; -fx-font-weight: bold;");

        g.add(titleLbl, 0, 0);  g.add(tF, 1, 0);
        g.add(authorLbl, 0, 1); g.add(aF, 1, 1);
        g.add(priceLbl, 0, 2);  g.add(pF, 1, 2);
        g.add(catLbl, 0, 3);    g.add(cB, 1, 3);
        g.add(condLbl, 0, 4);   g.add(dB, 1, 4);

        dialogPane.setContent(g);

        Node saveBtn = dialogPane.lookupButton(saveT);
        BooleanBinding validSave = new BooleanBinding() {
            {
                bind(tF.textProperty(), aF.textProperty(), pF.textProperty());
            }

            @Override
            protected boolean computeValue() {
                try {
                    return !tF.getText().trim().isEmpty()
                            && !aF.getText().trim().isEmpty()
                            && Double.parseDouble(pF.getText().trim()) > 0;
                } catch (Exception e) {
                    return false;
                }
            }
        };
        saveBtn.disableProperty().bind(validSave.not());

        dlg.setResultConverter(btn -> {
            if (btn == saveT) {
                book.setTitle(tF.getText().trim());
                book.setAuthor(aF.getText().trim());
                book.setOriginalPrice(Double.parseDouble(pF.getText().trim()));
                book.setCategory(cB.getValue());
                book.setCondition(dB.getValue());
                return book;
            }
            return null;
        });

        dlg.showAndWait().ifPresent(b -> {
            BookListingManager.updateListing(b.getId(), b);
            refreshTable();
            table.refresh();
        });
    }
}
