package vista;

import controlador.GestorPartida;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controlador de la pantalla inicial. */
public class PantallaMenu {
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    @FXML private TextField userField;
    @FXML private TextField passField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private void initialize() {
        userField.setText("Jugador 1");
        passField.setText("Jugador 2");
    }

    @FXML private void handleNewGame(ActionEvent event) {
        abrirJuego(event);
    }

    @FXML private void handleSaveGame() {
        System.out.println("La BBDD se implementará más adelante.");
    }

    @FXML private void handleLoadGame(ActionEvent event) {
        abrirJuego(event);
    }

    @FXML private void handleQuitGame() {
        System.exit(0);
    }

    @FXML private void handleLogin(ActionEvent event) {
        abrirJuego(event);
    }

    @FXML private void handleRegister() {
        System.out.println("Registro pendiente de la parte de BBDD.");
    }

    private void abrirJuego(ActionEvent event) {
        try {
            String nombre1 = userField.getText();
            String nombre2 = passField.getText();
            if (nombre1 == null || nombre1.trim().isEmpty()) nombre1 = "Jugador 1";
            if (nombre2 == null || nombre2.trim().isEmpty()) nombre2 = "Jugador 2";

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaJuego.fxml"));
            Parent root = loader.load();

            GestorPartida gestor = new GestorPartida();
            gestor.nuevaPartida(nombre1, nombre2);
            PantallaJuego controller = loader.getController();
            controller.setGestorPartida(gestor);
            controller.refrescarPantalla();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("El Joc d'en Pingu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
