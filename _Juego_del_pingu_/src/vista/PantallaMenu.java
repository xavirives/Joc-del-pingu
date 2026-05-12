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

/**
 * Controlador de la pantalla de menú principal del juego.
 * Gestiona la interfaz del menú: iniciar sesión, crear partida, cargar partida, etc.
 * @FXML indica que los elementos están vinculados al archivo FXML (diseño gráfico).
 */
public class PantallaMenu {
    // Opciones del menú (Archivo)
    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    // Campo de texto para el nombre de usuario
    @FXML private TextField userField;
    // Campo de contraseña (no usado actualmente, pero está en la interfaz)
    @FXML private PasswordField passField;
    // ComboBox para seleccionar cuántos jugadores jugarán (2, 3, 4)
    @FXML private ComboBox<String> numPlayersCombo;
    // Botones de login y registro
    @FXML private Button loginButton, registerButton;

    /**
     * Método que se ejecuta automáticamente al cargar la pantalla FXML.
     * Inicializa los componentes gráficos con valores por defecto.
     */
    @FXML 
    private void initialize() {
        // Si el campo de usuario existe, lo deja vacío
        if (userField != null) userField.setText("");
        // Si el ComboBox existe, lo configura
        if (numPlayersCombo != null) {
            // Limpia los valores anteriores
            numPlayersCombo.getItems().clear();
            // Añade las opciones: 2, 3, 4 jugadores
            numPlayersCombo.getItems().addAll("2", "3", "4");
            // Establece "2" como valor por defecto
            numPlayersCombo.setValue("2");
        }
    }

    /**
     * Se ejecuta cuando el usuario hace clic en "Nueva Partida" del menú.
     */
    @FXML 
    private void handleNewGame(ActionEvent event) {
        // Llama al método que abre el juego
        abrirJuego(event);
    }

    /**
     * Se ejecuta cuando el usuario hace clic en el botón "Login".
     */
    @FXML 
    private void handleLogin(ActionEvent event) {
        // Abre el juego (de momento, el login no valida nada)
        abrirJuego(event);
    }

    /**
     * Se ejecuta cuando el usuario hace clic en "Guardar Partida".
     * Actualmente no está implementado en el menú.
     */
    @FXML 
    private void handleSaveGame() {
        System.out.println("Guardado no disponible en el menú.");
    }

    /**
     * Se ejecuta cuando el usuario hace clic en "Cargar Partida".
     * Carga la última partida guardada desde la base de datos.
     */
    @FXML 
    private void handleLoadGame(ActionEvent event) {
        try {
            // Carga el archivo FXML de la pantalla del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            // Crea un gestor de partida y carga la partida guardada desde BD (ID=1)
            GestorPartida gestor = new GestorPartida();
            gestor.cargarPartida(); 
            
            // Si no hay partida guardada, muestra error y sale
            if (gestor.getPartida() == null) {
                System.err.println("No hay ninguna partida guardada en la base de datos.");
                return;
            }
            
            // Obtiene el controlador de la pantalla del juego
            PantallaJuego controller = loader.getController();
            // Le pasa el gestor de partida para que use la partida cargada
            controller.setGestorPartida(gestor);
            // Actualiza la pantalla con los datos de la partida cargada
            controller.refrescarPantalla(); 
            
            // Obtiene la ventana actual (Stage) y la actualiza
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("El Joc d'en Pingu - Partida Cargada");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error al cargar la partida desde el menú.");
            e.printStackTrace();
        }
    }

    /**
     * Se ejecuta cuando el usuario hace clic en "Salir".
     * Cierra la aplicación.
     */
    @FXML 
    private void handleQuitGame() {
        System.exit(0);
    }

    /**
     * Se ejecuta cuando el usuario hace clic en "Registrarse".
     * Actualmente no está implementado.
     */
    @FXML 
    private void handleRegister() {
        System.out.println("Registro pendiente de implementación.");
    }

    /**
     * Método privado que abre una nueva partida.
     * Se usa tanto en "Nueva Partida" como en "Login".
     * Parámetro: event = evento del botón/menú que activó esta función
     */
    private void abrirJuego(ActionEvent event) {
        try {
            // Obtiene el nombre del usuario del campo de texto (o usa "Jugador 1" por defecto)
            String nombrePrincipal = (userField != null && !userField.getText().isEmpty()) 
                                     ? userField.getText() : "Jugador 1";
            
            // Obtiene el número de jugadores seleccionado en el ComboBox (por defecto 2)
            int numPlayers = 2;
            if (numPlayersCombo != null && numPlayersCombo.getValue() != null) {
                numPlayers = Integer.parseInt(numPlayersCombo.getValue());
            }
            
            // Crea una lista de nombres para todos los jugadores
            ArrayList<String> nombres = new ArrayList<>();
            // El primer jugador es el nombre ingresado por el usuario
            nombres.add(nombrePrincipal);
            // Los demás jugadores se llaman "Jugador 2", "Jugador 3", etc.
            for (int i = 2; i <= numPlayers; i++) {
                nombres.add("Jugador " + i);
            }
            
            // Carga el archivo FXML de la pantalla del juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            // Crea un nuevo gestor de partida
            GestorPartida gestor = new GestorPartida();
            // Inicializa una nueva partida con los nombres y la Foca activada
            gestor.nuevaPartida(nombres, true); 
            
            // Obtiene el controlador de la pantalla del juego
            PantallaJuego controller = loader.getController();
            // Le pasa el gestor de partida para que controle el juego
            controller.setGestorPartida(gestor);
            // Actualiza la pantalla con los datos iniciales de la partida
            controller.refrescarPantalla();
            
            // Obtiene la ventana actual (Stage) y la actualiza
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Establece la nueva escena (pantalla del juego)
            stage.setScene(new Scene(root));
            // Cambia el título de la ventana
            stage.setTitle("El Joc d'en Pingu - Partida");
            // Muestra la ventana actualizada
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}