package eashan.pokernea.card;

import lombok.Getter;

public enum CardType {

   ACE("Ace", 1),
   TWO("2", 2),
   THREE("3", 3),
   FOUR("4", 4),
   FIVE("5", 5),
   SIX("6", 6),
   SEVEN("7", 7),
   EIGHT("8", 8),
   NINE("9", 9),
   TEN("10", 10),
   JACK("J", 11),
   QUEEN("Q", 12),
   KING("K", 13);

   private final @Getter String displayName;
   private final @Getter int order;

   CardType(String displayName, int order) {
      this.displayName = displayName;
      this.order = order;
   }

   public static CardType get(int num) {
      if (num > 14) return null;
      if (num == 14) num = 1;
      for (CardType type : CardType.values()) {
         if (type.getOrder() == num) {
            return type;
         }
      }
      return null;
   }

}
