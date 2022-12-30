package eashan.pokernea.rmi;

import eashan.pokernea.card.Card;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.windows.GameWindow;
import javafx.application.Platform;
import javafx.scene.layout.HBox;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ClientRMIQuery extends UnicastRemoteObject implements ClientRMI {
    public ClientRMIQuery() throws RemoteException {
        super();
    }

    public void showGame(Room room, int order, int ante, int balance) {
        GameWindow window = new GameWindow();
        Platform.runLater(() -> Util.getStage().setScene(window.getScene(room, order, ante, balance)));
    }

    public void showCards(LinkedList<Card> cards) {
        HBox userCards = Util.getUserCards();
        cards.forEach(c -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> userCards.getChildren().add(c.getShape(105, 150)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
