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

    @FXML private void initialize() {
        if (P3 != null) P3.setVisible(false);
        if (P4 != null) P4.setVisible(false);
    }

    @FXML private void handleNewGame() {
        String n1 = "Jugador 1";
        String n2 = "Jugador 2";
        if (gestorPartida != null && gestorPartida.getPartida() != null) {
            n1 = gestorPartida.getPartida().getJugadores().get(0).getNombre();
            n2 = gestorPartida.getPartida().getJugadores().get(1).getNombre();
        }
        gestorPartida = new GestorPartida();
        gestorPartida.nuevaPartida(n1, n2);
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
            String tipo = nombreCorto(casilla);
            Text texto = new Text(i + "\n" + tipo);
            texto.setUserData(TAG_CASILLA_TEXT);
            texto.getStyleClass().add("cell-type");
            GridPane.setRowIndex(texto, i / COLUMNS);
            GridPane.setColumnIndex(texto, i % COLUMNS);
            tablero.getChildren().add(texto);
        }
    }

    private String nombreCorto(Casilla c) {
        if (c instanceof Oso) return "Oso";
        if (c instanceof Agujero) return "Agujero";
        if (c instanceof Trineo) return "Trineo";
        if (c instanceof Evento) return "❓";
        if (c instanceof SueloQuebradizo) return "Suelo Quebradizo";
        return "Normal";
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