package eashan.pokernea.windows;

import eashan.pokernea.PokerServer;
import eashan.pokernea.room.Game;
import eashan.pokernea.rmi.RMI;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomMenu {

   private final Room room;
   private ListView listView;

   public RoomMenu(Room room) {
      this.room = room;
   }

   public Scene getScene() {
      Window window = new Window();
      Label roomCodeLabel = window.createLabel("Room Code:");
      Label code = window.createBigLabel(room.getCode());
      ListView users = window.createList(room);
      this.listView = users;

      HBox buttons = new HBox(10);
      buttons.setAlignment(Pos.CENTER);

      Button leaveRoom;
      if (!Util.isClient()) {
         Button startGame = window.createHalfButton("Start");

         startGame.setOnAction(e -> {
            show();
         });

         leaveRoom = window.createHalfLabelButton("Leave");
         buttons.getChildren().addAll(startGame, leaveRoom);
      } else {
         leaveRoom = window.createLabelButton("Leave room");
         buttons.getChildren().add(leaveRoom);
      }

      // when any of the buttons are pressed, cancel the executor
      ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
      if (!Util.isClient()) {
         Thread thread = new Thread(() -> Platform.runLater(() -> {
            listView.setItems(FXCollections.observableArrayList(room.getUsers()));
            listView.refresh();
         }));
         executor.scheduleAtFixedRate(thread, 0, 10, TimeUnit.MILLISECONDS);
      } else {
         try {
            RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");
            Thread thread = new Thread(() -> Platform.runLater(() -> {
               try {
                  Room newRoom = rmi.getRoom(room.getCode());
                  listView.setItems(FXCollections.observableArrayList(newRoom.getUsers()));
                  listView.refresh();
               } catch (RemoteException e) {
                  e.printStackTrace();
               }
            }));
            executor.scheduleAtFixedRate(thread, 0, 10, TimeUnit.MILLISECONDS);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      leaveRoom.setOnAction(f -> {
         try {
            RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");
            rmi.leaveRoom(room.getCode(), Util.getUser());
            executor.shutdown();
            Util.getStage().setScene(new ChoiceMenu().getScene());
         } catch (Exception e) {
            e.printStackTrace();
         }
      });

      return window.create(roomCodeLabel, code, users, buttons);
   }

   public void show() {
      Stage stage = new Stage();
      HBox container = new HBox();
      VBox box = new VBox(10);
      Window window = new Window();

      Label balanceLabel = window.createLabel("Set starting balance: £100");
      Slider balance = window.createSlider(50, 250, 100);
      Label anteLabel = window.createLabel("Set ante amount: £10");
      Slider ante = window.createSlider(5, (int) balance.getValue()/5, 10);
      Button button = window.createButton("Start Game");

      balance.valueProperty().addListener(s -> {
         ante.setMax((int) balance.getValue()/5);
         if (ante.getValue() > balance.getValue()/5) ante.setValue(ante.getMax());
         balanceLabel.setText("Set starting balance: £" + (int) balance.getValue());
      });

      ante.valueProperty().addListener(s -> {
         anteLabel.setText("Set ante amount: £" + (int) ante.getValue());
      });

      button.setOnAction(e -> {
         // Create a new method to start game, which first makes sure there are more than x number of players
         // Then sends a signal out for all clients to display GameWindow
         // Also if server leaves room, then all players leave room as well.
         Game game = new Game(room, (int) balance.getValue(), (int) ante.getValue());
         Util.setGame(game);
         try {
            game.start();
         } catch (InterruptedException ex) {
            ex.printStackTrace();
         }
         stage.hide();
      });

      box.getChildren().addAll(balanceLabel, balance, anteLabel, ante, button);
      HBox.setMargin(box, new Insets(50));
      container.getChildren().add(box);
      container.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), null, null)));

      Scene scene = new Scene(container, 300, 300);
      stage.setScene(scene);
      stage.setResizable(false);
      stage.showAndWait();
   }

}
