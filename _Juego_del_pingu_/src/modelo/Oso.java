package modelo;

/**
 * Casilla especial de tipo oso.
 * Si el jugador cae aquí, puede salvarse usando un pez.
 */
public class Oso extends Casilla {

    /**
     * Crea una casilla de oso en una posición concreta del tablero.
     */
    public Oso(int posicion) { 
        super(posicion); 
    }

    /**
     * Aplica el efecto del oso al jugador.
     * Si tiene un pez, lo usa y se salva.
     * Si no tiene peces, vuelve al inicio.
     */
    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {

        // Intenta quitar 1 pez del inventario del jugador.
        if (jugador.getInv().removeCantidad("pez", 1)) {

            // Si tenía pez, evita el castigo del oso.
            partida.setUltimoEvento("Un oso aparece, pero " + jugador.getNombre() + " usa un pez y se salva.");

        } else {
            // Si no tenía pez, el jugador vuelve a la casilla inicial.
            jugador.setPosicion(0);
            partida.setUltimoEvento("Un oso ataca a " + jugador.getNombre() + " y vuelve al inicio.");
        }
    }
}