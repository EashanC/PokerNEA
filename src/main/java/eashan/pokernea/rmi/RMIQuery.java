package eashan.pokernea.rmi;

import eashan.pokernea.PokerServer;
import eashan.pokernea.database.User;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.SQL;

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


}
