package eashan.pokernea.windows;

import eashan.pokernea.PokerServer;
import eashan.pokernea.rmi.ClientRMI;
import eashan.pokernea.rmi.RMI;
import eashan.pokernea.room.Move;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.rmi.Naming;

public class GameWindow {

   public Scene getScene(Room room, int order, int ante, int balance) throws Exception {
      RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");

      BorderPane content = new BorderPane();
      Scene scene = new Scene(content, 1280, 920);

      Background background = new Background(
            new BackgroundImage(new Image("file:table2-920.jpg"),
                  BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                  new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                  new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true))
      );
      content.setBackground(background);

      HBox bottom = new HBox(38.75);
      bottom.setPrefHeight(225);
      HBox userCards = new HBox(25);
      userCards.setMinWidth(625);
      Util.setUserCards(userCards);

      VBox chatArea = new VBox(10);
      ListView chat = new ListView();
      chat.setMaxWidth(250);
      chat.setMaxHeight(120);
      chat.setStyle("-fx-background-radius: 0px;");
      HBox hBox = new HBox(10);
      TextArea area = new TextArea();
      area.setMaxHeight(25);
      area.setMinHeight(25);
      area.setMaxWidth(195);
      area.setStyle("-fx-background-radius: 0px;");
      Button button = new Button("Send");
      button.setMinWidth(45);
      button.setMinHeight(25);
      button.setMaxHeight(25);
      button.setStyle("-fx-background-radius: 0px;");
      button.setOnAction(e -> {
         try {
            rmi.sendChat(Util.getUser().getUsername() + ": " + area.getText());
            area.clear();
         } catch (Exception f) {
            f.printStackTrace();
         }
      });
      hBox.getChildren().addAll(area, button);
      chatArea.getChildren().addAll(chat, hBox);

      VBox buttons = new VBox(10);
      Window window = new Window();
      HBox horizontalButtons1 = new HBox(10);
      Button check = window.createGameButton("CHECK", 120, 50);
      Button fold = window.createGameButton("FOLD", 120, 50);
      horizontalButtons1.getChildren().addAll(check, fold);
      HBox horizontalButtons2 = new HBox(10);
      Button call = window.createGameButton("CALL ??" + ante, 120, 50);
      Button raise = window.createGameButton("RAISE ??" + ante*2, 120, 50);
      horizontalButtons2.getChildren().addAll(call, raise);

      Slider slider = new Slider(ante*2, balance, ante*2);
      slider.setShowTickMarks(true);
      slider.setShowTickLabels(true);
      slider.setMajorTickUnit(10); // space between major ticks
      slider.setMinorTickCount(9); // 1 point between major ticks
      slider.setBlockIncrement(1); // how much it changes when using arrows
      slider.setSnapToTicks(true);
      slider.setStyle("-fx-color: #FFFFFF; -fx-control-inner-background: #CB2622; -fx-thumb-color: #CB2622;");
      slider.valueProperty().addListener(e -> raise.setText("RAISE ??" + (int) slider.getValue()));

      buttons.getChildren().addAll(horizontalButtons1, horizontalButtons2, slider);

