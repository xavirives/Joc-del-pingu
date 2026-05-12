package vista;

import controlador.GestorPartida;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador principal de la pantalla de juego.
 * Gestiona el tablero, fichas, animaciones, eventos y controles del jugador.
 */
public class PantallaJuego {
    // Elementos del menú
    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    // Botones de acciones: dado normal y dados especiales
    @FXML private Button dado, rapido, lento, peces, nieve;
    // Textos que muestran: resultado del dado, inventario e eventos
    @FXML private Text dadoResultText, rapido_t, lento_t, peces_t, nieve_t, eventos;
    // GridPane: contenedor que dibuja el tablero como una cuadrícula
    @FXML private GridPane tablero;
    // Círculos que representan las fichas de los 4 jugadores + la Foca
    @FXML private Circle P1, P2, P3, P4, focaFicha;

    // Gestor que controla la lógica del juego
    private GestorPartida gestorPartida;
    // Reproductor de música de fondo
    private MediaPlayer mediaPlayer;
    // Constante: el tablero tiene 5 columnas (10 filas = 50 casillas)
    private static final int COLUMNS = 5; 
    // Variables para almacenar las imágenes de cada tipo de casilla
    private Image imgOso, imgAgujero, imgTrineo, imgEvento, imgSueloQuebradizo, imgNormal;

    /**
     * Se ejecuta automáticamente al cargar la pantalla FXML.
     * Inicializa componentes gráficos.
     */
    @FXML 
    private void initialize() {
        // Oculta todas las fichas al inicio (se mostrarán cuando se actualice la pantalla)
        ocultarFichasAlInicio();
        // Carga todas las imágenes de casillas desde archivos
        cargarImagenes();
    }

    /**
     * Oculta todas las fichas de jugadores al inicio.
     */
    private void ocultarFichasAlInicio() {
        if (P1 != null) P1.setVisible(false);
        if (P2 != null) P2.setVisible(false);
        if (P3 != null) P3.setVisible(false);
        if (P4 != null) P4.setVisible(false);
        if (focaFicha != null) focaFicha.setVisible(false);
    }

    /**
     * Carga todas las imágenes desde la carpeta /resources/imagenes.
     * Si falla, captura el error pero no detiene el juego.
     */
    private void cargarImagenes() {
        try {
            imgOso = new Image(getClass().getResourceAsStream("/resources/imagenes/oso.png"));
            imgAgujero = new Image(getClass().getResourceAsStream("/resources/imagenes/agujero.png"));
            imgTrineo = new Image(getClass().getResourceAsStream("/resources/imagenes/trineo.png"));
            imgEvento = new Image(getClass().getResourceAsStream("/resources/imagenes/evento.png"));
            imgSueloQuebradizo = new Image(getClass().getResourceAsStream("/resources/imagenes/suelo_quebradizo.png"));
            imgNormal = new Image(getClass().getResourceAsStream("/resources/imagenes/normal.png"));
        } catch (Exception e) { System.err.println("Recursos gráficos no encontrados."); }
    }

    /**
     * Establece el gestor de partida y inicia el juego.
     * Parámetro: gestorPartida = el controlador de la lógica del juego
     */
    public void setGestorPartida(GestorPartida gestorPartida) {
        this.gestorPartida = gestorPartida;
        // Inicia la música de fondo del juego
        iniciarMusica();
        // Dibuja el tablero estático (casillas)
        dibujarTableroEstatico();
        // Actualiza la pantalla con posiciones iniciales de fichas y textos
        refrescarPantalla(); 
    }

    /**
     * Inicia la música de fondo del juego.
     * Se reproduce en bucle infinito con volumen bajo.
     */
    private void iniciarMusica() {
        // Si ya está reproduciendo, no hacer nada
        if (mediaPlayer != null) return;
        try {
            // Obtiene la ruta del archivo de música
            String r = getClass().getResource("/resources/sonidos/musica_fondo.mp3").toExternalForm();
            // Crea un reproductor de música
            mediaPlayer = new MediaPlayer(new Media(r));
            // Establece el volumen (0.05 = 5%)
            mediaPlayer.setVolume(0.05);
            // Hace que la música se repita infinitamente
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            // Inicia la reproducción
            mediaPlayer.play();
        } catch (Exception e) { }
    }

    // ====== FUNCIONES PARA TABLERO EN ZIG-ZAG ======
    // El tablero se dibuja en forma de serpiente: fila 0 izq->der, fila 1 der->izq, etc.
    // Estas funciones convierten entre coordenadas de grid y números de casilla.
    
