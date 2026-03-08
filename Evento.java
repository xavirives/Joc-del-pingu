package model.eventos;

import model.Juego;
import model.Jugador;
import model.TipoEvento;

public abstract class Evento {

    protected TipoEvento tipo;

    public Evento(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public abstract void aplicar(Jugador jugador, Juego juego);

    public TipoEvento getTipo() {
        return tipo;
    }
}
