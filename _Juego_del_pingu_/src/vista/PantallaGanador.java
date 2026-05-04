package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PantallaGanador {

    @FXML
    private void handleReiniciar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/PantallaMenu.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("El Joc d'en Pingu");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}