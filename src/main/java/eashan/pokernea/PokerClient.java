package eashan.pokernea;

import eashan.pokernea.database.User;
import eashan.pokernea.rmi.ClientRMI;
import eashan.pokernea.rmi.ClientRMIQuery;
import eashan.pokernea.util.Util;
import eashan.pokernea.windows.LoginMenu;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class PokerClient extends Application {

   /**
    * Create two programs: one for creating a room and a database (this one) and another for just joining.
    */

   @Getter @Setter
   private User user;

   @Override
   public void start(Stage stage) throws IOException {
      Util.setStage(stage);

      LoginMenu loginMenu = new LoginMenu();
      stage.setScene(loginMenu.getScene());
      stage.setResizable(false);

      stage.setTitle("Poker");
      stage.getIcons().add(new Image("file:icon.png"));
      stage.show();
   }

   public static void main(String[] args) throws MalformedURLException, RemoteException {
      Util.setClient(true);

      System.setProperty("java.rmi.server.hostname", Util.getIpAddress());
      ClientRMI rmi = new ClientRMIQuery();
      LocateRegistry.createRegistry(8090);
      Naming.rebind("rmi://" + Util.getIpAddress() + ":8090/player", rmi);

      launch();
   }
}