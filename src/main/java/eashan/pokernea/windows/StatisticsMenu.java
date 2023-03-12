package eashan.pokernea.windows;

import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class StatisticsMenu {

   public Scene getScene() {
      Window window = new Window();

      VBox left = new VBox(10);
      Label startDateLabel = window.createLabel("Start Date:");
      DatePicker startDate = new DatePicker();
      startDate.setValue(LocalDate.of(2005, 8, 25));
      left.getChildren().addAll(startDateLabel, startDate);

      VBox right = new VBox(10);
      Label endDateLabel = window.createLabel("End Date:");
      DatePicker endDate = new DatePicker();
      endDate.setValue(LocalDate.now());
      right.getChildren().addAll(endDateLabel, endDate);

      HBox top = new HBox(10);
      top.getChildren().addAll(left, right);

      Label wins = window.createLabel("Wins: 0");
      Label royalFlushes = window.createLabel("Royal Flushes: 0");
      Label moneyBet = window.createLabel("Money Bet: £0");

      Button back = window.createButton("Back");
      back.setOnAction(e -> {
         Util.getStage().setScene(new ChoiceMenu().getScene());
      });

      return window.create(top, wins, royalFlushes, moneyBet, back);
   }

   public void update() {
      Stage stage = Util.getStage();
      Scene scene = stage.getScene();
      VBox content = (VBox) scene.getRoot();
      VBox shape = (VBox) content.getChildren().get(0);


   }

}
