package eashan.pokernea.util;

import eashan.pokernea.card.Calculations;
import eashan.pokernea.database.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SQLManager {

   public static void setupGame(String code, int players) {
      LocalDateTime date = LocalDateTime.now();
      SQL.send(SQL.Level.UPDATE, "INSERT INTO game (code, date, numberPlayers) VALUES ('" + code + "', " + date + ", " + players + ");");
   }

   public static void addPlayerInfo(String code, List<User> users, int startingBalance, String winnerName) throws SQLException {
      ResultSet resultSet = SQL.send(SQL.Level.EXECUTE, "SELECT gameID FROM game WHERE code = '" + code + "';");
      int gameID = resultSet.getInt("gameID");
      for (User user : users) {
         boolean winner = user.getUsername().equals(winnerName);
         int moneyBet = startingBalance - user.getBalance();
         boolean hadRoyalFlush = new Calculations(user.getCards()).calculateHandRanking() == 1;
         ResultSet resultSet1 = SQL.send(SQL.Level.EXECUTE, "SELECT userID FROM login WHERE username = '" + user.getUsername() + "';");
         int userID = resultSet1.getInt("userID");
         SQL.send(SQL.Level.UPDATE, "INSERT INTO userStats VALUES (" + gameID + ", " + userID + ", " + moneyBet + ", " + winner + ", " + hadRoyalFlush + ");");
      }
   }

}
