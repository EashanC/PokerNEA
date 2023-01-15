package eashan.pokernea.util;

import eashan.pokernea.card.Card;
import eashan.pokernea.card.CardSet;
import eashan.pokernea.card.CardSuit;
import eashan.pokernea.card.CardType;
import eashan.pokernea.database.User;
import eashan.pokernea.room.Game;
import eashan.pokernea.room.Move;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;

public class Util {

   private static @Getter @Setter boolean isClient;
   public static @Getter @Setter User user;
   public static @Getter @Setter Stage stage;

   public static String hash(String string) {
      String hash = Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
      return hash;
   }

   public static void checkPassword(PasswordField field) {
      String password = field.getText();
      // https://stackoverflow.com/questions/36097097/password-validate-8-digits-contains-upper-lowercase-and-a-special-character
      if (password.contains("a")) {

      }
      field.clear();
   }

   public static CardSet<Card> createCardSet() {
      CardSet<Card> cardSet = new CardSet<>();
      for (CardSuit suit : CardSuit.values()) {
         for (CardType type : CardType.values()) {
            Card card = new Card(suit, type);
            cardSet.add(card);
         }
      }
      return cardSet;
   }

   public static void test() {
      CardSet<Card> cardSet = new CardSet<>();
      for (CardSuit suit : CardSuit.values()) {
         for (CardType type : CardType.values()) {
            Card card = new Card(suit, type);
            cardSet.add(card);
         }
      }
      cardSet.shuffle();
   }

   public static String generateRoomCode() {
      return RandomStringUtils.random(5, true, false);
   }

   public static String getIpAddress() {
      try {
         Enumeration e = NetworkInterface.getNetworkInterfaces();
         while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
               InetAddress i = (InetAddress) ee.nextElement();
               if (i.getHostAddress().startsWith("192.168.")) {
                  return i.getHostAddress();
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   }

   public static @Getter @Setter HBox userCards;
   public static @Getter @Setter Game game;
   public static @Getter @Setter Move selected = null;
   public static @Getter @Setter Button label;

}
