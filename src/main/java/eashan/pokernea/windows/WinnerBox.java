package eashan.pokernea.windows;

import eashan.pokernea.util.Util;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WinnerBox {

   private String message;

   public WinnerBox(String message) {
      this.message = message;
   }

   public void display() {
      Stage window = new Stage();

      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("Game Over!");
      window.setHeight(100);
      window.setWidth(150);

      Label label = new Label(message);
      Button button = new Button("Return to Menu");

      button.setOnAction(e -> {
         window.close();
         Util.getStage().setScene(new ChoiceMenu().getScene());
      });

      HBox top = new HBox(10);
      top.getChildren().add(label);
      top.setAlignment(Pos.CENTER);

      HBox centre = new HBox(10);
      centre.getChildren().addAll(button);
      centre.setAlignment(Pos.CENTER);

      BorderPane borderPane = new BorderPane();
      borderPane.setTop(top);
      borderPane.setCenter(centre);

      Scene scene = new Scene(borderPane);
      window.setScene(scene);
      window.setResizable(false);
      window.showAndWait();
   }

}
