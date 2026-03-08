package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;

public class EventoPerderTurno extends Evento {

    public EventoPerderTurno() {
        super(TipoEvento.PERDER_TURNO);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        jugador.setTurnoPerdido(true);
        System.out.println(jugador.getNombre() + " perderá el siguiente turno.");
    }
}
