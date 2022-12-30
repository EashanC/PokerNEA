package eashan.pokernea.windows;

import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class GameWindow {

   /**
    * Max height of 'bottom is 150!'
    */
   public Scene getScene(Room room, int order, int ante, int balance) {
      //Util.getStage().setResizable(true);
      //Util.getStage().setMinHeight(720);
      //Util.getStage().setMinWidth(1280);
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
         chat.getItems().add(Util.getUser().getUsername() + ": " + area.getText());
         area.clear();
      });
      hBox.getChildren().addAll(area, button);
      chatArea.getChildren().addAll(chat, hBox);

      VBox buttons = new VBox(10);
      Window window = new Window();
      Button fold = window.createGameButton("FOLD", 250, 50);
      HBox horizontalButtons = new HBox(10);
      Button call = window.createGameButton("CALL £" + ante, 120, 50);
      Button raise = window.createGameButton("RAISE £" + ante*2, 120, 50);
      horizontalButtons.getChildren().addAll(call, raise);

      Slider slider = new Slider(ante*2, balance, ante*2);
      slider.setShowTickMarks(true);
      slider.setShowTickLabels(true);
      slider.setMajorTickUnit(10); // space between major ticks
      slider.setMinorTickCount(9); // 1 point between major ticks
      slider.setBlockIncrement(1); // how much it changes when using arrows
      slider.setSnapToTicks(true);
      slider.setStyle("-fx-color: #FFFFFF; -fx-control-inner-background: #CB2622; -fx-thumb-color: #CB2622;");
      slider.valueProperty().addListener(e -> {
         raise.setText("RAISE £" + (int) slider.getValue());
      });

      buttons.getChildren().addAll(fold, horizontalButtons, slider);

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
      } else if (room.getUsers().size() == 4) {
         content.setLeft(room.getUsers().get(1).getShape(true, balance));
         content.setTop(room.getUsers().get(2).getShape(false, balance));
         content.setRight(room.getUsers().get(3).getShape(true, balance));
      } else {
         content.setRight(room.getUsers().get(0).getShape(false, balance));
         content.setLeft(room.getUsers().get(0).getShape(false, balance));
      }

      chatArea.setAlignment(Pos.BOTTOM_CENTER);
      buttons.setAlignment(Pos.BOTTOM_CENTER);
      //userCards.setAlignment(Pos.BOTTOM_CENTER);

      //chatArea.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
      //userCards.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
      //buttons.setStyle("-fx-border-color: red; -fx-border-width: 1px;");

      bottom.getChildren().addAll(chatArea, userCards, buttons);
      bottom.setPadding(new Insets(0, 0, 25, 0));
      bottom.setAlignment(Pos.BOTTOM_CENTER);
      content.setBottom(bottom);

      VBox centre = new VBox();
      Button label = window.createGameButton("Your cards will now be drawn.", 400, 50);
      label.setOnAction(f -> {
         Util.getGame().giveCards();
      });
      centre.getChildren().add(label);
      centre.setAlignment(Pos.CENTER);
      content.setCenter(centre);

      return scene;
   }

}
