package controlador;

import modelo.Jugador;
import modelo.Tablero;

/**
 * Gestiona el movimiento básico de un jugador en el tablero.
 */
public class GestorJugador {

    /**
     * Mueve al jugador una cantidad de casillas indicada.
     * Controla que no se salga del tablero.
     */
    public void jugadorSeMueve(Jugador j, int pasos, Tablero t) {

        // Calcula la nueva posición sumando los pasos a la posición actual.
        int nuevaPos = j.getPosicion() + pasos;

        // Obtiene la última casilla disponible del tablero.
        int ultima = t.getCasillas().size() - 1;

        // Si se pasa de la última casilla, lo deja en la meta.
        if (nuevaPos > ultima) nuevaPos = ultima;

        // Si retrocede más allá del inicio, lo deja en la casilla 0.
        if (nuevaPos < 0) nuevaPos = 0;

        // Guarda la nueva posición del jugador.
        j.setPosicion(nuevaPos);
    }
}