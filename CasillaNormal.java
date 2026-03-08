package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;

public class CasillaNormal extends Casilla {

    public CasillaNormal(int posicion) {
        super(posicion, TipoCasilla.NORMAL);
    }

    @Override
    public void activar(Jugador jugador, Juego juego) {
        System.out.println("Casilla normal.");
    }
}
