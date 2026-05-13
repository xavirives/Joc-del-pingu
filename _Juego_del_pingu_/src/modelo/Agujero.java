package modelo;

/**
 * Casilla especial de tipo agujero.
 * Si un jugador cae aquí, retrocede hasta el agujero anterior.
 */
public class Agujero extends Casilla {

    /**
     * Crea una casilla de agujero en una posición concreta del tablero.
     */
    public Agujero(int posicion) { 
        super(posicion); 
    }

    /**
     * Aplica el efecto del agujero sobre el jugador.
     */
    @Override 
    public void realizarAccion(Partida partida, Pinguino jugador) {

        // Busca la posición del agujero anterior.
        int anterior = partida.getTablero().buscarAgujeroAnterior(posicion);

        // Mueve al jugador hasta ese agujero anterior.
        jugador.setPosicion(anterior);

        // Si no hay agujero anterior, el jugador vuelve al inicio.
        if (anterior == 0) {
            partida.setUltimoEvento(jugador.getNombre() + " cae en el primer agujero y vuelve al inicio.");
        } else {
            partida.setUltimoEvento(jugador.getNombre() + " cae en un agujero y vuelve al agujero anterior.");
        }
    }
}