    /**
     * Obtiene la columna en el grid para una posición de casilla.
     * Ajusta para el patrón en zigzag.
     */
    private int getColZigZag(int p) { 
        int f = p / COLUMNS;           // Fila = posición / 5
        int c = p % COLUMNS;           // Columna = posición mod 5
        // Si la fila es impar, invierte la columna (para el zigzag)
        return (f % 2 != 0) ? (COLUMNS - 1) - c : c; 
    }

    /**
     * Obtiene la fila en el grid para una posición de casilla.
     */
    private int getRowZigZag(int p) { 
        return p / COLUMNS; 
    }

    /**
     * Convierte coordenadas de grid (columna, fila) a número de casilla.
     */
    private int calcularIndiceDesdeGrid(int c, int r) { 
        int b = r * COLUMNS; 
        // Si la fila es impar, invierte la columna
        return (r % 2 != 0) ? (b + (COLUMNS - 1 - c)) : (b + c); 
    }

    // ====== FUNCIONES PARA SEPARAR FICHAS EN LA MISMA CASILLA ======
    // Cuando 2+ fichas están en la misma casilla, se separan visualmente con offset.
    
    /**
     * Obtiene el desplazamiento X (horizontal) para una ficha según su índice.
     * Así se ven separadas cuando comparten casilla.
     */
    private double getOffsetX(int i) { 
        return (i == 0 || i == 2) ? -15 : 15; 
    }

    /**
     * Obtiene el desplazamiento Y (vertical) para una ficha según su índice.
     */
    private double getOffsetY(int i) { 
        return (i == 0 || i == 1) ? -15 : 15; 
    }

    /**
     * Actualiza la pantalla con las posiciones actuales de todas las fichas.
     * Se llama después de cada movimiento.
     */
    public void refrescarPantalla() {
        // Validaciones iniciales
        if (gestorPartida == null || gestorPartida.getPartida() == null) return;
        Partida p = gestorPartida.getPartida();
        
        // Oculta la ficha de la Foca al inicio
        if (focaFicha != null) focaFicha.setVisible(false);

        // Recorre todos los jugadores y actualiza sus fichas
        for (int i = 0; i < p.getJugadores().size(); i++) {
            Jugador j = p.getJugadores().get(i);
            // Obtiene la ficha correspondiente (Foca o Pinguino)
            Circle ficha = (j instanceof Foca) ? focaFicha : obtenerCirculo(i);
            
            if (ficha != null) {
                // Muestra la ficha
                ficha.setVisible(true);
                // Quita el estilo "jugador actual" de la ficha
                ficha.getStyleClass().remove("current-player");
                // Si es el jugador actual, añade el estilo "jugador actual" (brilla)
                if (i == p.getJugadorActualIndice()) ficha.getStyleClass().add("current-player");

                // Obtiene la posición actual de la ficha en el grid
                Integer c = GridPane.getColumnIndex(ficha);
                Integer r = GridPane.getRowIndex(ficha);
                // Convierte la posición del grid a número de casilla
                int posV = calcularIndiceDesdeGrid(c == null ? 0 : c, r == null ? 0 : r);
                
                // Si la posición del grid no coincide con la posición real del jugador...
                if (posV != j.getPosicion()) {
                    // Anima el movimiento desde la posición actual hasta la nueva
                    animarCamino(ficha, i, posV, j.getPosicion());
                } else {
                    // Si ya está en la posición correcta, solo ajusta el offset visual
                    GridPane.setColumnIndex(ficha, getColZigZag(j.getPosicion()));
                    GridPane.setRowIndex(ficha, getRowZigZag(j.getPosicion()));
                    ficha.setTranslateX(getOffsetX(i));
                    ficha.setTranslateY(getOffsetY(i));
                }
            }
        }
        // Actualiza los textos (inventario, eventos, turno)
        actualizarTextos(p);
    }

    /**
     * Anima el movimiento de una ficha de una casilla a otra.
     * Parámetros: f = ficha, idx = índice del jugador, ori = posición origen, dest = posición destino
     */
    private void animarCamino(Circle f, int idx, int ori, int dest) {
        // Si origen = destino, no animar
        if (ori == dest) return;
        
        // Crea una secuencia de animaciones (una para cada casilla que avanza)
        SequentialTransition seq = new SequentialTransition();
        // Calcula el ancho y alto de cada casilla del grid
        double w = tablero.getWidth() / COLUMNS;
        double h = tablero.getHeight() / 10.0;
        // Dirección: 1 = adelante, -1 = atrás
        int paso = (dest > ori) ? 1 : -1;
        int act = ori;

        // Crea una animación para cada casilla del movimiento
        while (act != dest) {
            int sig = act + paso;
            // Coordenadas de grid actual y siguiente
            int c1 = getColZigZag(act); int r1 = getRowZigZag(act);
            int c2 = getColZigZag(sig); int r2 = getRowZigZag(sig);
            // Crea una transición de 150ms entre casillas
            TranslateTransition tt = new TranslateTransition(Duration.millis(150), f);
            tt.setByX((c2 - c1) * w);  // Desplazamiento X
            tt.setByY((r2 - r1) * h);  // Desplazamiento Y
            seq.getChildren().add(tt);
            act = sig;
        }
        
        // Cuando termina la animación, ajusta la posición final
        seq.setOnFinished(e -> {
            f.setTranslateX(getOffsetX(idx));
            f.setTranslateY(getOffsetY(idx));
            GridPane.setColumnIndex(f, getColZigZag(dest));
            GridPane.setRowIndex(f, getRowZigZag(dest));
        });
        // Inicia la secuencia de animaciones
        seq.play();
    }

