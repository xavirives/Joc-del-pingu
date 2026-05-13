package modelo;

import java.io.Serializable;
/**
 * Representa una casilla normal del tablero.
 * Esta casilla no aplica ningún efecto especial al jugador.
 */
public class Normal extends Casilla implements Serializable {

    /**
     * Crea una casilla normal en una posición concreta.
     */
    public Normal(int posicion) { 
        super(posicion); 
    }

    /**
     * Acción que se realiza cuando un jugador cae en esta casilla.
     * Solo informa de que el jugador ha caído en una casilla normal.
     */
    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {
        partida.setUltimoEvento(jugador.getNombre() + " cae en una casilla normal.");
    }
}