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
import java.util.ArrayList;

public class PantallaJuego {
    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    @FXML private Button dado, rapido, lento, peces, nieve;
    @FXML private Text dadoResultText, rapido_t, lento_t, peces_t, nieve_t, eventos;
    @FXML private GridPane tablero;
    @FXML private Circle P1, P2, P3, P4, focaFicha;

    private GestorPartida gestorPartida;
    private MediaPlayer mediaPlayer;
    private static final int COLUMNS = 5; 
    private Image imgOso, imgAgujero, imgTrineo, imgEvento, imgSueloQuebradizo, imgNormal;

    @FXML private void initialize() {
        ocultarFichasAlInicio();
        cargarImagenes();
    }

    private void ocultarFichasAlInicio() {
        if (P1 != null) P1.setVisible(false);
        if (P2 != null) P2.setVisible(false);
        if (P3 != null) P3.setVisible(false);
        if (P4 != null) P4.setVisible(false);
        if (focaFicha != null) focaFicha.setVisible(false);
    }

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

    public void setGestorPartida(GestorPartida gestorPartida) {
        this.gestorPartida = gestorPartida;
        iniciarMusica();
        dibujarTableroEstatico();
        refrescarPantalla(); 
    }

    private void iniciarMusica() {
        if (mediaPlayer != null) return;
        try {
            String r = getClass().getResource("/resources/sonidos/musica_fondo.mp3").toExternalForm();
            mediaPlayer = new MediaPlayer(new Media(r));
            mediaPlayer.setVolume(0.05);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception e) { }
    }

    private int getColZigZag(int p) { int f = p / COLUMNS; int c = p % COLUMNS; return (f % 2 != 0) ? (COLUMNS - 1) - c : c; }
    private int getRowZigZag(int p) { return p / COLUMNS; }
    private int calcularIndiceDesdeGrid(int c, int r) { 
        int b = r * COLUMNS; 
        return (r % 2 != 0) ? (b + (COLUMNS - 1 - c)) : (b + c); 
    }
    
    private double getOffsetX(int i) { return (i == 0 || i == 2) ? -15 : 15; }
    private double getOffsetY(int i) { return (i == 0 || i == 1) ? -15 : 15; }

    public void refrescarPantalla() {
        if (gestorPartida == null || gestorPartida.getPartida() == null) return;
        Partida p = gestorPartida.getPartida();
        
        // ⚠️ PASO MAESTRO: Ocultar la foca antes de comprobar si juega
        if (focaFicha != null) focaFicha.setVisible(false);

        for (int i = 0; i < p.getJugadores().size(); i++) {
            Jugador j = p.getJugadores().get(i);
            Circle ficha = (j instanceof Foca) ? focaFicha : obtenerCirculo(i);
            
            if (ficha != null) {
                ficha.setVisible(true); // Solo se hace visible si el jugador está en la lista
                ficha.getStyleClass().remove("current-player");
                if (i == p.getJugadorActualIndice()) ficha.getStyleClass().add("current-player");

                Integer c = GridPane.getColumnIndex(ficha);
                Integer r = GridPane.getRowIndex(ficha);
                int posV = calcularIndiceDesdeGrid(c == null ? 0 : c, r == null ? 0 : r);
                
                if (posV != j.getPosicion()) {
                    animarCamino(ficha, i, posV, j.getPosicion());
                } else {
                    GridPane.setColumnIndex(ficha, getColZigZag(j.getPosicion()));
                    GridPane.setRowIndex(ficha, getRowZigZag(j.getPosicion()));
                    ficha.setTranslateX(getOffsetX(i));
                    ficha.setTranslateY(getOffsetY(i));
                }
            }
        }
        actualizarTextos(p);
    }

    private void animarCamino(Circle f, int idx, int ori, int dest) {
        if (ori == dest) return;
        SequentialTransition seq = new SequentialTransition();
        double w = tablero.getWidth() / COLUMNS;
        double h = tablero.getHeight() / 10.0;
        int paso = (dest > ori) ? 1 : -1;
        int act = ori;

        while (act != dest) {
            int sig = act + paso;
            int c1 = getColZigZag(act); int r1 = getRowZigZag(act);
            int c2 = getColZigZag(sig); int r2 = getRowZigZag(sig);
            TranslateTransition tt = new TranslateTransition(Duration.millis(150), f);
            tt.setByX((c2 - c1) * w); 
            tt.setByY((r2 - r1) * h);
            seq.getChildren().add(tt);
            act = sig;
        }
        seq.setOnFinished(e -> {
            f.setTranslateX(getOffsetX(idx));
            f.setTranslateY(getOffsetY(idx));
            GridPane.setColumnIndex(f, getColZigZag(dest));
            GridPane.setRowIndex(f, getRowZigZag(dest));
        });
        seq.play();
    }