    /**
     * Dibuja el tablero estático (las 50 casillas con sus imágenes).
     * Se llama una sola vez al inicio.
     */
    private void dibujarTableroEstatico() {
        // Limpia las casillas anteriores (mantiene solo las fichas)
        tablero.getChildren().removeIf(n -> n instanceof StackPane);
        Tablero t = gestorPartida.getPartida().getTablero();
        
        // Dibuja cada casilla
        for (int i = 0; i < t.getCasillas().size(); i++) {
            // Crea un StackPane para la casilla (puede contener imagen + número)
            StackPane celda = new StackPane();
            celda.getStyleClass().add("stack-pane");
            
            // Obtiene la imagen correspondiente a este tipo de casilla
            Image img = obtenerImagenCasilla(t.getCasillas().get(i));
            if (img != null) {
                ImageView iv = new ImageView(img);
                // Redimensiona la imagen a 26x26 píxeles
                iv.setFitWidth(26); iv.setFitHeight(26);
                celda.getChildren().add(iv);
            }
            
            // Añade el número de casilla en la esquina superior izquierda
            Text num = new Text(String.valueOf(i));
            num.getStyleClass().add("cell-title");
            StackPane.setAlignment(num, Pos.TOP_LEFT);
            celda.getChildren().add(num);
            
            // Añade la celda al grid en su posición
            tablero.add(celda, getColZigZag(i), getRowZigZag(i));
        }
        
        // Pone las fichas delante de las casillas (en primer plano)
        P1.toFront(); P2.toFront(); P3.toFront(); P4.toFront();
        if (focaFicha != null) focaFicha.toFront();
    }

    /**
     * Actualiza los textos de inventario y eventos.
     * Parámetro: p = la partida actual
     */
    private void actualizarTextos(Partida p) {
        Jugador j = p.getJugadorActual();
        // Si el jugador actual es un Pinguino (no la Foca)
        if (j instanceof Pinguino) {
            Pinguino a = (Pinguino) j;
            // Actualiza los textos con las cantidades del inventario
            rapido_t.setText("Dado rápido: " + a.getInv().getCantidad("rapido"));
            lento_t.setText("Dado lento: " + a.getInv().getCantidad("lento"));
            peces_t.setText("Peces: " + a.getInv().getCantidad("pez"));
            nieve_t.setText("Bolas: " + a.getInv().getCantidad("bola"));
        }
        // Actualiza el texto de eventos y turno
        eventos.setText(p.getUltimoEvento() + "\nTurno de: " + j.getNombre());
    }

    /**
     * Desactiva/activa los botones de controles del jugador.
     * Parámetro: s = true para desactivar, false para activar
     */
    private void desactivarControles(boolean s) {
        dado.setDisable(s);
        Jugador j = gestorPartida.getPartida().getJugadorActual();
        // Si es un Pinguino, desactiva los dados especiales si no tiene
        if (j instanceof Pinguino) {
            Pinguino a = (Pinguino) j;
            // Desactiva si no tiene dado rápido o si s es true
            rapido.setDisable(s || a.getInv().getCantidad("rapido") == 0);
            lento.setDisable(s || a.getInv().getCantidad("lento") == 0);
            nieve.setDisable(s || a.getInv().getCantidad("bola") == 0);
        }
    }

    /**
     * Devuelve la ficha (Circle) correspondiente a un índice de jugador.
     */
    private Circle obtenerCirculo(int i) {
        if (i == 0) return P1; if (i == 1) return P2;
        if (i == 2) return P3; if (i == 3) return P4;
        return null;
    }

    /**
     * Devuelve la imagen correspondiente a un tipo de casilla.
     */
    private Image obtenerImagenCasilla(Casilla c) {
        if (c instanceof Oso) return imgOso; 
        if (c instanceof Agujero) return imgAgujero;
        if (c instanceof Trineo) return imgTrineo; 
        if (c instanceof Evento) return imgEvento;
        if (c instanceof SueloQuebradizo) return imgSueloQuebradizo; 
        return imgNormal;
    }

