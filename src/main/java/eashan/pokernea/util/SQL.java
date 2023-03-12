package eashan.pokernea.util;

import eashan.pokernea.PokerServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQL {

   public enum Type {
      STRING,
      INT,
      EMPTY;
   }

   public enum Level {
      EXECUTE,
      UPDATE;
   }

   public static ResultSet send(Level level, String sql) {
      if (level == Level.EXECUTE) {
         return PokerServer.getDatabase().execute(sql);
      } else if (level == Level.UPDATE) {
         PokerServer.getDatabase().update(sql);
         return PokerServer.getDatabase().execute("SELECT 1 WHERE false");
      }
      return null;
   }

   public static List<Object> send(Level level, String sql, String arg, Type type) throws SQLException {
      if (level == Level.EXECUTE) {

         List<Object> list = new ArrayList<>();
         ResultSet rs = PokerServer.getDatabase().execute(sql);
         if (type == Type.STRING) {
            while (rs.next()) {
               list.add(rs.getString(arg));
            }
         }
         return list;

      } else if (level == Level.UPDATE) {
         PokerServer.getDatabase().update(sql);
         // dummy statement to return
         return Collections.emptyList();
      }
      return null;
   }

}
