package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;
import model.dados.DadoLento;

public class EventoDadoLento extends Evento {

    public EventoDadoLento() {
        super(TipoEvento.OBTENER_DADO_LENTO);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        jugador.getInventario().addDado(new DadoLento());
        System.out.println(jugador.getNombre() + " obtiene un dado lento.");
    }
}
