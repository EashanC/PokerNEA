package eashan.pokernea.database;

import eashan.pokernea.card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class GameUser {

   private final @Getter User user;
   private @Getter @Setter double balance;
   private @Getter @Setter LinkedList<Card> hand;

   public GameUser(User user) {
      this.user = user;
      this.balance = 100.00;
      this.hand = new LinkedList<>();
   }

}