    private void dibujarTableroEstatico() {
        tablero.getChildren().removeIf(n -> n instanceof StackPane);
        Tablero t = gestorPartida.getPartida().getTablero();
        
        for (int i = 0; i < t.getCasillas().size(); i++) {
            StackPane celda = new StackPane();
            celda.getStyleClass().add("stack-pane");
            Image img = obtenerImagenCasilla(t.getCasillas().get(i));
            if (img != null) {
                ImageView iv = new ImageView(img);
                iv.setFitWidth(26); iv.setFitHeight(26);
                celda.getChildren().add(iv);
            }
            Text num = new Text(String.valueOf(i));
            num.getStyleClass().add("cell-title");
            StackPane.setAlignment(num, Pos.TOP_LEFT);
            celda.getChildren().add(num);
            tablero.add(celda, getColZigZag(i), getRowZigZag(i));
        }
        P1.toFront(); P2.toFront(); P3.toFront(); P4.toFront();
        if (focaFicha != null) focaFicha.toFront();
    }

    private void actualizarTextos(Partida p) {
        Jugador j = p.getJugadorActual();
        if (j instanceof Pinguino) {
            Pinguino a = (Pinguino) j;
            rapido_t.setText("Dado rápido: " + a.getInv().getCantidad("rapido"));
            lento_t.setText("Dado lento: " + a.getInv().getCantidad("lento"));
            peces_t.setText("Peces: " + a.getInv().getCantidad("pez"));
            nieve_t.setText("Bolas: " + a.getInv().getCantidad("bola"));
        }
        eventos.setText(p.getUltimoEvento() + "\nTurno de: " + j.getNombre());
    }

    private void desactivarControles(boolean s) {
        dado.setDisable(s);
        Jugador j = gestorPartida.getPartida().getJugadorActual();
        if (j instanceof Pinguino) {
            Pinguino a = (Pinguino) j;
            rapido.setDisable(s || a.getInv().getCantidad("rapido") == 0);
            lento.setDisable(s || a.getInv().getCantidad("lento") == 0);
            nieve.setDisable(s || a.getInv().getCantidad("bola") == 0);
        }
    }

    private Circle obtenerCirculo(int i) {
        if (i == 0) return P1; if (i == 1) return P2;
        if (i == 2) return P3; if (i == 3) return P4;
        return null;
    }

    private Image obtenerImagenCasilla(Casilla c) {
        if (c instanceof Oso) return imgOso; if (c instanceof Agujero) return imgAgujero;
        if (c instanceof Trineo) return imgTrineo; if (c instanceof Evento) return imgEvento;
        if (c instanceof SueloQuebradizo) return imgSueloQuebradizo; return imgNormal;
    }

    @FXML private void handleDado() {
        desactivarControles(true);
        int res = gestorPartida.usarDadoNormal();
        if (res > 0) dadoResultText.setText("¡Lanzamiento: " + res + "!");
        PauseTransition p = new PauseTransition(Duration.millis(500));
        p.setOnFinished(e -> { refrescarPantalla(); desactivarControles(false); });
        p.play();
    }
    @FXML private void handleRapido() { if (gestorPartida.usarDado("rapido") > 0) refrescarPantalla(); }
    @FXML private void handleLento() { if (gestorPartida.usarDado("lento") > 0) refrescarPantalla(); }
    @FXML private void handleNieve() { gestorPartida.usarBolaDeNieve(); refrescarPantalla(); }
    @FXML private void handlePeces() { eventos.setText("Los peces te protegen de osos y focas."); }
    @FXML private void handleQuitGame() { if (mediaPlayer != null) mediaPlayer.stop(); System.exit(0); }
    @FXML private void handleSaveGame() { gestorPartida.guardarPartida(); }
    @FXML private void handleLoadGame() { gestorPartida.cargarPartida(); refrescarPantalla(); }
    
    @FXML private void handleNewGame() {
        // Al darle a "Archivo -> Nuevo", regresamos al menú para elegir si queremos foca
        try {
            if (mediaPlayer != null) mediaPlayer.stop();
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/resources/PantallaMenu.fxml"));
            Stage stage = (Stage) dado.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
