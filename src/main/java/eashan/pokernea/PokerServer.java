package eashan.pokernea;

import eashan.pokernea.database.User;
import eashan.pokernea.rmi.RMI;
import eashan.pokernea.rmi.RMIQuery;
import eashan.pokernea.room.RoomManager;
import eashan.pokernea.util.Database;
import eashan.pokernea.util.Util;
import eashan.pokernea.windows.LoginMenu;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class PokerServer extends Application {

   //private static final @Getter String ipAddress = "192.168.1.108";
   private static final @Getter String ipAddress = "192.168.0.10";

   private static @Getter Database database;
   private static @Getter @Setter User user;
   private static @Getter RoomManager roomManager;

   @Override
   public void start(Stage stage) {
      Util.setStage(stage);
      PokerServer.roomManager = new RoomManager();

      LoginMenu loginMenu = new LoginMenu();
      stage.setScene(loginMenu.getScene());
      //stage.setScene(new GameWindow().getScene(Room.createRoom()));
      stage.setResizable(false);

      stage.setTitle("Poker");
      stage.getIcons().add(new Image("file:icon.png"));
      stage.show();
   }

   public static void main(String[] args) throws RemoteException, MalformedURLException {
      Util.setClient(false);
      database = new Database();
      database.setup();

      System.setProperty("java.rmi.server.hostname", ipAddress);
      RMI rmi = new RMIQuery();
      LocateRegistry.createRegistry(8089);
      Naming.rebind("rmi://" + ipAddress + ":8089/poker", rmi);

      launch();
   }

}