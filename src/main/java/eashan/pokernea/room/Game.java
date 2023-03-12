package eashan.pokernea.room;

import eashan.pokernea.PokerServer;
import eashan.pokernea.card.Calculations;
import eashan.pokernea.card.Card;
import eashan.pokernea.card.CardSet;
import eashan.pokernea.database.User;
import eashan.pokernea.rmi.ClientRMI;
import eashan.pokernea.util.SQLManager;
import eashan.pokernea.util.Util;
import eashan.pokernea.windows.GameWindow;
import eashan.pokernea.windows.WinnerBox;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

public class Game {

   private final Room room;
   private final @Getter LinkedList<User> users;
   private final CardSet<Card> cards;
   private final @Getter int balance, ante;
   private @Getter @Setter int pot;
   private @Setter @Getter String currentUser;
   private int playersLeft;
   private LinkedList<Boolean> remainingPlayers;
   private int currentIndex = 0;
   private boolean secondRound;
   public Map<ClientRMI, String> rmis;

   public Game(Room room, int balance, int ante) {
      this.room = room;
      this.users = room.getUsers();
      this.cards = new CardSet<>();
      cards.setup();
      this.ante = ante;
      this.balance = balance;
      this.rmis = new HashMap<>();

      for (User user : users) {
         if (user.getIpAddress().equals(PokerServer.getIpAddress())) continue;
         try {
            rmis.put((ClientRMI) Naming.lookup("rmi://" + user.getIpAddress() + ":8090/player"), user.getUsername());
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
      SQLManager.setupGame(room.getCode(), users.size());
      cards.shuffle();
      Calculations.giveCards(cards, users);
      users.forEach(u -> u.setCards(new Calculations(u.getCards()).sortHand()));
      users.forEach(u -> u.setBalance(balance));
      display();
   }

   public void giveCards() {
      for (User user : users) {
         try {
            if (Objects.equals(users.get(0), user)) {
               users.get(0).getCards().forEach(c -> Util.getUserCards().getChildren().add(c.getShape(105, 150)));
            } else {
               ClientRMI rmi = (ClientRMI) Naming.lookup("rmi://" + user.getIpAddress() + ":8090/player");
               rmi.showCards(user.getCards());
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public void addAnte() {
      System.out.println("Starting to add ante, " + pot);
      users.forEach(u -> {
         u.setBalance(balance - ante);
         pot += ante;
      });
      System.out.println("POT: " + pot);
   }

   private void update() {
      try {
         for (ClientRMI rmi : Util.getGame().rmis.keySet()) {
            rmi.update(Util.getGame().rmis.get(rmi));
         }
            BorderPane pane = (BorderPane) Util.getStage().getScene().getRoot();
            // Pot
            VBox centre = (VBox) pane.getCenter();
            Button potButton = (Button) centre.getChildren().get(0);
            potButton.setText("POT: £" + pot);

            // Left user
            VBox leftShape = (VBox) pane.getLeft();
            String leftUsername = ((Label) leftShape.getChildren().get(0)).getText();
            Label leftMoney = (Label) leftShape.getChildren().get(1);
            leftMoney.setText("£" + getUserFromName(leftUsername).getBalance());

            // Right user
            VBox rightShape = (VBox) pane.getRight();
            String rightUsername = ((Label) rightShape.getChildren().get(0)).getText();
            Label rightMoney = (Label) rightShape.getChildren().get(1);
            rightMoney.setText("£" + getUserFromName(rightUsername).getBalance());

            // Top user
            VBox topShape = (VBox) pane.getTop();
            String topUsername = ((Label) topShape.getChildren().get(0)).getText();
            Label topMoney = (Label) topShape.getChildren().get(1);
            topMoney.setText("£" + getUserFromName(topUsername).getBalance());

            // Slider
            HBox bottom = (HBox) pane.getBottom();
            VBox buttons = (VBox) bottom.getChildren().get(2);
            Slider slider = (Slider) buttons.getChildren().get(2);
            slider.setMax(getUserFromName(Util.getUser().getUsername()).getBalance());
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public void setMove(Move move, int raise) {
      System.out.println("set move");
      int temp = pot;
      switch (move) {
         case BET -> {
            User user = getUserFromName(currentUser);
            user.setBalance(user.getBalance() - ante);
            pot += ante;
            if (user.getIpAddress().equals(PokerServer.getIpAddress())) {
               Util.setHasChecked(true);
            } else {
               rmis.forEach((r, u) -> {
                  try {
                     r.setHasChecked(true);
                  } catch (RemoteException e) {
                     e.printStackTrace();
                  }
               });
            }
         }
         case FOLD -> {
            playersLeft--;
            remainingPlayers.set(currentIndex, false);
            if (playersLeft == 2) {
               calculateWinner();
            }
         }
         case RAISE -> {
            User user = getUserFromName(currentUser);
            user.setBalance(user.getBalance() - raise);
            pot += raise;
         }
         case CHECK -> {
            User currentPlayer = getUserFromName(currentUser);
            if (currentPlayer.getIpAddress().equals(PokerServer.getIpAddress())) {
               Util.setHasChecked(true);
            } else {
               try {
                  ClientRMI rmi = (ClientRMI) Naming.lookup("rmi://" + currentPlayer.getIpAddress() + ":8090/player");
                  rmi.setHasChecked(true);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      }
      update();

      User user1 = getUserFromName(currentUser);
      if (user1.getBalance() < 10) {
         playersLeft--;
         remainingPlayers.set(currentIndex, false);
         System.out.println(playersLeft);
      }

      if (playersLeft == 2) {
         System.out.println("Should perform? ************************************************************");
         calculateWinner();
      }

      System.out.println(currentUser + " has " + move.name() + "ed " + (pot-temp));
      System.out.println("checks done");
      boolean isPlaying = false;
      do {
         try {
            currentIndex++;
            if (currentIndex == users.size()) {
               secondRound = true;
               currentIndex = 0;
               System.out.println("Change cards?");
               endOfRound();
            }
            currentUser = users.get(currentIndex).getUsername();
            if (remainingPlayers.get(currentIndex)) isPlaying = true;
            else continue;

            String ipAddress = users.get(currentIndex).getIpAddress();
            if (ipAddress.equals(PokerServer.getIpAddress())) {
               Platform.runLater(() -> Util.getLabel().setText("It's your go! Select a move."));
               Util.setGo(true);
               System.out.println("servers go - set label");
            } else {
               ClientRMI currentRMI = (ClientRMI) Naming.lookup("rmi://" + ipAddress + ":8090/player");
               currentRMI.editLabel("It's your go! Select a move.");
               currentRMI.setGo(true);
               System.out.println("client " + ipAddress + " go, set label");
            }

            for (User user : users) {
               if (user.getIpAddress().equals(ipAddress)) continue;

               if (user.getIpAddress().equals(PokerServer.getIpAddress())) {
                  Platform.runLater(() -> Util.getLabel().setText("It's " + users.get(currentIndex).getUsername() + "'s go!"));
                  Util.setGo(false);
                  System.out.println("set label for server");
                  continue;
               }

               ClientRMI rmi = (ClientRMI) Naming.lookup("rmi://" + user.getIpAddress() + ":8090/player");
               rmi.editLabel("It's " + users.get(currentIndex).getUsername() + "'s go!");
               rmi.setGo(false);
               System.out.println("set label for " + user.getIpAddress());
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         System.out.println("new player should be set");
      } while (!isPlaying);
   }

   private void calculateWinner() {
      List<User> users = new ArrayList<>();
      for (int i = 0; i < remainingPlayers.size(); i++) {
         if (remainingPlayers.get(i)) {
            users.add(this.users.get(i));
         }
      }

      LinkedList<Card> user1Cards = users.get(0).getCards();
      LinkedList<Card> user2Cards = users.get(1).getCards();

      int user1Calculations = new Calculations(user1Cards).calculateHandRanking();
      int user2Calculations = new Calculations(user2Cards).calculateHandRanking();

      String toDisplay = "Error occured calculating winner!";
      if (user2Calculations > user1Calculations) {
         System.out.println(users.get(0).getUsername() + " Wins!");
         toDisplay = users.get(0).getUsername() + " has won!";
      } else if (user1Calculations > user2Calculations) {
         System.out.println(users.get(1).getUsername() + " Wins!");
         toDisplay = users.get(1).getUsername() + " has won!";
      } else {
         int winner = Calculations.compareHands(user1Calculations, user1Cards, user2Cards);
         if (winner == 0) {
            System.out.println("Draw");
            toDisplay = "It's a draw!";
         } else if (winner == 1) {
            System.out.println(users.get(0).getUsername() + " wins!");
            toDisplay = users.get(0).getUsername() + " has won!";
         } else if (winner == 2) {
            System.out.println(users.get(1).getUsername() + " wins!");
            toDisplay = users.get(1).getUsername() + " has won!";
         }
      }

      try {
         SQLManager.addPlayerInfo(room.getCode(), users, balance, toDisplay);
      } catch (Exception e) {
         e.printStackTrace();
      }

      String finalToDisplay = toDisplay;
      rmis.forEach((r, n) -> { // For clients
         try {
            r.displayWinner(finalToDisplay);
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      });
      new WinnerBox(finalToDisplay).display(); // Server
   }

   public void resetRound() {
      System.out.println("Start of resetRound");
      playersLeft = users.size();
      remainingPlayers = new LinkedList<>();
      users.forEach(u-> remainingPlayers.add(true));
      setCurrentUser(Util.getUser().getUsername());
      rmis.forEach((r, u) -> {
         try {
            r.setGo(false);
            r.setHasChecked(false);
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      });
      Util.setHasChecked(false);
      Util.setGo(true);
      // TODO reset isGo and hasChecked for every user

      if (!secondRound) {
         Util.getLabel().setText("It's your go! Select a move.");
         rmis.forEach((r, u) -> {
            try {
               r.editLabel("It's " + Util.getUser().getUsername() + "'s go!");
            } catch (Exception e) {
               e.printStackTrace();
            }
         });
      }
      System.out.println("End of reset round");
   }

   public User getUserFromName(String username) {
      for (User user : users) {
         if (user.getUsername().equalsIgnoreCase(username)) {
            return user;
         }
      }
      return null;
   }

   public void endOfRound() {
      rmis.forEach((k, v) -> {
         try {
            User user = getUserFromName(v);
            boolean[] raisedCards = k.raisedCards();
            System.out.println(Arrays.toString(raisedCards));
            for (int i = 0; i < 5; i++) {
               boolean b = raisedCards[i];
               if (b) {
                  cards.addFirst(user.getCards().get(i));
                  user.getCards().remove(i);
                  user.getCards().add(cards.getLast());
                  cards.removeLast();
               }
            }
            System.out.println(user.getCards());
            user.setCards(new Calculations(user.getCards()).sortHand());
            System.out.println(user.getCards());
            k.showCards(user.getCards());
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      });
      int x = 0;
      for (Node node : Util.getUserCards().getChildren()) {
         VBox shape = (VBox) node;
         Rectangle blank = (Rectangle) shape.getChildren().get(1);
         if (blank.getHeight() > 0) {
            cards.addFirst(users.get(0).getCards().get(x));
            users.get(0).getCards().remove(x);
            users.get(0).getCards().add(cards.getLast());
            cards.removeLast();
         }
         x++;
      }
      users.get(0).setCards(new Calculations(users.get(0).getCards()).sortHand());
      Platform.runLater(() -> Util.getUserCards().getChildren().clear());
      HBox userCards = Util.getUserCards();
      Platform.runLater(() -> userCards.getChildren().clear());
      users.get(0).getCards().forEach(c -> Platform.runLater(() -> userCards.getChildren().add(c.getShape(105, 150))));

   }

}