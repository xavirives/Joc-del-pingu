package modelo;

import java.io.Serializable;

/**
 * Casilla especial de tipo trineo.
 * Permite avanzar hasta el siguiente trineo del tablero.
 */
public class Trineo extends Casilla implements Serializable {
    /**
     * Crea una casilla de trineo en una posición concreta.
     */
    public Trineo(int posicion) { 
        super(posicion); 
    }

    /**
     * Aplica el efecto del trineo al jugador.
     * Si hay otro trineo más adelante, el jugador avanza hasta él.
     */
    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {

        // Busca el siguiente trineo después de la posición actual.
        int siguiente = partida.getTablero().buscarTrineoSiguiente(posicion);

        // Si existe un trineo más adelante, mueve al jugador hasta allí.
        if (siguiente > posicion) {
            jugador.setPosicion(siguiente);
            partida.setUltimoEvento(jugador.getNombre() + " usa un trineo y avanza hasta el siguiente trineo.");

        } else {
            // Si no hay más trineos, el jugador se queda donde está.
            partida.setUltimoEvento(jugador.getNombre() + " cae en el último trineo y no avanza más.");
        }
    }
}