package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;

public class EventoObtenerPez extends Evento {

    public EventoObtenerPez() {
        super(TipoEvento.OBTENER_PEZ);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        jugador.getInventario().addPez();
        System.out.println(jugador.getNombre() + " obtiene un pez.");
    }
 }
