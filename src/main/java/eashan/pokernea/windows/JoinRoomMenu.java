package eashan.pokernea.windows;

import eashan.pokernea.PokerServer;
import eashan.pokernea.rmi.RMI;
import eashan.pokernea.room.Room;
import eashan.pokernea.util.Util;
import eashan.pokernea.util.Window;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.Naming;

public class JoinRoomMenu {

   public Scene getScene() {
      Window window = new Window();

      Label roomCodeLabel = window.createLabel("Room code:");
      TextField roomCode = window.createTextField();
      Button joinRoom = window.createButton("Join Room");
      joinRoom.setDefaultButton(true);
      joinRoom.setOnAction(f -> {
         if (Util.isClient()) {
            try {
               RMI rmi = (RMI) Naming.lookup("rmi://" + PokerServer.getIpAddress() + ":8089/poker");
               int roomCount = rmi.checkUserCount(roomCode.getText());
               if (roomCount >= 4) {
                  AlertBox box = new AlertBox("Room is full!");
                  box.display();
               } else {
                  boolean addedToRoom = rmi.addUser(roomCode.getText(), Util.getUser());
                  if (addedToRoom) {
                     Room room = rmi.getRoom(roomCode.getText());
                     Util.getStage().setScene(new RoomMenu(room).getScene());
                  } else {
                     AlertBox box = new AlertBox("Room doesn't exist!");
                     box.display();
                  }
               }
            } catch (NullPointerException e) {
               AlertBox box = new AlertBox("Room doesn't exist!");
               box.display();
            } catch (Exception e) {
               e.printStackTrace();
            }
         } else {
            for (Room room : PokerServer.getRoomManager().getRooms()) {
               if (room.getCode().equalsIgnoreCase(roomCode.getText())) {
                  room.addUser(Util.getUser());
               }
            }
         }
      });
      Button goBack = window.createLabelButton("Go back");
      goBack.setOnAction(e -> Util.getStage().setScene(new ChoiceMenu().getScene()));

      return window.create(roomCodeLabel, roomCode, joinRoom, goBack);
   }

}
