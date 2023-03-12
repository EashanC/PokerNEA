package eashan.pokernea.rmi;

import eashan.pokernea.database.User;
import eashan.pokernea.room.Move;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.SQL;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface RMI extends Remote {

   //https://stackoverflow.com/questions/24870021/java-why-resultset-is-not-serializable
   // Potentially fixable by returning a List<Object> and iterating over that???

   // Used for execute
   List<Object> sql(SQL.Level level, String sql, String arg, SQL.Type type) throws RemoteException, SQLException;
   // Used for update
   void sql(SQL.Level level, String sql) throws SQLException, RemoteException;
   void sendChat(String chat) throws RemoteException;
   boolean addUser(String roomCode, User user) throws RemoteException;
   Room getRoom(String code) throws RemoteException;
   void leaveRoom(String code, User user) throws RemoteException;
   int checkUserCount(String text) throws RemoteException;
   int getBalance(String username) throws RemoteException;
   int getPot() throws RemoteException;
   void setMove(String username, Move move, int raise) throws RemoteException;
}
