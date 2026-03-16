package controlador;

import modelo.Casilla;
import modelo.CasillaInterrogante;
import modelo.Evento;
import modelo.Juego;
import modelo.Jugador;
import modelo.Partida;
import modelo.Tablero;

public class GestorTablero {
    public String generarRepresentacion(Partida partida) {
        StringBuilder sb = new StringBuilder();
        Tablero tablero = partida.getTablero();

        for (int i = 0; i < tablero.getNumeroCasillas(); i++) {
            boolean hayJugador = false;
            for (Jugador jugador : partida.getJugadores()) {
                if (jugador.getPosicion() == i) {
                    sb.append("[").append(i).append(":").append(jugador.getNombre().charAt(0)).append("]");
                    hayJugador = true;
                    break;
                }
            }
            if (!hayJugador) {
                sb.append("[").append(i).append(":").append(tablero.getCasilla(i).getIcono()).append("]");
            }
            if ((i + 1) % 10 == 0) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public Evento aplicarEfectoCasilla(Juego juego, Jugador jugador) {
        Casilla casilla = juego.getPartida().getTablero().getCasilla(jugador.getPosicion());
        casilla.alCaer(jugador, juego);
        if (casilla instanceof CasillaInterrogante) {
            return ((CasillaInterrogante) casilla).getUltimoEventoGenerado();
        }
        return null;
    }
}
