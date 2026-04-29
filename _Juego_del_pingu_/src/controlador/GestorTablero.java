package controlador;

import modelo.Casilla;
import modelo.Partida;
import modelo.Pinguino;

/** Ejecuta efectos del tablero. */
public class GestorTablero {
    public void ejecutarCasilla(Partida partida, Pinguino p, Casilla c) {
        c.realizarAccion(partida, p);
    }
}
