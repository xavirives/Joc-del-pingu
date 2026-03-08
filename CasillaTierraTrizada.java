package model.casillas;

import model.Juego;
import model.Jugador;
import model.TipoCasilla;

public class CasillaTierraTrizada extends Casilla {

    public CasillaTierraTrizada(int posicion) {
        super(posicion, TipoCasilla.TIERRA_TRIZADA);
    }

    @Override
    public void activar(Jugador jugador, Juego juego) {
        int objetos = jugador.getInventario().getNumeroObjetos();
        if (objetos > 5) {
            jugador.setPosicion(0);
            System.out.println(jugador.getNombre() + " cae por llevar demasiado peso y vuelve al inicio.");
        } else if (objetos > 0) {
            jugador.setTurnoPerdido(true);
            System.out.println(jugador.getNombre() + " pierde un turno por la tierra trizada.");
        } else {
            System.out.println(jugador.getNombre() + " pasa sin penalización.");
        }
    }
}
