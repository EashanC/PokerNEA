package eashan.pokernea.database;

import eashan.pokernea.PokerServer;
import eashan.pokernea.card.Card;
import eashan.pokernea.rmi.RMI;
import eashan.pokernea.util.Database;
import eashan.pokernea.util.SQL;
import eashan.pokernea.util.Util;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.rmi.Naming;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {

   public static boolean checkLogin(String username, String hashedPassword) {
      if (username == "" || hashedPassword == "" || username == null || hashedPassword == null) return false;
      try {
         String password = null;
         if (Util.isClient()) {
            RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");
            List<Object> result = rmi.sql(SQL.Level.EXECUTE, "SELECT * FROM login WHERE username = '" + username  + "'", "password", SQL.Type.STRING);
            if (result.size() == 0) return false;
            password = (String) result.get(0);
         } else {
            ResultSet rs = SQL.send(SQL.Level.EXECUTE, "SELECT * FROM login WHERE username = '" + username  + "'");
            if (rs.next()) {
               password = rs.getString("password");
            }
         }
         if (password.equals(hashedPassword)) {
            return true;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static boolean createAccount(String username, String password) {
      if (username == "" || password == "" || username == null || password == null) return false;
      try {
         if (Util.isClient()) {
            RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");
            List<Object> result = rmi.sql(SQL.Level.EXECUTE, "SELECT * FROM login WHERE username = '" + username + "'", "", SQL.Type.STRING);
            if (result.size() == 0) {
               rmi.sql(SQL.Level.UPDATE, "INSERT INTO login (username, password) VALUES ('" + username + "', '" + password + "')");
               return true;
            }
         } else {
            Database database = PokerServer.getDatabase();
            ResultSet rs = database.execute("SELECT * FROM login WHERE username = '" + username  + "'");
            if (!rs.next()) {
               database.update("INSERT INTO login (username, password) VALUES ('" + username + "', '" + password + "')");
               return true;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static User emptyUser() {
      return new User("", "");
   }

   public static boolean isEmptyUser(User user) {
      return (Objects.equals(user, emptyUser()));
   }

   private final @Getter String username;
   private @Getter @Setter LinkedList<Card> cards;
   private @Getter @Setter int balance;

   private @Getter String ipAddress;

   public User(String ipAddress, String username) {
      this.username = username;
      this.ipAddress = ipAddress;
      this.cards = new LinkedList<>();
      this.balance = 0;
   }

   @Override
   public String toString() {
      return username;
   }

   public void leaveRoom(String code) {
      PokerServer.getRoomManager().getRoom(code).removeUser(this);
   }

   public VBox getShape(boolean bottomGap, int sBal) {
      Label username = new Label(getUsername().toUpperCase());
      username.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF;");
      username.setFont(Font.font("Leelawadee", FontWeight.BOLD, 16));
      username.setMinSize(154, 20);
      username.setMaxSize(154, 20);
      username.setAlignment(Pos.CENTER);

      Label money = new Label("£" + sBal);
      money.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF;");
      money.setFont(Font.font("Leelawadee", FontWeight.BOLD, 12));
      money.setMinSize(154, 20);
      money.setAlignment(Pos.CENTER);

      Rectangle blank = new Rectangle(154, 10);
      blank.setFill(Color.TRANSPARENT);

      Rectangle rectangle = new Rectangle(154, 40);
      ImagePattern pattern = new ImagePattern(
            new Image("file:cards.png", 616, 160, true, true)
      );
      rectangle.setFill(pattern);

      Rectangle blank2 = new Rectangle();
      if (bottomGap) {
         blank2.setWidth(154);
         blank2.setHeight(10);
         blank2.setFill(Color.TRANSPARENT);
      }

      VBox box = new VBox();
      box.setAlignment(Pos.CENTER);
      box.getChildren().addAll(username, money, blank, rectangle, blank2);
      box.setPadding(new Insets(25));
      return box;
   }

}
