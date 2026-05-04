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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/** Controlador de la pantalla inicial. */
public class PantallaMenu {
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private ComboBox<String> numPlayersCombo;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private void initialize() {
        userField.setText("");
        passField.setText("");
        if (numPlayersCombo != null) {
            numPlayersCombo.setValue("2");
        }
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
            String password = passField.getText();
            if (nombre1 == null || nombre1.trim().isEmpty()) nombre1 = "Jugador 1";
            
            int numPlayers = 2;
            if (numPlayersCombo != null && numPlayersCombo.getValue() != null) {
                numPlayers = Integer.parseInt(numPlayersCombo.getValue());
            }

            java.util.List<String> nombres = new java.util.ArrayList<>();
            nombres.add(nombre1);
            for (int i = 2; i <= numPlayers; i++) {
                nombres.add("Jugador " + i);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaJuego.fxml"));
            Parent root = loader.load();

            GestorPartida gestor = new GestorPartida();
            gestor.nuevaPartida(nombres);
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
