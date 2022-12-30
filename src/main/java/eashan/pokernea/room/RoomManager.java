package eashan.pokernea.room;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {

   /**
    * Is this class even needed? Only one game can be played at once?
    */

   public @Getter List<Room> rooms;

   public RoomManager() {
      this.rooms = new ArrayList<>();
   }

   public Room getRoom(String code) {
      for (Room room : rooms) {
         if (room.getCode().equalsIgnoreCase(code)) {
            return room;
         }
      }
      return null;
   }

}
