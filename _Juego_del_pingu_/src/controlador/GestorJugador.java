package controlador;

import modelo.Jugador;
import modelo.Tablero;

/** Gestor sencillo de movimientos de jugadores. */
public class GestorJugador {
    public void jugadorSeMueve(Jugador j, int pasos, Tablero t) {
        int nuevaPos = j.getPosicion() + pasos;
        int ultima = t.getCasillas().size() - 1;
        if (nuevaPos > ultima) nuevaPos = ultima;
        if (nuevaPos < 0) nuevaPos = 0;
        j.setPosicion(nuevaPos);
    }
}
