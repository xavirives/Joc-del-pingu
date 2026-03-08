package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;

public class EventoPerderObjeto extends Evento {

    public EventoPerderObjeto() {
        super(TipoEvento.PERDER_OBJETO);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        jugador.getInventario().perderObjetoAleatorio();
        System.out.println(jugador.getNombre() + " pierde un objeto aleatorio.");
    }
 }
