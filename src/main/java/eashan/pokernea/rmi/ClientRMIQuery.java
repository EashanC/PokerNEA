package eashan.pokernea.rmi;

import eashan.pokernea.PokerServer;
import eashan.pokernea.card.Card;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.windows.GameWindow;
import eashan.pokernea.windows.WinnerBox;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ClientRMIQuery extends UnicastRemoteObject implements ClientRMI {
    public ClientRMIQuery() throws RemoteException {
        super();
    }

    public void showGame(Room room, int order, int ante, int balance) {
        GameWindow window = new GameWindow();
        Platform.runLater(() -> {
            try {
                Util.getStage().setScene(window.getScene(room, order, ante, balance));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showCards(LinkedList<Card> cards) {
        Platform.runLater(() -> Util.getUserCards().getChildren().clear());
        HBox userCards = Util.getUserCards();
        cards.forEach(c -> Platform.runLater(() -> userCards.getChildren().add(c.getShape(105, 150))));
    }

    public void editLabel(String message) {
        Platform.runLater(() -> Util.getLabel().setText(message));
    }

    // TODO update slider
    public void update(String username) {
        Platform.runLater(() -> {
            try {
                BorderPane pane = (BorderPane) Util.getStage().getScene().getRoot();
                RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");

                // Pot
                VBox centre = (VBox) pane.getCenter();
                Button potButton = (Button) centre.getChildren().get(0);
                potButton.setText("POT: £" + rmi.getPot());

                // Left user
                VBox leftShape = (VBox) pane.getLeft();
                String leftUsername = ((Label) leftShape.getChildren().get(0)).getText();
                Label leftMoney = (Label) leftShape.getChildren().get(1);
                leftMoney.setText("£" + rmi.getBalance(leftUsername));

                // Right user
                VBox rightShape = (VBox) pane.getRight();
                String rightUsername = ((Label) rightShape.getChildren().get(0)).getText();
                Label rightMoney = (Label) rightShape.getChildren().get(1);
                rightMoney.setText("£" + rmi.getBalance(rightUsername));

                // Top user
                VBox topShape = (VBox) pane.getTop();
                String topUsername = ((Label) topShape.getChildren().get(0)).getText();
                Label topMoney = (Label) topShape.getChildren().get(1);
                topMoney.setText("£" + rmi.getBalance(topUsername));

                // Slider
                HBox bottom = (HBox) pane.getBottom();
                VBox buttons = (VBox) bottom.getChildren().get(2);
                Slider slider = (Slider) buttons.getChildren().get(2);
                slider.setMax(rmi.getBalance(Util.getStage().getTitle().substring(7)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendChat(String chat) throws RemoteException {
        Platform.runLater(() -> {
            BorderPane pane = (BorderPane) Util.getStage().getScene().getRoot();
            HBox bottom = (HBox) pane.getBottom();
            VBox chatArea = (VBox) bottom.getChildren().get(0);
            ListView listView = (ListView) chatArea.getChildren().get(0);
            System.out.println(listView.getItems().toString());
            listView.getItems().add(chat);
            System.out.println(listView.getItems().toString());
        });
    }

    public void setGo(boolean isGo) throws RemoteException {
        Util.setGo(isGo);
    }

    public void setHasChecked(boolean hasChecked) throws RemoteException {
        Util.setHasChecked(hasChecked);
    }

    public void displayWinner(String winner) throws RemoteException {
        WinnerBox box = new WinnerBox(winner);
        Platform.runLater(box::display);
    }

    public boolean[] raisedCards() throws RemoteException {
        boolean[] toReturn = new boolean[5];
        int i = 0;
        for (Node node : Util.getUserCards().getChildren()) {
            VBox shape = (VBox) node;
            Rectangle blank = (Rectangle) shape.getChildren().get(1);
            if (blank.getHeight() > 0) {
                toReturn[i] = true;
            } else {
                toReturn[i] = false;
            }
            i++;
        }
        return toReturn;
    }
}
