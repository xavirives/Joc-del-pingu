package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla de ganador.
 * Se muestra cuando un jugador llega a la casilla final.
 */
public class PantallaGanador {

    /**
     * Vuelve al menú principal cuando se pulsa el botón de reiniciar.
     */
    @FXML
    private void handleReiniciar(ActionEvent event) {
        try {
            // Carga la pantalla del menú principal.
            Parent root = FXMLLoader.load(getClass().getResource("/resources/PantallaMenu.fxml"));

            // Obtiene la ventana actual desde el botón que se ha pulsado.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Crea una nueva escena con el menú.
            Scene scene = new Scene(root);

            // Cambia la pantalla actual por la pantalla del menú.
            stage.setScene(scene);
            stage.setTitle("El Joc d'en Pingu");
            stage.show();

        } catch (Exception e) {
            // Muestra el error si no se puede cargar la pantalla.
            e.printStackTrace();
        }
    }
}