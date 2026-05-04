package vista;

import controlador.GestorPartida;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import modelo.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/** Controlador de la pantalla del juego. */
public class PantallaJuego {
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;
    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;

    private GestorPartida gestorPartida;
    private static final int COLUMNS = 5;
    private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";

    // Cache de imagenes para las celdas
    private Image imgOso;
    private Image imgAgujero;
    private Image imgTrineo;
    private Image imgEvento;
    private Image imgSueloQuebradizo;
    private Image imgNormal;

    @FXML private void initialize() {
        if (P3 != null) P3.setVisible(false);
        if (P4 != null) P4.setVisible(false);

        try {
            imgOso = new Image(getClass().getResourceAsStream("/resources/imagenes/oso.png"));
            imgAgujero = new Image(getClass().getResourceAsStream("/resources/imagenes/agujero.png"));
            imgTrineo = new Image(getClass().getResourceAsStream("/resources/imagenes/trineo.png"));
            imgEvento = new Image(getClass().getResourceAsStream("/resources/imagenes/evento.png"));
            imgSueloQuebradizo = new Image(getClass().getResourceAsStream("/resources/imagenes/suelo_quebradizo.png"));
            imgNormal = new Image(getClass().getResourceAsStream("/resources/imagenes/normal.png"));
        } catch (Exception e) {
            System.err.println("No se pudieron cargar algunas imágenes de las casillas: " + e.getMessage());
        }
    }

    @FXML private void handleNewGame() {
        java.util.List<String> nombres = new java.util.ArrayList<>();
        if (gestorPartida != null && gestorPartida.getPartida() != null) {
            for (Jugador j : gestorPartida.getPartida().getJugadores()) {
                nombres.add(j.getNombre());
            }
        } else {
            nombres.add("Jugador 1");
            nombres.add("Jugador 2");
        }
        gestorPartida = new GestorPartida();
        gestorPartida.nuevaPartida(nombres);
        dadoResultText.setText("Ha salido: -");
        refrescarPantalla();
    }

    @FXML private void handleSaveGame() {
        gestorPartida.guardarPartida();
        refrescarPantalla();
    }

    @FXML private void handleLoadGame() {
        gestorPartida.cargarPartida(1);
        refrescarPantalla();
    }

    @FXML private void handleQuitGame() {
        System.exit(0);
    }

    @FXML private void handleDado() {
        int resultado = gestorPartida.usarDadoNormal();
        if (resultado > 0) dadoResultText.setText("Ha salido: " + resultado);
        refrescarPantalla();
    }

    @FXML private void handleRapido() {
        int resultado = gestorPartida.usarDado("rapido");
        if (resultado > 0) dadoResultText.setText("Rápido: " + resultado);
        refrescarPantalla();
    }

    @FXML private void handleLento() {
        int resultado = gestorPartida.usarDado("lento");
        if (resultado > 0) dadoResultText.setText("Lento: " + resultado);
        refrescarPantalla();
    }

    @FXML private void handlePeces() {
        eventos.setText("Los peces se usan automáticamente cuando aparece un oso.");
    }

    @FXML private void handleNieve() {
        gestorPartida.usarBolaDeNieve();
        refrescarPantalla();
    }

    public void setGestorPartida(GestorPartida gestorPartida) {
        this.gestorPartida = gestorPartida;
    }

    public void refrescarPantalla() {
        if (gestorPartida == null || gestorPartida.getPartida() == null) return;
        Partida partida = gestorPartida.getPartida();
        mostrarTiposDeCasillasEnTablero(partida.getTablero());

        Pinguino j1 = (Pinguino) partida.getJugadores().get(0);
        Pinguino j2 = (Pinguino) partida.getJugadores().get(1);
        colocarFicha(P1, j1.getPosicion());
        colocarFicha(P2, j2.getPosicion());
        P1.setVisible(true);
        P2.setVisible(true);

        if (partida.getJugadores().size() >= 3) {
            Pinguino j3 = (Pinguino) partida.getJugadores().get(2);
            colocarFicha(P3, j3.getPosicion());
            P3.setVisible(true);
        } else {
            P3.setVisible(false);
        }

        if (partida.getJugadores().size() >= 4) {
            Pinguino j4 = (Pinguino) partida.getJugadores().get(3);
            colocarFicha(P4, j4.getPosicion());
            P4.setVisible(true);
        } else {
            P4.setVisible(false);
        }

        Pinguino actual = (Pinguino) partida.getJugadorActual();
        Inventario inv = actual.getInv();
        rapido_t.setText("Dado rápido: " + inv.getCantidad("rapido"));
        lento_t.setText("Dado lento: " + inv.getCantidad("lento"));
        peces_t.setText("Peces: " + inv.getCantidad("pez"));
        nieve_t.setText("Bolas de nieve: " + inv.getCantidad("bola"));

        String texto = partida.getUltimoEvento();
        if (!partida.isFinalizada()) texto += "\nTurno de: " + actual.getNombre();
        eventos.setText(texto);

        boolean fin = partida.isFinalizada();
        if (fin) {
            mostrarPantallaGanador();
            return;
        }
        dado.setDisable(fin);
        rapido.setDisable(fin || inv.getCantidad("rapido") <= 0);
        lento.setDisable(fin || inv.getCantidad("lento") <= 0);
        nieve.setDisable(fin || inv.getCantidad("bola") <= 0);
        peces.setDisable(fin);
    }

    private void mostrarTiposDeCasillasEnTablero(Tablero t) {
        tablero.getChildren().removeIf(node -> TAG_CASILLA_TEXT.equals(node.getUserData()));
        for (int i = 0; i < t.getCasillas().size(); i++) {
            Casilla casilla = t.getCasillas().get(i);
            Image img = obtenerImagenCasilla(casilla);
            
            StackPane cellPane = new StackPane();
            cellPane.setUserData(TAG_CASILLA_TEXT);
            
            if (img != null) {
                ImageView imageView = new ImageView(img);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
                cellPane.getChildren().add(imageView);
            }
            
            Text textoIndice = new Text(String.valueOf(i));
            textoIndice.setStyle("-fx-font-weight: bold; -fx-fill: white; -fx-effect: dropshadow(gaussian, black, 2, 1.0, 0, 0);");
            StackPane.setAlignment(textoIndice, javafx.geometry.Pos.TOP_LEFT);
            cellPane.getChildren().add(textoIndice);

            GridPane.setRowIndex(cellPane, i / COLUMNS);
            GridPane.setColumnIndex(cellPane, i % COLUMNS);
            tablero.getChildren().add(cellPane);
        }
    }

    private Image obtenerImagenCasilla(Casilla c) {
        if (c instanceof Oso) return imgOso;
        if (c instanceof Agujero) return imgAgujero;
        if (c instanceof Trineo) return imgTrineo;
        if (c instanceof Evento) return imgEvento;
        if (c instanceof SueloQuebradizo) return imgSueloQuebradizo;
        return imgNormal;
    }

    private void colocarFicha(Circle ficha, int posicion) {
        GridPane.setRowIndex(ficha, posicion / COLUMNS);
        GridPane.setColumnIndex(ficha, posicion % COLUMNS);
    }
    private boolean pantallaGanadorMostrada = false;
    private void mostrarPantallaGanador() {
        if (pantallaGanadorMostrada) return;

        try {
            pantallaGanadorMostrada = true;

            Parent root = FXMLLoader.load(getClass().getResource("/resources/has ganado.fxml"));
            Stage stage = (Stage) dado.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Has ganado");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}