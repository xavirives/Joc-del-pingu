package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;

public class EventoMotoNieve extends Evento {

    public EventoMotoNieve() {
        super(TipoEvento.MOTO_NIEVE);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        juego.moverAlSiguienteTrineo(jugador);
        System.out.println(jugador.getNombre() + " encuentra una moto de nieve.");
    }
}
