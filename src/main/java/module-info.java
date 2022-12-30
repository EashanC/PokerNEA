module eashan.pokernea {
   requires javafx.controls;
   requires javafx.fxml;
   requires java.sql;
   requires static lombok;
   requires org.apache.commons.lang3;
   requires java.rmi;


   opens eashan.pokernea to javafx.fxml;
   exports eashan.pokernea;
   exports eashan.pokernea.rmi;
}