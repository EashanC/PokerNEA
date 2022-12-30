package eashan.pokernea.room;

import eashan.pokernea.card.Calculations;
import eashan.pokernea.card.Card;
import eashan.pokernea.card.CardSet;
import eashan.pokernea.database.User;
import eashan.pokernea.rmi.ClientRMI;
import eashan.pokernea.util.Util;
import eashan.pokernea.windows.GameWindow;
import javafx.application.Platform;
import lombok.Getter;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Objects;

public class Game {

   private Room room;
   private LinkedList<User> users;
   private CardSet<Card> cards;
   private @Getter int balance, ante;
   public Game(Room room, int balance, int ante) {
      this.room = room;
      this.users = room.getUsers();
      this.cards = new CardSet<>();
      cards.setup();
      this.ante = ante;
      this.balance = balance;
   }

   private int getOrder(User user) {
      return users.indexOf(user);
   }

   public void display() {
      for (User user : users) {
         try {
            if (Objects.equals(user, Util.getUser())) {
               Util.getStage().setScene(new GameWindow().getScene(room, getOrder(user), ante, balance));
            } else {
               ClientRMI rmi = (ClientRMI) Naming.lookup("rmi://" + user.getIpAddress() + ":8090/player");
               Platform.runLater(() -> {
                  try {
                     rmi.showGame(room, getOrder(user), ante, balance);
                  } catch (RemoteException e) {
                     e.printStackTrace();
                  }
               });
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public void start() throws InterruptedException {
      cards.shuffle();
      Calculations.giveCards(cards, users);
      users.forEach(u -> u.setCards(new Calculations(u.getCards()).sortHand()));
      display();
   }

   public void giveCards() {
      for (User user : users) {
         try {
            if (Objects.equals(users.get(0), user)) {
               users.get(0).getCards().forEach(c -> {
                  /*try {          // with a gap - doesn't really work unless it happens simulatenously
                     Thread.sleep(1000);
                     Util.getUserCards().getChildren().add(c.getShape(105, 150));
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }*/
                  Util.getUserCards().getChildren().add(c.getShape(105, 150));
               });
            } else {
               ClientRMI rmi = (ClientRMI) Naming.lookup("rmi://" + user.getIpAddress() + ":8090/player");
               rmi.showCards(user.getCards());
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

}
