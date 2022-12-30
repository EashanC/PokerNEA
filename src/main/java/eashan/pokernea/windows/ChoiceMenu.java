package eashan.pokernea.windows;

import eashan.pokernea.PokerServer;
import eashan.pokernea.database.User;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ChoiceMenu {

   // make sure you can resize this menu - maybe include also in login? unsure
   
   public Scene getScene() {
      Window window = new Window();

      Button room = window.createButton("");
      if (!Util.isClient()) {
         room.setText("Create room");
         room.setOnAction(e -> {
            Room pokerRoom = Room.createRoom();
            Util.getStage().setScene(new RoomMenu(pokerRoom).getScene());
         });
      } else {
         room.setText("Join room");
         room.setOnAction(e -> {
            Util.getStage().setScene(new JoinRoomMenu().getScene());
         });
      }

      Button viewStats = window.createButton("View statistics");

      Button logOut = window.createLabelButton("Log out");
      logOut.setOnAction(e -> {
         PokerServer.setUser(User.emptyUser());
         Util.getStage().setScene(new LoginMenu().getScene());
         Util.getStage().setTitle("Poker");
         Util.setUser(User.emptyUser());
      });

      return window.create(room, viewStats, logOut);
   }

}