    /**
     * Se ejecuta cuando el jugador hace clic en "Lanzar Dado Normal".
     */
    @FXML 
    private void handleDado() {
        // Si la partida ya terminó, no hacer nada
        if (gestorPartida.getPartida().isFinalizada()) return; 

        // Desactiva los controles mientras se resuelve el turno
        desactivarControles(true);
        // Lanza el dado y obtiene el resultado
        int res = gestorPartida.usarDadoNormal();
        
        if (res > 0) {
            // Muestra el resultado en la pantalla
            dadoResultText.setText("¡Lanzamiento: " + res + "!");
            
            // Actualiza la pantalla con la nueva posición
            refrescarPantalla(); 

            // Espera 1 segundo antes de verificar si alguien ganó
            PauseTransition delayVictoria = new PauseTransition(Duration.millis(1000));
            delayVictoria.setOnFinished(e -> {
                if (gestorPartida.getPartida().isFinalizada()) {
                    // Si alguien ganó, carga la pantalla de victoria
                    cargarPantallaVictoria();
                } else {
                    // Si no, reactiva los controles para el siguiente turno
                    desactivarControles(false);
                }
            });
            delayVictoria.play();
        }
    }

    /**
     * Verifica si la partida ha terminado y carga pantalla de victoria si es necesario.
     */
    private void verificarVictoria() {
        if (gestorPartida.getPartida().isFinalizada()) {
            // Espera 800ms antes de mostrar la pantalla de victoria
            PauseTransition delay = new PauseTransition(Duration.millis(800));
            delay.setOnFinished(event -> cargarPantallaVictoria());
            delay.play();
        } else {
            // Si no terminó, reactiva los controles
            desactivarControles(false);
        }
    }

    /**
     * Carga la pantalla de victoria cuando alguien gana.
     */
    private void cargarPantallaVictoria() {
        try {
            // Detiene la música de fondo
            if (mediaPlayer != null) mediaPlayer.stop();
            
            // Carga la pantalla de victoria (has ganado.fxml)
            Parent root = FXMLLoader.load(getClass().getResource("/resources/has ganado.fxml"));
            // Obtiene la ventana actual
            Stage stage = (Stage) dado.getScene().getWindow();
            // Cambia a la pantalla de victoria
            stage.setScene(new Scene(root));
            stage.setTitle("¡Fin de la partida!");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar has ganado.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Se ejecuta al hacer clic en "Dado Rápido".
     */
    @FXML 
    private void handleRapido() { 
        // Si el lanzamiento fue exitoso, actualiza pantalla
        if (gestorPartida.usarDado("rapido") > 0) {
            refrescarPantalla();
            verificarVictoria();
        }
    }

    /**
     * Se ejecuta al hacer clic en "Dado Lento".
     */
    @FXML 
    private void handleLento() { 
        // Si el lanzamiento fue exitoso, actualiza pantalla
        if (gestorPartida.usarDado("lento") > 0) {
            refrescarPantalla();
            verificarVictoria();
        }
    }

    /**
     * Se ejecuta al hacer clic en "Lanzar Bola de Nieve".
     */
    @FXML 
    private void handleNieve() { 
        gestorPartida.usarBolaDeNieve(); 
        refrescarPantalla(); 
    }

    /**
     * Se ejecuta al hacer clic en "Ver Peces".
     */
    @FXML 
    private void handlePeces() { 
        eventos.setText("Los peces te protegen de osos y focas."); 
    }

    /**
     * Se ejecuta al hacer clic en "Salir".
     */
    @FXML 
    private void handleQuitGame() { 
        if (mediaPlayer != null) mediaPlayer.stop(); 
        System.exit(0); 
    }

    /**
     * Se ejecuta al hacer clic en "Guardar Partida".
     */
    @FXML 
    private void handleSaveGame() { 
        gestorPartida.guardarPartida(); 
    }

    /**
     * Se ejecuta al hacer clic en "Cargar Partida".
     */
    @FXML 
    private void handleLoadGame() { 
        gestorPartida.cargarPartida(); 
        refrescarPantalla(); 
    }
    
    /**
     * Se ejecuta al hacer clic en "Nueva Partida".
     */
    @FXML 
    private void handleNewGame() {
        try {
            // Detiene la música actual
            if (mediaPlayer != null) mediaPlayer.stop();
            // Carga la pantalla del menú
            Parent root = FXMLLoader.load(getClass().getResource("/resources/PantallaMenu.fxml"));
            // Obtiene la ventana actual
            Stage stage = (Stage) dado.getScene().getWindow();
            // Cambia a la pantalla del menú
            stage.setScene(new Scene(root));
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}
