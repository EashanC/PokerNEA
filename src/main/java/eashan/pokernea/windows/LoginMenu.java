package eashan.pokernea.windows;

import eashan.pokernea.database.User;
import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LoginMenu {

   public Scene getScene() {
      Window window = new Window();

      Label usernameLabel = window.createLabel("Username:");
      TextField username = window.createTextField();
      Label passwordLabel = window.createLabel("Password:");
      PasswordField password = window.createPasswordField();
      Button login = window.createButton("Login");
      Button create = window.createLabelButton("Create account");
      Label error = window.createErrorLabel(" ", Color.web("#FF0000"));

      login.setDefaultButton(true); // same functionality as above

      login.setOnAction(e -> {
         error.setTextFill(Color.web("#FFFFFF"));
         boolean canLogin = User.checkLogin(username.getText(), Util.hash(password.getText()));
         if (canLogin) {
            Util.getStage().setScene(new ChoiceMenu().getScene());
            Util.getStage().setTitle("Poker: " + username.getText());
            User user = new User(Util.getIpAddress(), username.getText());
            Util.setUser(user);
         } else {
            error.setTextFill(Color.web("#FF0000"));
            error.setText("Incorrect username/password");
         }
         username.clear();
         password.clear();
      });
      create.setOnAction(e -> {
         error.setTextFill(Color.web("#FFFFFF"));
         boolean canCreate = User.createAccount(username.getText(), Util.hash(password.getText()));
         if (canCreate) {
            error.setTextFill(Color.web("#1C2833"));
            error.setText("Account created!");
         } else {
            error.setTextFill(Color.web("#FF0000"));
            error.setText("Account already exists");
         }
         username.clear();
         password.clear();
      });

      return window.create(usernameLabel, username, passwordLabel, password, login, create, error);
   }

}
