package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;

public abstract class Casilla {

    protected int posicion;
    protected TipoCasilla tipoCasilla;

    public Casilla(int posicion, TipoCasilla tipoCasilla) {
        this.posicion = posicion;
        this.tipoCasilla = tipoCasilla;
    }

    public abstract void activar(Jugador jugador, Juego juego);

    public int getPosicion() {
        return posicion;
    }

    public TipoCasilla getTipoCasilla() {
        return tipoCasilla;
    }
}
