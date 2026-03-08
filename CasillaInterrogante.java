package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;
import model.eventos.Evento;

public class CasillaInterrogante extends Casilla {

    public CasillaInterrogante(int posicion) {
        super(posicion, TipoCasilla.INTERROGANTE);
    }

    @Override
    public void activar(Jugador jugador, Juego juego) {
        Evento evento = juego.getGestorEventos().generarAleatorio();
        System.out.println("Evento: " + evento.getTipo());
        evento.aplicar(jugador, juego);
    }
}
