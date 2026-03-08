package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;
import model.dados.DadoRapido;

public class EventoDadoRapido extends Evento {

    public EventoDadoRapido() {
        super(TipoEvento.OBTENER_DADO_RAPIDO);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        jugador.getInventario().addDado(new DadoRapido());
        System.out.println(jugador.getNombre() + " obtiene un dado rápido.");
    }
}
