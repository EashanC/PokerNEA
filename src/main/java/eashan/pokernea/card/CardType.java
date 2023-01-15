package eashan.pokernea.card;

import lombok.Getter;

public enum CardType {

   ACE("Ace", 13),
   TWO("2", 1),
   THREE("3", 2),
   FOUR("4", 3),
   FIVE("5", 4),
   SIX("6", 5),
   SEVEN("7", 6),
   EIGHT("8", 7),
   NINE("9", 8),
   TEN("10", 9),
   JACK("J", 10),
   QUEEN("Q", 11),
   KING("K", 12);

   private final @Getter String displayName;
   private final @Getter int order;

   CardType(String displayName, int order) {
      this.displayName = displayName;
      this.order = order;
   }

   public static CardType get(int num) {
      if (num > 13) return null;
      for (CardType type : CardType.values()) {
         if (type.getOrder() == num) {
            return type;
         }
      }
      return null;
   }

}
