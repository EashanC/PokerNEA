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
import lombok.Setter;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

   private Room room;
   private @Getter LinkedList<User> users;
   private CardSet<Card> cards;
   private final @Getter int balance, ante;
   private @Getter @Setter int pot;

   public Game(Room room, int balance, int ante) {
      this.room = room;
      this.users = room.getUsers();
      this.cards = new CardSet<>();
      cards.setup();
      this.ante = ante;
      this.balance = balance;
      this.rmis = new ArrayList<>();

      for (User user : users) {
         try {
            rmis.add((ClientRMI) Naming.lookup("rmi://" + user.getIpAddress() + ":8090/player"));
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
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
      // Shuffles the card deck and hands it out to the users
      cards.shuffle();
      Calculations.giveCards(cards, users); // Maybe add them to the user's card list and then hand them out?
      users.forEach(u -> u.setCards(new Calculations(u.getCards()).sortHand())); // ensures the cards are shown in order of value
      users.forEach(u -> u.setBalance(balance));

      // Shows window to all users
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

   // Needs to be activated from GameWindow of the server, via a label
   public void addAnte() {
      System.out.println("Starting to add ante, " + pot);
      users.forEach(u -> {
         u.setBalance(balance - ante);
         pot += ante;
      });
      System.out.println("POT: " + pot);
   }

   private User getUser(String username) {
      for (User user : users) {
         if (user.getUsername().equals(username)) return user;
      }
      return null;
   }

   /**
    * Add listener on pot - so it automatically updates using RMI? something like that...
    */

   private Move getChoice(int index){
      try {
         if (Util.isClient()) {
            ClientRMI rmi = rmis.get(index);
            Move move = rmi.getSelected();
            while (move == null) move = rmi.getSelected();
            return move;
         } else {
            Move move = Util.getSelected();
            while (move == null) move = Util.getSelected();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public void startRound() { // A round = each player has two goes (providing they don't fold)
      // Starts at user[1]
      int playersLeft = users.size();
      Move previousMove = Move.CHECK;
      List<Boolean> remainingPlayers = new LinkedList<>();
      users.forEach(u -> remainingPlayers.add(true));
      int index = 1;
      for (int i = 0; i < users.size()*2; i++) { // Goes through twice
         if (!remainingPlayers.get(index)) continue;
         User user = users.get(index);
         // Display above cards the options available to the user - start with only BET and CHECK
         switch (previousMove) {
            case CHECK -> { // After this remove the option for check - only BET, RAISE, FOLD
               Move move = getChoice(index);
               if (move == Move.CHECK) {
                  continue; // Nothing happens, play moves over
               } else if (move == Move.BET) {
                  previousMove = Move.BET;
                  user.setBalance(user.getBalance() - ante);
                  pot += ante;
               }
            }
            default -> {
               Move move = getChoice(index);
               if (move == Move.BET) {
                  user.setBalance(user.getBalance() - ante);
                  pot += ante;
               } else if (move == Move.RAISE) {
                  int raised = 1; // get value from slider
                  user.setBalance(user.getBalance() - raised);
                  pot += raised;
               } else if (move == Move.FOLD) {
                  // Change player colour
                  playersLeft--;
                  remainingPlayers.set(index, false);
                  // Remove player from a list of players in the game? Something like that
                  // Might have to iterate over a different list, keeping count of the index?
                  // Maybe have a list full of the index's of the users still in the round, and then remove them -> User user = user.get(index);
                  // This would allow for a while loop -> while (playersLeft > 2) - otherwise to compare hands.
               }
            }
         }
         try {
            rmis.get(index).resetSelected();
         } catch (Exception f) {
            f.printStackTrace();
         }
         index++;
         if (index == remainingPlayers.size()) index = 0;
         if (playersLeft == 2) break;
         output();
      }

      // Actually get the two players left in the round
      List<User> remainingUsers = new ArrayList<>();
      AtomicInteger x = new AtomicInteger();
      remainingPlayers.forEach(u -> {
         if (u) remainingUsers.add(users.get(remainingPlayers.indexOf(x)));
         x.getAndIncrement();
      });
      int user1rank = new Calculations(remainingUsers.get(0).getCards()).calculateHandRanking();
      int user2rank = new Calculations(remainingUsers.get(1).getCards()).calculateHandRanking();
      if (user1rank > user2rank) {
         System.out.println("User 2 wins");
      } else if (user1rank < user2rank) {
         System.out.println("User 1 wins");
      } else {
         boolean is1winner = Calculations.compareHands(user1rank, remainingUsers.get(0).getCards(), remainingUsers.get(1).getCards()); // Create method
         if (is1winner) {
            System.out.println("User 1 wins");
         } else {
            System.out.println("User 2 wins");
         }
      }

   }

   public List<ClientRMI> rmis;

   private void output() {
      System.out.println("POT: " + pot);
      users.forEach(u -> System.out.println(u.getUsername() + ": " + u.getBalance()));
   }

   public User getUserFromName(String username) {
      for (User user : users) {
         if (user.getUsername().equalsIgnoreCase(username)) {
            return user;
         }
      }
      return null;
   }

}
