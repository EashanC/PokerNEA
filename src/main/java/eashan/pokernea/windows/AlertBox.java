package eashan.pokernea.windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

   private String message;

   public AlertBox(String message) {
      this.message = message;
   }

   public void display() {
      Stage window = new Stage();

      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("Error!");
      window.setHeight(100);
      window.setWidth(150);
      window.setResizable(false);

      Label label = new Label(message);
      Button button = new Button("Close");
      button.setOnAction(e -> window.close());

      VBox layout = new VBox(10);
      layout.getChildren().addAll(label, button);
      layout.setAlignment(Pos.CENTER);

      Scene scene = new Scene(layout);
      window.setScene(scene);
      window.showAndWait();
   }

}
