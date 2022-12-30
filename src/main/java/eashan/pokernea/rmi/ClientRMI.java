package eashan.pokernea.rmi;

import eashan.pokernea.card.Card;
import eashan.pokernea.room.Room;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ClientRMI extends Remote {

    void showGame(Room room, int order, int ante, int balance) throws RemoteException;
    void showCards(LinkedList<Card> cards) throws RemoteException;
}
