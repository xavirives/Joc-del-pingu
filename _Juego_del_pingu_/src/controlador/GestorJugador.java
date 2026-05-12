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
    // Parámetros: j = jugador a mover, pasos = cuántas casillas se mueve, t = tablero del juego
    public void jugadorSeMueve(Jugador j, int pasos, Tablero t) {
        // Calcula la nueva posición sumando los pasos a la posición actual.
        // Ejemplo: si estaba en casilla 5 y tira 3 dados = 5 + 3 = 8
        int nuevaPos = j.getPosicion() + pasos;
        // Obtiene la última casilla disponible del tablero. Si hay 50 casillas, última = 49 (empieza en 0)
        int ultima = t.getCasillas().size() - 1;
        // Si se pasa de la última casilla, lo deja en la meta.
        // Ejemplo: si nuevaPos = 52 pero la última es 49, se queda en 49
        if (nuevaPos > ultima) nuevaPos = ultima;
        // Si retrocede más allá del inicio, lo deja en la casilla 0.
        // Ejemplo: si tira -5 pasos y está en casilla 2, se quedaría en -3, pero se limita a 0
        if (nuevaPos < 0) nuevaPos = 0;
        // Guarda la nueva posición del jugador en el objeto Jugador
        j.setPosicion(nuevaPos);
    }
}
 