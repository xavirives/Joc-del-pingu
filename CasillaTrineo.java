package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;

public class CasillaTrineo extends Casilla {

    public CasillaTrineo(int posicion) {
        super(posicion, TipoCasilla.TRINEO);
    }

    @Override
    public void activar(Jugador jugador, Juego juego) {
        juego.moverAlSiguienteTrineo(jugador);
        System.out.println(jugador.getNombre() + " usa el trineo y avanza.");
    }
}
