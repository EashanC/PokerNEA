package eashan.pokernea.windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

   private String message;
   private boolean answer;

   public ConfirmBox(String message) {
      this.message = message;
   }

   public void display() {
      Stage window = new Stage();

      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("");
      window.setHeight(100);
      window.setWidth(150);

      Label label = new Label(message);
      Button yes = new Button("Yes");
      Button no = new Button("No");

      yes.setOnAction(e -> {
         answer = true;
         window.close();
      });

      no.setOnAction(e -> {
         answer = false;
         window.close();
      });

      HBox top = new HBox(10);
      top.getChildren().add(label);
      top.setAlignment(Pos.CENTER);

      HBox centre = new HBox(10);
      centre.getChildren().addAll(yes, no);
      centre.setAlignment(Pos.CENTER);

      BorderPane borderPane = new BorderPane();
      borderPane.setTop(top);
      borderPane.setCenter(centre);

      Scene scene = new Scene(borderPane);
      window.setScene(scene);
      window.setResizable(false);
      window.showAndWait();
   }

   public boolean getAnswer() {
      return answer;
   }

}
