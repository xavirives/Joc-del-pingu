package controlador;
import modelo.Casilla;
import modelo.Partida;
import modelo.Pinguino;

/**
 * Ejecuta efectos del tablero. Gestor que maneja las acciones especiales de cada casilla.
 */
public class GestorTablero {
    /**
     * Ejecuta la acción especial de una casilla cuando un jugador cae en ella.
     * Parámetros: partida = la partida actual, p = el pinguino que cayó, c = la casilla donde cayó
     */
    // Obtiene la casilla y ejecuta su acción (puede ser trampa, bonificación, evento, etc.)
    public void ejecutarCasilla(Partida partida, Pinguino p, Casilla c) {
        // Llama al método realizarAccion de la casilla, que aplica el efecto especial
        // Ejemplo: si es una trampa retrocede, si es bonificación avanza, etc.
        c.realizarAccion(partida, p);
    }
}