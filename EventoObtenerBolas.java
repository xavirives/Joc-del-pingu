package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;

public class EventoObtenerBolas extends Evento {

    public EventoObtenerBolas() {
        super(TipoEvento.OBTENER_BOLAS);
    }

    @Override
    public void aplicar(Jugador jugador, Juego juego) {
        int cantidad = 1 + (int) (Math.random() * 3);
        jugador.getInventario().addBolas(cantidad);
        System.out.println(jugador.getNombre() + " obtiene " + cantidad + " bolas de nieve.");
    }
}
