package eashan.pokernea.util;

import eashan.pokernea.database.User;
import eashan.pokernea.room.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Window {

   private final Font font = Font.font("Leelawadee", FontWeight.BOLD, 12);

   public Scene create(Node... nodes) {
      VBox content = new VBox();

      // Main background
      Background background = new Background(
            new BackgroundImage(new Image("file:bg.jpg"),
                  BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                  new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                  new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true))
      );
      content.setBackground(background);

      // White box
      VBox shape = new VBox(10);
      shape.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), null, null)));
      shape.setMinSize(300, 300);
      shape.setMaxSize(300, 300);
      shape.setAlignment(Pos.CENTER);

      shape.getChildren().addAll(nodes);
      VBox.setMargin(shape, new Insets(75, 150, 75, 150));
      content.getChildren().add(shape);
      return new Scene(content, 600, 450);
   }

   public Label createLabel(String string) {
      Label label = new Label(string);
      label.setFont(font);
      return label;
   }

   public Label createLabel(String string, int fontSize) {
      Label label = new Label(string);
      label.setFont(Font.font("Leelawadee", FontWeight.BOLD, fontSize));
      return label;
   }

   public Label createBigLabel(String string) {
      Label label = new Label(string);
      label.setFont(Font.font("Leelawadee", FontWeight.BOLD, 24));
      label.setAlignment(Pos.CENTER);
      return label;
   }

   public Label createErrorLabel(String string, Color color) {
      Label label = createLabel(string);
      label.setTextFill(color);
      label.setAlignment(Pos.CENTER);
      return label;
   }

   public Button createButton(String text) {
      Button button = new Button(text);
      button.setFont(font);
      button.setMinSize(200, 30);
      button.setStyle("-fx-background-color: #1C2833; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
      return button;
   }

   public Button createHalfButton(String text) {
      Button button = createButton(text);
      button.setMinSize(95, 30);
      return button;
   }
   public Button createLabelButton(String text) {
      Button button = new Button(text);
      button.setFont(font);
      button.setMinSize(200, 30);
      button.setStyle("-fx-background-radius: 0px; -fx-border-width: 2px; -fx-border-color: #1C2833; -fx-cursor: hand;");
      return button;
   }

   public Button createHalfLabelButton(String text) {
      Button button = createLabelButton(text);
      button.setMinSize(95, 30);
      return button;
   }

   public TextField createTextField() {
      TextField field = new TextField();
      field.setStyle("-fx-background-radius: 0px; -fx-border-width: 2px; -fx-border-color: #1C2833");
      field.setMaxSize(200, 25);
      return field;
   }

   public PasswordField createPasswordField() {
      PasswordField field = new PasswordField();
      field.setStyle("-fx-background-radius: 0px; -fx-border-width: 2px; -fx-border-color: #1C2833");
      field.setMaxSize(200, 25);
      return field;
   }

   public ListView createList(Room room) {
      ListView<User> listView = new ListView();
      ObservableList<User> items = FXCollections.observableArrayList(room.getUsers());
      listView.setItems(items);
      listView.setMaxSize(200, 150);
      return listView;
   }

   public Button createGameButton(String text, int width, int height) {
      Button button = new Button(text);
      button.setFont(font);
      button.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: hand;");
      button.setMinSize(width, height);
      return button;
   }

   public Button createGameButtonC(String text, int width, int height) {
      Button button = new Button(text);
      button.setFont(font);
      button.setStyle("-fx-background-color: #CB2622; -fx-text-fill: #FFFFFF; -fx-background-radius: 0px; -fx-cursor: default;");
      button.setMinSize(width, height);
      return button;
   }

   public Slider createSlider(int min, int max, int start) {
      Slider slider = new Slider(min, max, start);
      slider.setShowTickLabels(true);
      slider.setShowTickMarks(true);
      slider.setMajorTickUnit(10);
      slider.setMinorTickCount(9);
      slider.setBlockIncrement(1);
      slider.setSnapToTicks(true);
      slider.setStyle("-fx-color: #FFFFFF; -fx-control-inner-background: #CB2622; -fx-thumb-color: #CB2622;");
      return slider;
   }

}
