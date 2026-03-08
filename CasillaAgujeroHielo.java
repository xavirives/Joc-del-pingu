package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;

public class CasillaAgujeroHielo extends Casilla {

    public CasillaAgujeroHielo(int posicion) {
        super(posicion, TipoCasilla.AGUJERO_HIELO);
    }

    @Override
    public void activar(Jugador jugador, Juego juego) {
        juego.moverAlAnteriorAgujero(jugador);
        System.out.println(jugador.getNombre() + " cae en un agujero y retrocede.");
    }
}
