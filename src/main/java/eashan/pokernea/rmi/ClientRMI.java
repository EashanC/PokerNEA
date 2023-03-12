package eashan.pokernea.rmi;

import eashan.pokernea.card.Card;
import eashan.pokernea.room.Room;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ClientRMI extends Remote {

    void showGame(Room room, int order, int ante, int balance) throws RemoteException;
    void showCards(LinkedList<Card> cards) throws RemoteException;
    void editLabel(String message) throws RemoteException;
    void update(String username) throws RemoteException;
    void sendChat(String chat) throws RemoteException;
    void setGo(boolean isGo) throws RemoteException;
    void setHasChecked(boolean hasChecked) throws RemoteException;
    void displayWinner(String winner) throws RemoteException;
    boolean[] raisedCards() throws RemoteException;
}
