package eashan.pokernea.util;

import java.sql.*;

public class Database {

   private Connection connection;
   private Statement statement;
   private ResultSet resultSet;

   private void connect(String url) {
      try {
         Class.forName("org.sqlite.JDBC");
         this.connection = DriverManager.getConnection(url);
         if (this.connection != null) {
            System.out.println("Connected to the database");
            DatabaseMetaData metaData = (DatabaseMetaData) this.connection.getMetaData();
            System.out.println("Driver name: " + metaData.getDriverName());
            System.out.println("Driver version: " + metaData.getDriverVersion());
            System.out.println("Product name: " + metaData.getDatabaseProductName());
            System.out.println("Product version: " + metaData.getDatabaseProductVersion());
         }
      } catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
      }
   }

   /* https://www.sqlitetutorial.net/sqlite-date/ */
   public void setup() {
      String url = "jdbc:sqlite:Database.db";
      connect(url);
      update("CREATE TABLE IF NOT EXISTS login (userID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
      update("CREATE TABLE IF NOT EXISTS game (gameID INTEGER PRIMARY KEY AUTOINCREMENT, code TEXT, date TEXT, numberPlayers INTEGER)");
      update("CREATE TABLE IF NOT EXISTS userStats (userID INTEGER NOT NULL, gameID INTEGER NOT NULL, moneyBet INTEGER, won BOOLEAN, hadRoyalFlush BOOLEAN, PRIMARY KEY (userID, gameID))");
   }

   public void update(String sql) {
      try {
         statement = connection.createStatement();
         statement.execute(sql);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public ResultSet execute(String sql) {
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery(sql);
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return resultSet;
   }

   public void close() {
      try {
         resultSet.close();
         statement.close();
         connection.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

}
