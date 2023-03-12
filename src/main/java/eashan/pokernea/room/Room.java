package eashan.pokernea.room;

import eashan.pokernea.PokerServer;
import eashan.pokernea.database.User;
import eashan.pokernea.util.Util;
import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedList;

public class Room implements Serializable {

   public static Room createRoom() {
      Room room = new Room(Util.generateRoomCode().toUpperCase());
      PokerServer.getRoomManager().getRooms().add(room);
      room.addUser(Util.getUser());
      return room;
   }

   private final @Getter String code;
   private final @Getter LinkedList<User> users;
   public Room(String code) {
      this.code = code;
      this.users = new LinkedList<>();
   }

   private void setupSQL() {

   }

   public void addUser(User user) {
      users.add(user);
   }

   public void removeUser(User user) {
      for (User u : users) {
         if (u.getUsername().equals(user.getUsername())) {
            users.remove(u);
         }
      }
   }

   public void addUser(String ipAddress, String username) {
      users.add(new User(ipAddress, username));
   }

}
