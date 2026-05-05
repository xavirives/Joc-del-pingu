package modelo;

import java.io.Serializable;

/** 
 * Casilla de Nivel Intermedio. 
 * El efecto varía según la carga del inventario del jugador.
 */
public class SueloQuebradizo extends Casilla implements Serializable{
    
    public SueloQuebradizo(int posicion) { 
        super(posicion); 
    }

    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {
        int objetos = jugador.getInv().totalObjetos();

        if (objetos > 5) {
            // Efecto crítico: Demasiado peso rompe el hielo.
            jugador.setPosicion(0);
            partida.setUltimoEvento("¡CRACK! El hielo se rompe. " + jugador.getNombre() + 
                " llevaba " + objetos + " objetos y vuelve al inicio por el peso.");
        } else if (objetos > 0) {
            // Efecto moderado: El jugador avanza con cuidado y pierde tiempo.
            jugador.setPierdeTurno(true);
            partida.setUltimoEvento("El suelo cruje... " + jugador.getNombre() + 
                " tiene que avanzar muy despacio y pierde el próximo turno.");
        } else {
            // Efecto nulo: Sin objetos, el hielo aguanta perfectamente.
            partida.setUltimoEvento(jugador.getNombre() + 
                " pasa ligero como una pluma por el suelo quebradizo.");
        }
    }
}