      fold.setOnAction(e -> {
         if (Util.isGo() && Util.isHasChecked()) {
            check.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            call.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            raise.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            fold.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #CB2622; -fx-background-radius: 0px; -fx-cursor: hand;");
            try {
               rmi.setMove(Util.getUser().getUsername(), Move.FOLD, 0);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
      });

      call.setOnAction(e -> {
         if (Util.isGo()) {
            check.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            fold.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            raise.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            call.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #CB2622; -fx-background-radius: 0px; -fx-cursor: hand;");
            try {
               rmi.setMove(Util.getUser().getUsername(), Move.BET, 0);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
      });

      raise.setOnAction(e -> {
         if (Util.isGo() && Util.isHasChecked()) {
            check.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            fold.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            call.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            raise.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #CB2622; -fx-background-radius: 0px; -fx-cursor: hand;");
            try {
               rmi.setMove(Util.getUser().getUsername(), Move.RAISE, (int) slider.getValue());
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
      });

      check.setOnAction(e -> {
         if (Util.isGo() && !Util.isHasChecked()) {
            raise.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            fold.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            call.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
            check.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #CB2622; -fx-background-radius: 0px; -fx-cursor: hand;");
            try {
               rmi.setMove(Util.getUser().getUsername(), Move.CHECK, 0);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
      });

      if (room.getUsers().size() == 3) {
         if (order == 2) {
            content.setLeft(room.getUsers().get(0).getShape(true, balance));
         } else {
            content.setLeft(room.getUsers().get(order+1).getShape(true, balance));
         }
         if (order == 0) {
            content.setRight(room.getUsers().get(order+2).getShape(true, balance));
         } else {
            content.setRight(room.getUsers().get(order-1).getShape(true, balance));
         }
      } else if (room.getUsers().size() == 4) { // Order = index in list
         // O: L-1 C-2 R-3
         // 1: L-2 C-3 R-0
         // 2: L-3 C-0 R-1
         // 3: L-0 C-1 R-2
         if (order == 0) {
            content.setLeft(room.getUsers().get(1).getShape(true, balance));
            content.setTop(room.getUsers().get(2).getShape(false, balance));
            content.setRight(room.getUsers().get(3).getShape(true, balance));
         } else if (order == 1) {
            content.setLeft(room.getUsers().get(2).getShape(true, balance));
            content.setTop(room.getUsers().get(3).getShape(false, balance));
            content.setRight(room.getUsers().get(0).getShape(true, balance));
         } else if (order == 2) {
            content.setLeft(room.getUsers().get(3).getShape(true, balance));
            content.setTop(room.getUsers().get(0).getShape(false, balance));
            content.setRight(room.getUsers().get(1).getShape(true, balance));
         } else if (order == 3) {
            content.setLeft(room.getUsers().get(0).getShape(true, balance));
            content.setTop(room.getUsers().get(1).getShape(false, balance));
            content.setRight(room.getUsers().get(2).getShape(true, balance));
         }
      }

      chatArea.setAlignment(Pos.BOTTOM_CENTER);
      buttons.setAlignment(Pos.BOTTOM_CENTER);

      bottom.getChildren().addAll(chatArea, userCards, buttons);
      bottom.setPadding(new Insets(0, 0, 25, 0));
      bottom.setAlignment(Pos.BOTTOM_CENTER);
      content.setBottom(bottom);

      VBox centre = new VBox(10);
      Button pot = window.createGameButtonC("POT: ??0", 150, 50);
      Button label;
      if (!Util.isClient()) {
         label = window.createGameButton("Click to start drawing cards.", 400, 50);
         Util.setLabel(label);
         label.setOnAction(f -> {
            Util.getGame().giveCards();

            label.setText("Click to add ante to the pot.");
            label.setOnAction(g -> {
               Util.getGame().addAnte();
               Util.getGame().rmis.forEach((r, u) -> {
                  try {
                     r.editLabel("Added ante to the pot");
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
               });
               try {
                  for (ClientRMI clientRMI : Util.getGame().rmis.keySet()) {
                     clientRMI.update(Util.getGame().rmis.get(rmi));
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
               update(pot, content.getTop(), content.getLeft(), content.getRight(), content.getBottom());

               label.setText("Start game");
               label.setOnAction(h -> {
                  Util.getGame().resetRound();
               });
            });
         });

      } else {
         label = window.createGameButton("Your cards will now be drawn.", 400, 50);
         label.setCursor(Cursor.DEFAULT);
         Util.setLabel(label);
      }
      centre.getChildren().addAll(pot, label);
      centre.setAlignment(Pos.CENTER);
      content.setCenter(centre);

      return scene;
   }

   private void update(Button pot, Node top, Node left, Node right, Node bottom) {
      pot.setText("??" + Util.getGame().getPot());
      // Left user
      VBox leftShape = (VBox) left;
      String leftUsername = ((Label) leftShape.getChildren().get(0)).getText();
      Label leftMoney = (Label) leftShape.getChildren().get(1);
      leftMoney.setText("??" + Util.getGame().getUserFromName(leftUsername).getBalance());

      // Right user
      VBox rightShape = (VBox) right;
      String rightUsername = ((Label) rightShape.getChildren().get(0)).getText();
      Label rightMoney = (Label) rightShape.getChildren().get(1);
      rightMoney.setText("??" + Util.getGame().getUserFromName(rightUsername).getBalance());

      // Top user
      if (top != null) {
         VBox topShape = (VBox) top;
         String topUsername = ((Label) topShape.getChildren().get(0)).getText();
         Label topMoney = (Label) topShape.getChildren().get(1);
         topMoney.setText("??" + Util.getGame().getUserFromName(topUsername).getBalance());
      }

      // Slider
      HBox bottomBox = (HBox) bottom;
      VBox buttons = (VBox) bottomBox.getChildren().get(2);
      Slider slider = (Slider) buttons.getChildren().get(2);
      slider.setMax(Util.getUser().getBalance());
   }

}