package eashan.pokernea.card;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public record Card(@Getter CardSuit suit, @Getter CardType type) implements Serializable {

   public static final Comparator<Card> CARD_COMPARATOR = Comparator.comparingInt(o -> o.getType().getOrder());
   public String getURL() {
      return "file:cards/" + suit.toString().toLowerCase() + "-" + type.name().toLowerCase() + ".jpg";
   }

   public int getOrder() {
      return type.getOrder();
   }

   public VBox getShape(double width, double height) {
      AtomicBoolean isRaised = new AtomicBoolean(false);
      VBox box = new VBox();

      Rectangle rectangle = new Rectangle(width, height);
      rectangle.setArcHeight(15);
      rectangle.setArcWidth(15);
      ImagePattern pattern = new ImagePattern(
            new Image(getURL(), width*2.5, height*2.5, true, true)
      );
      rectangle.setFill(pattern);
      rectangle.setStyle("-fx-cursor: hand;");

      Timer timer = new Timer();

      Rectangle blank = new Rectangle(width, 0);
      blank.setFill(Color.TRANSPARENT);
      rectangle.setOnMouseClicked(e -> {
         if (isRaised.get()) {
            isRaised.set(false);
            timer.scheduleAtFixedRate(new TimerTask() {
               @Override
               public void run() {
                  if (blank.getHeight() == 0) cancel();
                  blank.setHeight(blank.getHeight()-0.5);
               }
            }, 0, 1);
         } else {
            isRaised.set(true);
            timer.scheduleAtFixedRate(new TimerTask() {
               @Override
               public void run() {
                  if (blank.getHeight() == 25) cancel();
                  blank.setHeight(blank.getHeight()+0.5);
               }
            }, 0, 1);
         }
      });

      box.getChildren().addAll(rectangle, blank);
      box.setAlignment(Pos.BOTTOM_CENTER);
      return box;
   }

   @Override
   public String toString() {
      return "(" + type.getDisplayName() + " of " + suit.name() + "S)";
   }
}
