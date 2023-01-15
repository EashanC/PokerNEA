package eashan.pokernea.rmi;

import eashan.pokernea.PokerServer;
import eashan.pokernea.database.User;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.SQL;
import eashan.pokernea.util.Util;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class RMIQuery extends UnicastRemoteObject implements RMI {

   public RMIQuery() throws RemoteException {
      super();
   }

   public List<Object> sql(SQL.Level level, String sql, String arg, SQL.Type type) throws RemoteException, SQLException {
      return new SQL().send(level, sql, arg, type);
   }

   public void sql(SQL.Level level ,String sql) throws SQLException, RemoteException {
      sql(level, sql, "", SQL.Type.EMPTY);
   }

   public void sendChat(String chat) throws RemoteException {
      int x=0;
      for (ClientRMI rmi : Util.getGame().rmis) {
         rmi.sendChat(chat);
         System.out.println(x);
         x++;
      }
      Platform.runLater(() -> {
         BorderPane pane = (BorderPane) Util.getStage().getScene().getRoot();
         HBox bottom = (HBox) pane.getBottom();
         VBox chatArea = (VBox) bottom.getChildren().get(0);
         ListView listView = (ListView) chatArea.getChildren().get(0);
         listView.getItems().add(chat);
      });
   }

   public boolean addUser(String roomCode, User user) throws RemoteException {
      for (Room room : PokerServer.getRoomManager().getRooms()) {
         if (room.getCode().equalsIgnoreCase(roomCode)) {
            room.addUser(user);
            return true;
         }
      }
      return false;
   }

   public Room getRoom(String code) throws RemoteException {
      for (Room room : PokerServer.getRoomManager().getRooms()) {
         if (room.getCode().equalsIgnoreCase(code)) {
            return room;
         }
      }
      return null;
   }

   public void leaveRoom(String code, User user) throws RemoteException {
      Room room = getRoom(code);
      room.removeUser(user);
   }

   public int checkUserCount(String text) throws RemoteException {
      return getRoom(text).getUsers().size();
   }

   public int getBalance(String username) throws RemoteException {
      for (User user : Util.getGame().getUsers()) {
         if (user.getUsername().equalsIgnoreCase(username)) {
            return user.getBalance();
         }
      }
      return 0;
   }

   public int getPot() throws RemoteException {
      return Util.getGame().getPot();
   }

}
