package gofish;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Player {

    private final List<Card> hand;
    private final List<String> books;
    private static int[] recReq = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 

    public Player() {
        this.hand = new ArrayList<>();
        this.books = new ArrayList<>();
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<String> getBooks() {
        return books;
    }

    public void takeCard(Card card) {
        hand.add(card);
        sortHand();
    }

    public boolean hasCard(Card card) {
        for (Card c : hand) {
            if (c.getRank().equals(card.getRank())) {
                return true;    
            }
        }

        return false;   
    }

    public void relinquishCard(Player player, Card card) {
        int index = findCard(card);

        if (index != -1) {
            Card c = hand.remove(index);   
            player.getHand().add(c);       

            sortHand();
            player.sortHand();
        }
    }

    public boolean findAndRemoveBooks() {
        for (int i = 0; i < hand.size() - 1; i++) {
            int frequency = 1;

            for (int j = i + 1; j < hand.size(); j++) {
                if (hand.get(i).getRank().equals(hand.get(j).getRank())) { 
                    frequency++;
                }
            }

            if (frequency == 4) {   
                return removeSets(i);
            }
        }

        return false;
    }

    public Card getCardByNeed() {
        int i1 = 0;
        for (int i = 0; i < recReq.length; i++){
            if(recReq[i] != 0){
                i1++;
            }
        }
        if(i1 == 0){
            int idx = 0;
            int freq = 1;

            for (int k = 0; k < hand.size() - 1; k++) {
                int c = 1;
                for (int j = k + 1; j < hand.size(); j++) {
                    if (hand.get(k).getRank().equals(hand.get(j).getRank())) {
                        c++;
                    }
                }
                if (c > freq) {
                    idx = k;
                    freq = c;
                }
            }
            recReq[Card.getOrderedRank(hand.get(idx).getRank()) - 2] = 3;
            return hand.get(idx);
        }else{
            List<Card> smallHand = new ArrayList<>();

            for(int k = 0; k <= hand.size() - 1; k++){
                if(recReq[Card.getOrderedRank(hand.get(k).getRank()) - 2] == 0){
                    smallHand.add(hand.get(k));
                }
            }
            if(smallHand.size() == 0){
                List<Card> otherHand = new ArrayList<>();
                for(int i = 0; i <= hand.size() - 1; i++){
                    if(GoFish.getRequestedThisTurn()[Card.getOrderedRank(hand.get(i).getRank()) - 2] = false){
                        otherHand.add(hand.get(i));
                    }
                }
                if(otherHand.size() == 0){
                    int idx = 0;
                    int freq = 1;
                    for (int k = 0; k < hand.size() - 1; k++) {
                        int c = 1;
                        for (int j = k + 1; j < hand.size(); j++) {
                            if (hand.get(k).getRank().equals(hand.get(j).getRank())) {
                                c++;
                            }
                        }
                        if (c > freq) {
                            idx = k;
                            freq = c;
                        }
                    }
                    recReq[Card.getOrderedRank(hand.get(idx).getRank()) - 2] = 3;
                    return hand.get(idx);
                }else{
                    int idx = 0;
                    int freq = 1;
                    for (int k = 0; k < otherHand.size() - 1; k++) {
                        int c = 1;
                        for (int j = k + 1; j < otherHand.size(); j++) {
                            if (otherHand.get(k).getRank().equals(otherHand.get(j).getRank())) {
                                c++;
                            }
                        }
                        if (c > freq) {
                            idx = k;
                            freq = c;
                        }
                    }
                    recReq[Card.getOrderedRank(otherHand.get(idx).getRank()) - 2] = 3;
                    return otherHand.get(idx);
                }
            }else{
                int idx = 0;
                int freq = 1;
                for (int k = 0; k < smallHand.size() - 1; k++){
                    int c = 1;
                    for (int j = k + 1; j <smallHand.size(); j++) {
                        if (smallHand.get(k).getRank().equals(smallHand.get(j).getRank())) {
                            c++;
                        }
                    }
                    if (c > freq) {
                        idx = k;
                        freq = c;
                    }
                }
                recReq[Card.getOrderedRank(smallHand.get(idx).getRank()) - 2] = 3;
                return smallHand.get(idx);
            }
        }
    }

    public static void decrementRecentlyRequested(){
        for(int k = 0; k < recReq.length; k++){
            if(recReq[k] != 0){
                recReq[k] -= 1;
            }
        }
    }
    private int findCard(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getRank().equals(card.getRank())) {     
                return i;
            }
        }

        return -1;
    }

    private boolean removeSets(int index) {
        books.add(hand.get(index).getRank());   

        for (int i = 0; i < 4; i++) {
            hand.remove(index);     
        }

        sortHand();
        sortBooks();

        return true;
    }

    private void sortHand() {
        hand.sort((a, b) -> {
            if (Card.getOrderedRank(a.getRank()) == Card.getOrderedRank(b.getRank())) {
                return Card.getOrderedSuit(a.getSuit()) - Card.getOrderedSuit(b.getSuit());    
            }                                                                                  

            return Card.getOrderedRank(a.getRank()) - Card.getOrderedRank(b.getRank());        
        });
    }

    private void sortBooks() {
        books.sort(Comparator.comparingInt(Card::getOrderedRank));  
    }
}