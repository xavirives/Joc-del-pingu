package modelo;

import java.io.Serializable;


public class SueloQuebradizo extends Casilla implements Serializable{
    
    public SueloQuebradizo(int posicion) { 
        super(posicion); 
    }

    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {
        int objetos = jugador.getInv().totalObjetos();

        if (objetos > 5) {
            jugador.setPosicion(0);
            partida.setUltimoEvento("¡CRACK! El hielo se rompe. " + jugador.getNombre() + 
                " llevaba " + objetos + " objetos y vuelve al inicio por el peso.");
        } else if (objetos > 0) {
            jugador.setPierdeTurno(true);
            partida.setUltimoEvento("El suelo cruje... " + jugador.getNombre() + 
                " tiene que avanzar muy despacio y pierde el próximo turno.");
        } else {
            partida.setUltimoEvento(jugador.getNombre() + 
                " pasa ligero como una pluma por el suelo quebradizo.");
        }
    }
}
