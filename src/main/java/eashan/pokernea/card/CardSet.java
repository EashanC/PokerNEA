package eashan.pokernea.card;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class CardSet<C> extends LinkedList<C> {
   public void shuffle() {

      Object[] array = toArray();
      Random random = new Random();
      for (int i = 0; i < array.length-1; i++) {
         int index = random.nextInt(i + 1);
         Object object = array[index];
         array[index] = array[i];
         array[i] = object;
      }

      ListIterator<C> iter = listIterator();
      for (Object object : array) {
         iter.next();
         iter.set((C) object);
      }
   }

   public void setup() {
      for (CardSuit suit : CardSuit.values()) {
         for (CardType type : CardType.values()) {
            Card card = new Card(suit, type);
            add((C) card);
         }
      }
   }

}
