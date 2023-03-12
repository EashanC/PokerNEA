package eashan.pokernea.card;

import eashan.pokernea.database.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Calculations {

    public LinkedList<Card> sortHand() {
        cards.sort(Card.CARD_COMPARATOR);
        return cards;
    }

    // WORKS
    public static void giveCards(CardSet<Card> cards, LinkedList<User> users) {
        for (int i = 0; i < 5; i++) { // repeat for 5 cards
            for (User user : users) { // repeat for each user
                user.getCards().add(cards.getLast());
                cards.removeLast();
            }
        }
    }

    /**
     * public static CardType get(int num) {
     *       for (CardType type : values()) {
     *          if (type.getNumber() == num) {
     *             return type;
     *          }
     *       }
     *       return null;
     *    }
     *
     *    Ace as 1, King as 13
     *    Make it so if the number is 14, return 1, otherwise null
     */

    private final LinkedList<Card> cards;

    public Calculations(LinkedList<Card> cards) {
        this.cards = cards;
    }

    /**static LinkedList<Card> create(Card... cards) {
        return new LinkedList<>(List.of(cards));
    }

    public static LinkedList<Card> royalFlush() {
        CardSuit suit = CardSuit.HEART;
        return create(new Card(suit, CardType.TEN),
                new Card(suit, CardType.JACK),
                new Card(suit, CardType.QUEEN),
                new Card(suit, CardType.KING),
                new Card(suit, CardType.ACE));
    }

    public static LinkedList<Card> straightFlush() {
        CardSuit suit = CardSuit.HEART;
        return create(new Card(suit, CardType.FOUR),
                new Card(suit, CardType.FIVE),
                new Card(suit, CardType.SIX),
                new Card(suit, CardType.SEVEN),
                new Card(suit, CardType.EIGHT));
    }

    public static LinkedList<Card> fourOfAKind() {
        return create(new Card(CardSuit.SPADE, CardType.EIGHT),
                new Card(CardSuit.CLOVE, CardType.ACE),
                new Card(CardSuit.HEART, CardType.ACE),
                new Card(CardSuit.SPADE, CardType.ACE),
                new Card(CardSuit.DIAMOND, CardType.ACE));
    }

    public static LinkedList<Card> fullHouse() {
        return create(new Card(CardSuit.CLOVE, CardType.SIX),
                new Card(CardSuit.SPADE, CardType.SIX),
                new Card(CardSuit.DIAMOND, CardType.QUEEN),
                new Card(CardSuit.CLOVE, CardType.QUEEN),
                new Card(CardSuit.HEART, CardType.QUEEN));
    }

    public static LinkedList<Card> flush() {
        return create(new Card(CardSuit.CLOVE, CardType.TWO),
                new Card(CardSuit.CLOVE, CardType.FOUR),
                new Card(CardSuit.CLOVE, CardType.SIX),
                new Card(CardSuit.CLOVE, CardType.SEVEN),
                new Card(CardSuit.CLOVE, CardType.EIGHT));
    }

    public static LinkedList<Card> straight() {
        return create(new Card(CardSuit.SPADE, CardType.THREE),
                new Card(CardSuit.HEART, CardType.FOUR),
                new Card(CardSuit.DIAMOND, CardType.FIVE),
                new Card(CardSuit.CLOVE, CardType.SIX),
                new Card(CardSuit.HEART, CardType.SEVEN));
    }

    public static LinkedList<Card> threeOfAKind() {
        return create(new Card(CardSuit.SPADE, CardType.FIVE),
                new Card(CardSuit.DIAMOND, CardType.EIGHT),
                new Card(CardSuit.HEART, CardType.KING),
                new Card(CardSuit.SPADE, CardType.KING),
                new Card(CardSuit.CLOVE, CardType.KING));
    }

    public static LinkedList<Card> twoPairs() {
        return create(new Card(CardSuit.CLOVE, CardType.THREE),
                new Card(CardSuit.DIAMOND, CardType.FIVE),
                new Card(CardSuit.CLOVE, CardType.FIVE),
                new Card(CardSuit.HEART, CardType.NINE),
                new Card(CardSuit.SPADE, CardType.NINE));
    }

    public static LinkedList<Card> pair() {
        return create(new Card(CardSuit.DIAMOND, CardType.TWO),
                new Card(CardSuit.SPADE, CardType.SIX),
                new Card(CardSuit.CLOVE, CardType.SIX),
                new Card(CardSuit.SPADE, CardType.NINE),
                new Card(CardSuit.HEART, CardType.TEN));
    }

    public static LinkedList<Card> highCard() {
        return create(new Card(CardSuit.SPADE, CardType.THREE),
                new Card(CardSuit.DIAMOND, CardType.FOUR),
                new Card(CardSuit.SPADE, CardType.SIX),
                new Card(CardSuit.HEART, CardType.EIGHT),
                new Card(CardSuit.CLOVE, CardType.KING));
    }**/

    public static int compareHands(int ranking, LinkedList<Card> cards1, LinkedList<Card> cards2) {
        // 0 = draw, 1 = user1, 2 = user2
        List<Integer> ints1 = list(cards1), ints2 = list(cards2); // is in the same order as CardType
        switch (ranking) {
            case 1 -> {
                return 0; // Split pot
            }
            case 2 -> {
                if (cards1.getLast().getOrder() > cards2.getLast().getOrder()) {
                    return 1;
                } else if (cards2.getLast().getOrder() > cards1.getLast().getOrder()) {
                    return 2;
                } else {
                    return 0; // they must have the exact same order but different suits so draw
                }
            }
            case 3 -> {
                if (ints1.indexOf(4) > ints2.indexOf(4)) {
                    return 1;
                } else if (ints2.indexOf(4) > ints1.indexOf(4)) {
                    return 2;
                } else {
                    return 0;
                }
            }
            case 4, 7 -> {
                if (ints1.indexOf(3) > ints2.indexOf(3)) { // Can't have the same three cards
                    return 1;
                } else if (ints2.indexOf(3) > ints1.indexOf(3)) {
                    return 2;
                } else {
                    return 0;
                }
            }
            case 6 -> {
                if (cards1.get(0).getOrder() > cards2.get(0).getOrder()) {
                    return 1;
                } else if (cards2.get(0).getOrder() > cards1.get(0).getOrder()) {
                    return 2;
                } else {
                    return 0;
                }
            }
            case 8 -> {
                if (ints1.lastIndexOf(2) > ints2.lastIndexOf(2)) {
                    return 1;
                } else if (ints2.lastIndexOf(2) > ints1.lastIndexOf(2)) {
                    return 2;
                } else {
                    if (ints1.indexOf(2) > ints2.indexOf(2)) {
                        return 1;
                    } else if (ints2.indexOf(2) > ints1.indexOf(2)) {
                        return 2;
                    } else {
                        if (ints1.indexOf(1) > ints2.indexOf(1)) {
                            return 1;
                        } else if (ints2.indexOf(1) > ints1.indexOf(1)) {
                            return 2;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            case 9 -> {
                if (ints1.indexOf(2) > ints2.indexOf(2)) {
                    return 1;
                } else if (ints2.indexOf(2) > ints1.indexOf(2)) {
                    return 2;
                } else {
                    return 0; // TODO find a method to go through 3 other cards, potential limitation?
                }
            }
            default -> { // 5 and 10
                int index = 0;
                while (true) {
                    try {
                        if (cards1.get(index).getOrder() > cards2.get(index).getOrder()) {
                            return 1;
                        } else if (cards2.get(index).getOrder() > cards1.get(index).getOrder()) {
                            return 2;
                        } else {
                            index++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        return 0; // Means they have the exact same cards in different suit
                    }
                }
            }
        }
    }

    public int calculateHandRanking() {
        if (isRoyalFlush()) return 1;
        else if (isStraightFlush()) return 2;
        else if (isFourOfAKind()) return 3;
        else if (isFullHouse()) return 4;
        else if (isFlush()) return 5;
        else if (isStraight()) return 6;
        else if (isThreeOfAKind()) return 7;
        else if (isTwoPairs()) return 8;
        else if (isPair()) return 9;
        else return 10;
    }

    boolean isRoyalFlush() {
        CardSuit suit = cards.getFirst().suit();
        if (cards.get(0).getType() == CardType.TEN && cards.get(0).getSuit() == suit) {
            if (cards.get(1).getType() == CardType.JACK && cards.get(1).getSuit() == suit) {
                if (cards.get(2).getType() == CardType.QUEEN && cards.get(2).getSuit() == suit) {
                    if (cards.get(3).getType() == CardType.KING && cards.get(3).getSuit() == suit) {
                        return cards.get(4).getType() == CardType.ACE && cards.get(4).getSuit() == suit;
                    }
                }
            }
        }
        return false;
    }

    boolean isStraightFlush() {
        CardSuit suit = cards.get(0).getSuit();
        int num = cards.get(0).getType().getOrder();
        if (cards.get(1).getType() == CardType.get(num+1) && cards.get(1).getSuit() == suit) {
            if (cards.get(2).getType() == CardType.get(num+2) && cards.get(2).getSuit() == suit) {
                if (cards.get(3).getType() == CardType.get(num+3) && cards.get(3).getSuit() == suit) {
                    return cards.get(4).getType() == CardType.get(num+4) && cards.get(4).getSuit() == suit;
                }
            }
        }
        return false;
    }

    boolean isFourOfAKind() {
        List<Integer> ints = list();
        return ints.contains(4);
    }

    boolean isFullHouse() {
        List<Integer> ints = list();
        return ints.contains(3) && ints.contains(2);
    }

    boolean isFlush() {
        boolean ys = true;
        CardSuit suit = cards.get(0).getSuit();
        for (Card card : cards) {
            if (card.getSuit() == suit) continue;
            ys = false;
        }
        return ys;
    }

    boolean isStraight() {
        int num = cards.get(0).getType().getOrder();
        if (cards.get(1).getType() == CardType.get(num+1)) {
            if (cards.get(2).getType() == CardType.get(num+2)) {
                if (cards.get(3).getType() == CardType.get(num+3)) {
                    return cards.get(4).getType() == CardType.get(num+4);
                }
            }
        }
        return false;
    }

    boolean isThreeOfAKind() {
        List<Integer> ints = list();
        return ints.contains(3);
    }

    boolean isTwoPairs() {
        List<Integer> ints = list();
        return Collections.frequency(ints, 2) == 2;
    }

    boolean isPair() {
        List<Integer> ints = list();
        return ints.contains(2);
    }

    private List<Integer> list() {
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int seven = 0;
        int eight = 0;
        int nine = 0;
        int ten = 0;
        int jack = 0;
        int queen = 0;
        int king = 0;
        int ace = 0;
        for (Card card : cards) {
            if (card.getType() == CardType.TWO) two++;
            if (card.getType() == CardType.THREE) three++;
            if (card.getType() == CardType.FOUR) four++;
            if (card.getType() == CardType.FIVE) five++;
            if (card.getType() == CardType.SIX) six++;
            if (card.getType() == CardType.SEVEN) seven++;
            if (card.getType() == CardType.EIGHT) eight++;
            if (card.getType() == CardType.NINE) nine++;
            if (card.getType() == CardType.TEN) ten++;
            if (card.getType() == CardType.JACK) jack++;
            if (card.getType() == CardType.QUEEN) queen++;
            if (card.getType() == CardType.KING) king++;
            if (card.getType() == CardType.ACE) ace++;
        }
        return Arrays.asList(two, three, four, five, six, seven, eight, nine, ten, jack, queen, king, ace);
    }

    private static List<Integer> list(LinkedList<Card> cardList) {
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int seven = 0;
        int eight = 0;
        int nine = 0;
        int ten = 0;
        int jack = 0;
        int queen = 0;
        int king = 0;
        int ace = 0;
        for (Card card : cardList) {
            if (card.getType() == CardType.TWO) two++;
            if (card.getType() == CardType.THREE) three++;
            if (card.getType() == CardType.FOUR) four++;
            if (card.getType() == CardType.FIVE) five++;
            if (card.getType() == CardType.SIX) six++;
            if (card.getType() == CardType.SEVEN) seven++;
            if (card.getType() == CardType.EIGHT) eight++;
            if (card.getType() == CardType.NINE) nine++;
            if (card.getType() == CardType.TEN) ten++;
            if (card.getType() == CardType.JACK) jack++;
            if (card.getType() == CardType.QUEEN) queen++;
            if (card.getType() == CardType.KING) king++;
            if (card.getType() == CardType.ACE) ace++;
        }
        return Arrays.asList(two, three, four, five, six, seven, eight, nine, ten, jack, queen, king, ace);
    }

}
