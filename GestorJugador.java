package controlador;

import modelo.Juego;
import modelo.Jugador;
import modelo.Tablero;

public class GestorJugador {
    public void moverJugador(Juego juego, Jugador jugador, int pasos) {
        Tablero tablero = juego.getPartida().getTablero();
        int nuevaPosicion = jugador.getPosicion() + pasos;

        if (nuevaPosicion >= tablero.getNumeroCasillas()) {
            nuevaPosicion = tablero.getNumeroCasillas() - 1;
        }

        jugador.setPosicion(nuevaPosicion);
    }
}
