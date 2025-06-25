package LoginPage;

import Data.User;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

/**
 * Book Devils – Login window.
 * <p>
 * Left column  : text fields & buttons<br>
 * Right column : logo<br>
 * <p>
 * Authentication is delegated to {@link AuthenticationService}.
 */
public class LoginPage extends Application {

    /* --- UI controls we need to read/write in handlers --- */
    private TextField          idField     = new TextField();
    private PasswordField      pwField     = new PasswordField();
    private ComboBox<User.Role> roleBox     = new ComboBox<>();
    private Label              messageLbl  = new Label();

    @Override
    public void start(Stage stage) {

        /* ==========  Header text  ========== */
        Label greeting = new Label("Welcome Back !");
        greeting.setFont(Font.font("Arial", 24));

        Label subText = new Label(
            "Today is a new day. It’s your day. You shape it.\n" +
            "Sign in to start buying and selling."
        );
        subText.setTextFill(Color.DARKGRAY);

        /* ==========  Credential fields  ========== */
        idField.setPromptText("1234567890");
        pwField.setPromptText("At least 8 characters");

        // use values from the User.Role enum so UI and logic never diverge
        roleBox.getItems().addAll(User.Role.values());
        roleBox.setPromptText("Role");

        /* ==========  Buttons  ========== */
        Button signInBtn = new Button("Sign In");
        signInBtn.setMaxWidth(Double.MAX_VALUE);
        signInBtn.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");

        // main login action
        signInBtn.setOnAction(e -> handleLogin());

        Button ssoBtn = new Button("Sign in with SSO");
        ssoBtn.setOnAction(e -> openSSO());

        /* ==========  Sign-up link  ========== */
        Label signupPrompt = new Label("Don’t have an account?");
        Hyperlink signupLink = new Hyperlink("Sign up");
        HBox signupBox = new HBox(5, signupPrompt, signupLink);
        signupBox.setAlignment(Pos.CENTER);

        /* ==========  Sign-up link implementation  ========== */

        signupLink.setOnAction(e -> {
            Dialog<Boolean> dlg = new Dialog<>();
            dlg.setTitle("Create account");

            // inputs …
            TextField id  = new TextField();
            PasswordField pw = new PasswordField();
            ComboBox<User.Role> role = new ComboBox<>();
            role.getItems().addAll(User.Role.values());

            GridPane g = new GridPane();
            g.setHgap(10); g.setVgap(10);
            g.addRow(0, new Label("ASU ID:"), id);
            g.addRow(1, new Label("Password:"), pw);
            g.addRow(2, new Label("Role:"), role);
            dlg.getDialogPane().setContent(g);

            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setResultConverter(bt -> bt == ButtonType.OK);

            dlg.showAndWait().filter(r -> r).ifPresent(r -> {
                boolean ok = AuthenticationService.register(id.getText(), pw.getText(), role.getValue());
                setMessage(ok ? "Account created!" : "ID already exists.", ok ? Color.GREEN : Color.RED);
            });
        });


        /* ==========  Left column (form)  ========== */
        VBox form = new VBox(10,
            greeting, subText,
            new Label("ASU ID"),          idField,
            new Label("Password"),        pwField,
            new Label("Role Selection"),  roleBox,
            signInBtn, ssoBtn,
            messageLbl,
            signupBox
        );
        form.setPadding(new Insets(30));
        form.setMaxWidth(350);

        /* ==========  Right column (logo)  ========== */
        ImageView logoView = loadLogo();           // try to load /LoginPage/logo.png
        VBox logoBox = new VBox(logoView);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(30));

        /* ==========  Two-column layout  ========== */
        HBox mainLayout = new HBox(form, logoBox);
        mainLayout.setSpacing(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: white;");

        /* ==========  Scene / Stage  ========== */
        Scene scene = new Scene(mainLayout, 800, 500);
        stage.setTitle("Book Devils - Login");
        stage.setScene(scene);
        stage.show();
    }

    /* ============================================================
     *  Helpers
     * ============================================================
     */

    /** Opens ASU CAS login in the default browser (simple stub). */
    private void openSSO() {
        try {
            Desktop.getDesktop().browse(new URI("https://weblogin.asu.edu/cas/login"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Handles Sign-In button click */
    private void handleLogin() {
        String asuId   = idField.getText().trim();
        String pw      = pwField.getText();
        User.Role role = roleBox.getValue();

        // basic client-side validation
        if (asuId.isEmpty() || pw.isEmpty() || role == null) {
            setMessage("Please fill out all fields.", Color.RED);
            return;
        }

        // delegate to AuthenticationService
        User loggedIn = AuthenticationService.authenticate(asuId, pw, role);

        if (loggedIn != null) {
            setMessage("Login successful! Logged in as " + loggedIn.getRole(), Color.GREEN);
            // TODO: switch to Buyer/Seller/Admin dashboard here
        } else {
            setMessage("Invalid credentials.", Color.RED);
        }
    }

    /** Convenience to style the feedback label */
    private void setMessage(String text, Color color) {
        messageLbl.setText(text);
        messageLbl.setTextFill(color);
    }

    /**
     * Loads /LoginPage/logo.png from the classpath.
     * If not found, returns an empty ImageView so the layout width stays intact.
     */
    private ImageView loadLogo() {
        try {
            var url = getClass().getResource("/LoginPage/logo.png");
            if (url == null) {                       // fallback: try local file system
                url = getClass().getResource("file:logo.png");
            }
            Image img = (url != null)
                       ? new Image(url.toExternalForm())
                       : null;

            ImageView iv = (img != null) ? new ImageView(img) : new ImageView();
            iv.setFitWidth(250);
            iv.setPreserveRatio(true);
            return iv;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ImageView();                  // placeholder
        }
    }

    /** Java entry point */
    public static void main(String[] args) { launch(args); }
}
