package modelo;

import java.io.Serializable;

/**
 * Clase que representa una casilla especial "Suelo Quebradizo" en el tablero.
 * Extiende Casilla para heredar propiedades básicas.
 * Implementa Serializable para poder guardarse en la base de datos.
 * 
 * Efecto: El efecto depende de cuántos objetos lleva el pinguino.
 * - Más de 5 objetos: vuelve al inicio (peso del hielo rompe)
 * - 1-5 objetos: pierde el próximo turno (va lento)
 * - 0 objetos: pasa sin efecto
 */
public class SueloQuebradizo extends Casilla implements Serializable {

    /**
     * Constructor de la casilla SueloQuebradizo.
     * Parámetro: posicion = número de casilla en el tablero donde está este efecto
     */
    public SueloQuebradizo(int posicion) {
        // Llama al constructor de la clase padre (Casilla) con la posición
        super(posicion);
    }

    /**
     * Ejecuta el efecto de esta casilla cuando un pinguino cae en ella.
     * El efecto varía según cuántos objetos lleva el pinguino.
     * Parámetros: partida = la partida actual, jugador = el pinguino que cayó
     */
    @Override
    public void realizarAccion(Partida partida, Pinguino jugador) {
        // Cuenta cuántos objetos totales tiene el pinguino en su inventario
        int objetos = jugador.getInv().totalObjetos();

        // CASO 1: Si lleva MÁS de 5 objetos, el hielo se rompe por el peso
        if (objetos > 5) {
            // Vuelve a la casilla 0 (inicio del tablero)
            jugador.setPosicion(0);
            // Registra el evento con el número de objetos que llevaba
            partida.setUltimoEvento("¡CRACK! El hielo se rompe. " + jugador.getNombre() +
                " llevaba " + objetos + " objetos y vuelve al inicio por el peso.");
        } 
        // CASO 2: Si lleva entre 1 y 5 objetos, el suelo cruje pero aguanta
        else if (objetos > 0) {
            // Establece que el pinguino pierde el próximo turno (va más lento)
            jugador.setPierdeTurno(true);
            // Registra el evento
            partida.setUltimoEvento("El suelo cruje... " + jugador.getNombre() +
                " tiene que avanzar muy despacio y pierde el próximo turno.");
        } 
        // CASO 3: Si lleva 0 objetos, es tan ligero que pasa sin problema
        else {
            // Solo registra que pasó sin efecto negativo
            partida.setUltimoEvento(jugador.getNombre() +
                " pasa ligero como una pluma por el suelo quebradizo.");
        }
    }
}