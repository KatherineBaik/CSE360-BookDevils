package LoginPage;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Greeting
        Label greeting = new Label("Welcome Back !");
        greeting.setFont(Font.font("Arial", 24));

        Label subText = new Label("Today is a new day. It’s your day. You shape it.\nSign in to start buying and selling.");
        subText.setTextFill(Color.DARKGRAY);

        // Input fields
        TextField asuIdField = new TextField();
        asuIdField.setPromptText("1234567890");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("At least 8 characters");

        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Buyer", "Seller", "Admin");
        roleDropdown.setPromptText("Role");

        Button signInBtn = new Button("Sign In");
        signInBtn.setMaxWidth(Double.MAX_VALUE);
        signInBtn.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");

        Label messageLabel = new Label();

        signInBtn.setOnAction(e -> {
            String asuId = asuIdField.getText();
            String password = passwordField.getText();
            String role = roleDropdown.getValue();

            if (asuId.isEmpty() || password.isEmpty() || role == null) {
                messageLabel.setText("Please fill out all fields.");
                messageLabel.setTextFill(Color.RED);
                return;
            }

            boolean isAuthenticated = AuthenticationService.authenticate(asuId, password, role);

            if (isAuthenticated) {
                messageLabel.setText("Login successful! Logged in as " + role);
                messageLabel.setTextFill(Color.GREEN);
                // TODO: Redirect to role page here if needed
            } else {
                messageLabel.setText("Invalid credentials.");
                messageLabel.setTextFill(Color.RED);
            }
        });

        Button ssoBtn = new Button("Sign in with SSO");
        ssoBtn.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://weblogin.asu.edu/cas/login"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label signupPrompt = new Label("Don’t have an account?");
        Hyperlink signupLink = new Hyperlink("Sign up");
        HBox signupBox = new HBox(5, signupPrompt, signupLink);
        signupBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(10,
                greeting, subText,
                new Label("ASU ID"), asuIdField,
                new Label("Password"), passwordField,
                new Label("Role Selection"), roleDropdown,
                signInBtn, ssoBtn,
                messageLabel,
                signupBox
        );
        form.setPadding(new Insets(30));
        form.setMaxWidth(350);

        Image logoImage = new Image("file:logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        VBox logoBox = new VBox(logoView);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(30));

        HBox mainLayout = new HBox(form, logoBox);
        mainLayout.setSpacing(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(mainLayout, 800, 500);
        primaryStage.setTitle("Book Devils - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
