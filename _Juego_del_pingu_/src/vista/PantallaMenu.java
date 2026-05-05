package vista;

import controlador.GestorPartida;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;


public class PantallaMenu {
    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private ComboBox<String> numPlayersCombo;
    @FXML private Button loginButton, registerButton;

    @FXML private void initialize() {
        if (userField != null) userField.setText("");
        if (numPlayersCombo != null) {
            numPlayersCombo.getItems().clear();
            numPlayersCombo.getItems().addAll("2", "3", "4");
            numPlayersCombo.setValue("2");
        }
    }

    @FXML private void handleNewGame(ActionEvent event) {
        abrirJuego(event);
    }

    @FXML private void handleLogin(ActionEvent event) {
        abrirJuego(event);
    }

    @FXML private void handleSaveGame() {
        System.out.println("Guardado no disponible en el menú.");
    }

 
    @FXML private void handleLoadGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaJuego.fxml"));
            Parent root = loader.load();

            GestorPartida gestor = new GestorPartida();
            gestor.cargarPartida(); 

            if (gestor.getPartida() == null) {
                System.err.println("No hay ninguna partida guardada en la base de datos.");
                return;
            }

            PantallaJuego controller = loader.getController();
            controller.setGestorPartida(gestor);
            controller.refrescarPantalla(); 

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("El Joc d'en Pingu - Partida Cargada");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al cargar la partida desde el menú.");
            e.printStackTrace();
        }
    }

    @FXML private void handleQuitGame() {
        System.exit(0);
    }

    @FXML private void handleRegister() {
        System.out.println("Registro pendiente de implementación.");
    }

    private void abrirJuego(ActionEvent event) {
        try {
            String nombrePrincipal = (userField != null && !userField.getText().isEmpty()) 
                                     ? userField.getText() : "Jugador 1";
            
            int numPlayers = 2;
            if (numPlayersCombo != null && numPlayersCombo.getValue() != null) {
                numPlayers = Integer.parseInt(numPlayersCombo.getValue());
            }

            ArrayList<String> nombres = new ArrayList<>();
            nombres.add(nombrePrincipal);
            for (int i = 2; i <= numPlayers; i++) {
                nombres.add("Jugador " + i);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaJuego.fxml"));
            Parent root = loader.load();

            GestorPartida gestor = new GestorPartida();
            gestor.nuevaPartida(nombres, true); 

            PantallaJuego controller = loader.getController();
            controller.setGestorPartida(gestor);
            controller.refrescarPantalla();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("El Joc d'en Pingu - Partida");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
