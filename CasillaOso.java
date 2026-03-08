package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;

public class CasillaOso extends Casilla {

    public CasillaOso(int posicion) {
        super(posicion, TipoCasilla.OSO);
    }

    @Override
    public void activar(Jugador jugador, Juego juego) {
        if (jugador.getInventario().usarPez()) {
            System.out.println(jugador.getNombre() + " usa un pez y evita al oso.");
        } else {
            jugador.setPosicion(0);
            System.out.println(jugador.getNombre() + " vuelve al inicio por culpa del oso.");
        }
    }
}
