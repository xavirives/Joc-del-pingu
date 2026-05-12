package controlador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 * Extiende Application para crear una interfaz gráfica de usuario (GUI).
 * Es el verdadero punto de entrada de la aplicación.
 */
public class Main extends Application {
    /**
     * Método start: se ejecuta cuando se inicia la aplicación JavaFX.
     * Carga la interfaz gráfica (FXML) y la muestra en pantalla.
     * Parámetro: primaryStage = ventana principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crea un cargador FXML que lee el archivo de diseño de la pantalla del menú
        // getClass().getResource(...) busca el archivo en la carpeta /resources del proyecto
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/PantallaMenu.fxml"));
        // Carga el archivo FXML y lo convierte en un árbol de componentes (Root = componente principal)
        Parent root = loader.load();
        // Crea una escena (contenedor de componentes) con el root cargado del FXML
        Scene scene = new Scene(root);
        // Asigna la escena a la ventana principal
        primaryStage.setScene(scene);
        // Establece el título que aparecerá en la barra de la ventana
        primaryStage.setTitle("El Joc d'en Pingu");
        // Muestra la ventana en pantalla
        primaryStage.show();
    }

    /**
     * Método main: punto de entrada estándar de Java.
     * Lanza la aplicación JavaFX llamando al método launch().
     * Parámetro: args = argumentos de línea de comandos (se pasan a launch)
     */
    public static void main(String[] args) {
        // launch() inicia la aplicación JavaFX, que automáticamente llama al método start()
        launch(args);
    }
}